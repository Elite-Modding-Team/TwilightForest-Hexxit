package twilightforest.entity.boss;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFFeature;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;
import twilightforest.util.EntityUtil;
import twilightforest.util.WorldUtil;
import twilightforest.world.TFWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntityTFHydra extends EntityLiving implements IEntityMultiPart, IMob {

    public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/hydra");

    private static final int TICKS_BEFORE_HEALING = 600;
    private static final int HEAD_RESPAWN_TICKS = 50;
    private static final int HEAD_MAX_DAMAGE = 100;
    private static final float ARMOR_MULTIPLIER = 8.0F;
    private static final int MAX_HEALTH = 450;

    private static final int SECONDARY_FLAME_CHANCE = 50;
    private static final int SECONDARY_MORTAR_CHANCE = 50;

    private static final DataParameter<Boolean> DATA_SPAWNHEADS = EntityDataManager.createKey(EntityTFHydra.class, DataSerializers.BOOLEAN);

    private final Entity partArray[];

    public final int numHeads = 7;
    public final HydraHeadContainer[] hc = new HydraHeadContainer[numHeads];

    public final MultiPartEntityPart body = new MultiPartEntityPart(this, "body", 4F, 4F);
    private final MultiPartEntityPart leftLeg = new MultiPartEntityPart(this, "leg", 2F, 3F);
    private final MultiPartEntityPart rightLeg = new MultiPartEntityPart(this, "leg", 2F, 3F);
    private final MultiPartEntityPart tail = new MultiPartEntityPart(this, "tail", 4F, 4F);
    private final BossInfoServer bossInfo = new BossInfoServer(getDisplayName(), BossInfo.Color.BLUE, BossInfo.Overlay.PROGRESS);

    private int ticksSinceDamaged = 0;

    public EntityTFHydra(World world) {
        super(world);

        List<Entity> parts = new ArrayList<>();
        parts.add(body);
        parts.add(leftLeg);
        parts.add(rightLeg);
        parts.add(tail);

        for (int i = 0; i < numHeads; i++) {
            hc[i] = new HydraHeadContainer(this, i, i < 3);
            Collections.addAll(parts, hc[i].getNeckArray());
        }

        partArray = parts.toArray(new Entity[0]);

        this.ignoreFrustumCheck = true;
        this.isImmuneToFire = true;
        this.experienceValue = 511;

        setSize(16F, 12F);
        setSpawnHeads(true);
    }

    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(MAX_HEALTH);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(12.0D);
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    protected void despawnEntity() {
        if (world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            world.setBlockState(getPosition().add(0, 2, 0), TFBlocks.boss_spawner.getDefaultState().withProperty(BlockTFBossSpawner.VARIANT, BossVariant.HYDRA));
            setDead();
            for (HydraHeadContainer container : hc) {
                if (container.headEntity != null) {
                    container.headEntity.setDead();
                }
            }
        } else {
            super.despawnEntity();
        }
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    // [Vanilla Copy] from EntityLivingBase. Hydra doesn't like the one from EntityLiving for whatever reason
    @Override
    protected float updateDistance(float p_110146_1_, float p_110146_2_) {
        float f = MathHelper.wrapDegrees(p_110146_1_ - this.renderYawOffset);
        this.renderYawOffset += f * 0.3F;
        float f1 = MathHelper.wrapDegrees(this.rotationYaw - this.renderYawOffset);
        boolean flag = f1 < -90.0F || f1 >= 90.0F;

        if (f1 < -75.0F) {
            f1 = -75.0F;
        }

        if (f1 >= 75.0F) {
            f1 = 75.0F;
        }

        this.renderYawOffset = this.rotationYaw - f1;

        if (f1 * f1 > 2500.0F) {
            this.renderYawOffset += f1 * 0.2F;
        }

        if (flag) {
            p_110146_2_ *= -1.0F;
        }

        return p_110146_2_;
    }

    @Override
    public void onLivingUpdate() {
        if (hc[0].headEntity == null || hc[1].headEntity == null || hc[2].headEntity == null) {
            // don't spawn if we're connected in multiplayer
            if (!world.isRemote && shouldSpawnHeads()) {
                for (int i = 0; i < numHeads; i++) {
                    hc[i].headEntity = new EntityTFHydraHead(this, "head" + i, 3F, 3F);
                    hc[i].headEntity.setPosition(this.posX, this.posY, this.posZ);
                    hc[i].setHeadPosition();
                    world.spawnEntity(hc[i].headEntity);
                }

                setSpawnHeads(false);
            }
        }

        body.onUpdate();

        // update all heads (maybe we should change to only active ones
        for (int i = 0; i < numHeads; i++) {
            hc[i].onUpdate();
        }

        if (this.hurtTime > 0) {
            for (int i = 0; i < numHeads; i++) {
                hc[i].setHurtTime(this.hurtTime);
            }
        }

        this.ticksSinceDamaged++;

        if (!this.world.isRemote && this.ticksSinceDamaged > TICKS_BEFORE_HEALING && this.ticksSinceDamaged % 5 == 0) {
            this.heal(1);
        }

        super.onLivingUpdate();

        body.width = body.height = 6.0F;
        tail.width = 6.0F;
        tail.height = 2.0F;

        // set body part positions
        float angle;
        double dx, dy, dz;

        // body goes behind the actual position of the hydra
        angle = (((renderYawOffset + 180) * 3.141593F) / 180F);

        dx = posX - MathHelper.sin(angle) * 3.0;
        dy = posY + 0.1;
        dz = posZ + MathHelper.cos(angle) * 3.0;
        body.setPosition(dx, dy, dz);

        dx = posX - MathHelper.sin(angle) * 10.5;
        dy = posY + 0.1;
        dz = posZ + MathHelper.cos(angle) * 10.5;
        tail.setPosition(dx, dy, dz);

        if (hurtTime == 0) {
            this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.body.getEntityBoundingBox()), this.body);
            this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.tail.getEntityBoundingBox()), this.tail);
        }

        // destroy blocks
        if (!this.world.isRemote) {
            this.destroyBlocksInAABB(this.body.getEntityBoundingBox());
            this.destroyBlocksInAABB(this.tail.getEntityBoundingBox());

            for (int i = 0; i < numHeads; i++) {
                if (hc[i].headEntity != null && hc[i].isActive()) {
                    this.destroyBlocksInAABB(this.hc[i].headEntity.getEntityBoundingBox());
                }
            }

            // smash blocks beneath us too
            if (this.ticksExisted % 20 == 0) {
                if (isUnsteadySurfaceBeneath()) {
                    this.destroyBlocksInAABB(this.getEntityBoundingBox().offset(0, -1, 0));

                }
            }

            bossInfo.setPercent(getHealth() / getMaxHealth());
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DATA_SPAWNHEADS, false);
    }

    private boolean shouldSpawnHeads() {
        return dataManager.get(DATA_SPAWNHEADS);
    }

    private void setSpawnHeads(boolean flag) {
        dataManager.set(DATA_SPAWNHEADS, flag);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("SpawnHeads", shouldSpawnHeads());
        compound.setByte("NumHeads", (byte) countActiveHeads());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setSpawnHeads(compound.getBoolean("SpawnHeads"));
        activateNumberOfHeads(compound.getByte("NumHeads"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }


    // TODO modernize this more (old AI copypasta still kind of here)
    private int numTicksToChaseTarget;

    @Override
    protected void updateAITasks() {
        moveStrafing = 0.0F;
        moveForward = 0.0F;
        float f = 48F;

        // kill heads that have taken too much damage
        for (int i = 0; i < numHeads; i++) {
            if (hc[i].isActive() && hc[i].getDamageTaken() > HEAD_MAX_DAMAGE) {
                hc[i].setNextState(HydraHeadContainer.State.DYING);
                hc[i].endCurrentAction();

                // set this head and a random dead head to respawn
                hc[i].setRespawnCounter(HEAD_RESPAWN_TICKS);
                int otherHead = getRandomDeadHead();
                if (otherHead != -1) {
                    hc[otherHead].setRespawnCounter(HEAD_RESPAWN_TICKS);
                }
            }
        }

        if (rand.nextFloat() < 0.7F) {
            EntityPlayer entityplayer1 = world.getNearestAttackablePlayer(this, f, f);

            if (entityplayer1 != null) {
                setAttackTarget(entityplayer1);
                numTicksToChaseTarget = 100 + rand.nextInt(20);
            } else {
                randomYawVelocity = (rand.nextFloat() - 0.5F) * 20F;
            }
        }

        if (getAttackTarget() != null) {
            faceEntity(getAttackTarget(), 10F, getVerticalFaceSpeed());

            // have any heads not currently attacking switch to the primary target
            for (int i = 0; i < numHeads; i++) {
                if (!hc[i].isAttacking() && !hc[i].isSecondaryAttacking) {
                    hc[i].setTargetEntity(getAttackTarget());
                }
            }

            // let's pick an attack
            if (this.getAttackTarget().isEntityAlive()) {
                float distance = this.getAttackTarget().getDistance(this);

                if (this.getEntitySenses().canSee(this.getAttackTarget())) {
                    this.attackEntity(this.getAttackTarget(), distance);
                }
            }

            if (numTicksToChaseTarget-- <= 0 || getAttackTarget().isDead || getAttackTarget().getDistanceSq(this) > (double) (f * f)) {
                setAttackTarget(null);
            }
        } else {
            if (rand.nextFloat() < 0.05F) {
                randomYawVelocity = (rand.nextFloat() - 0.5F) * 20F;
            }

            rotationYaw += randomYawVelocity;
            rotationPitch = 0;

            // TODO: while we are idle, consider having the heads breathe fire on passive mobs

            // set idle heads to no target
            for (int i = 0; i < numHeads; i++) {
                if (hc[i].isIdle()) {
                    hc[i].setTargetEntity(null);
                }
            }
        }

        // heads that are free at this point may consider attacking secondary targets
        this.secondaryAttacks();
    }

    // TODO: make random
    private int getRandomDeadHead() {
        for (int i = 0; i < numHeads; i++) {
            if (hc[i].canRespawn()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Used when re-loading from save.  Assumes three heads are active and activates more if necessary.
     */
    private void activateNumberOfHeads(int howMany) {
        int moreHeads = howMany - this.countActiveHeads();

        for (int i = 0; i < moreHeads; i++) {
            int otherHead = getRandomDeadHead();
            if (otherHead != -1) {
                // move directly into not dead
                hc[otherHead].setNextState(HydraHeadContainer.State.IDLE);
                hc[otherHead].endCurrentAction();
            }
        }
    }

    /**
     * Count timers, and pick an attack against the entity if our timer says go
     */
    private void attackEntity(Entity target, float distance) {

        int BITE_CHANCE = 10;
        int FLAME_CHANCE = 100;
        int MORTAR_CHANCE = 160;

        boolean targetAbove = target.getEntityBoundingBox().minY > this.getEntityBoundingBox().maxY;

        // three main heads can do these kinds of attacks
        for (int i = 0; i < 3; i++) {
            if (hc[i].isIdle()) {
                if (distance > 4 && distance < 10 && rand.nextInt(BITE_CHANCE) == 0 && this.countActiveHeads() > 2) {
                    hc[i].setNextState(HydraHeadContainer.State.BITE_BEGINNING);
                } else if (distance > 0 && distance < 20 && rand.nextInt(FLAME_CHANCE) == 0) {
                    hc[i].setNextState(HydraHeadContainer.State.FLAME_BEGINNING);
                } else if (distance > 8 && distance < 32 && !targetAbove && rand.nextInt(MORTAR_CHANCE) == 0) {
                    hc[i].setNextState(HydraHeadContainer.State.MORTAR_BEGINNING);
                }
            }
        }

        // heads 4-7 can do everything but bite
        for (int i = 3; i < numHeads; i++) {
            if (hc[i].isIdle()) {
                if (distance > 0 && distance < 20 && rand.nextInt(FLAME_CHANCE) == 0) {
                    hc[i].setNextState(HydraHeadContainer.State.FLAME_BEGINNING);
                } else if (distance > 8 && distance < 32 && !targetAbove && rand.nextInt(MORTAR_CHANCE) == 0) {
                    hc[i].setNextState(HydraHeadContainer.State.MORTAR_BEGINNING);
                }
            }
        }
    }

    private int countActiveHeads() {
        int count = 0;

        for (int i = 0; i < numHeads; i++) {
            if (hc[i].isActive()) {
                count++;
            }
        }

        return count;
    }

    /**
     * Called sometime after the main attackEntity routine.  Finds a valid secondary target and has an unoccupied head start an attack against it.
     * <p>
     * The center head (head 0) does not make secondary attacks
     */
    private void secondaryAttacks() {
        for (int i = 0; i < numHeads; i++) {
            if (hc[i].headEntity == null) {
                return;
            }
        }

        EntityLivingBase secondaryTarget = findSecondaryTarget(20);

        if (secondaryTarget != null) {
            float distance = secondaryTarget.getDistance(this);

            for (int i = 1; i < numHeads; i++) {
                if (hc[i].isActive() && hc[i].isIdle() && isTargetOnThisSide(i, secondaryTarget)) {
                    if (distance > 0 && distance < 20 && rand.nextInt(SECONDARY_FLAME_CHANCE) == 0) {
                        hc[i].setTargetEntity(secondaryTarget);
                        hc[i].isSecondaryAttacking = true;
                        hc[i].setNextState(HydraHeadContainer.State.FLAME_BEGINNING);
                    } else if (distance > 8 && distance < 32 && rand.nextInt(SECONDARY_MORTAR_CHANCE) == 0) {
                        hc[i].setTargetEntity(secondaryTarget);
                        hc[i].isSecondaryAttacking = true;
                        hc[i].setNextState(HydraHeadContainer.State.MORTAR_BEGINNING);
                    }
                }
            }
        }
    }

    /**
     * Used to make sure heads don't attack across the whole body
     */
    private boolean isTargetOnThisSide(int headNum, Entity target) {
        double headDist = distanceSqXZ(hc[headNum].headEntity, target);
        double middleDist = distanceSqXZ(this, target);
        return headDist < middleDist;
    }

    /**
     * Square of distance between two entities with y not a factor, just x and z
     */
    private double distanceSqXZ(Entity headEntity, Entity target) {
        double distX = headEntity.posX - target.posX;
        double distZ = headEntity.posZ - target.posZ;
        return distX * distX + distZ * distZ;
    }

    @Nullable
    private EntityLivingBase findSecondaryTarget(double range) {
        return this.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.posX, this.posY, this.posZ, this.posX + 1, this.posY + 1, this.posZ + 1).grow(range, range, range))
                .stream()
                .filter(e -> !(e instanceof EntityTFHydra || e instanceof EntityTFHydraPart))
                .filter(e -> e != getAttackTarget() && !isAnyHeadTargeting(e) && getEntitySenses().canSee(e))
                .min(Comparator.comparingDouble(this::getDistanceSq)).orElse(null);
    }

    private boolean isAnyHeadTargeting(Entity targetEntity) {
        for (int i = 0; i < numHeads; i++) {
            if (hc[i].targetEntity != null && hc[i].targetEntity.equals(targetEntity)) {
                return true;
            }
        }

        return false;
    }

    // [VanillaCopy] based on EntityDragon.collideWithEntities
    private void collideWithEntities(List<Entity> entities, Entity part) {
        double d0 = (part.getEntityBoundingBox().minX + part.getEntityBoundingBox().maxX) / 2.0D;
        double d1 = (part.getEntityBoundingBox().minZ + part.getEntityBoundingBox().maxZ) / 2.0D;

        for (Entity entity : entities) {
            if (entity instanceof EntityLivingBase) {
                double d2 = entity.posX - d0;
                double d3 = entity.posZ - d1;
                double d4 = d2 * d2 + d3 * d3;
                entity.addVelocity(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
            }
        }
    }

    /**
     * Check the surface immediately beneath us, if it is less than 80% solid
     */
    private boolean isUnsteadySurfaceBeneath() {
        int minX = MathHelper.floor(this.getEntityBoundingBox().minX);
        int minZ = MathHelper.floor(this.getEntityBoundingBox().minZ);
        int maxX = MathHelper.floor(this.getEntityBoundingBox().maxX);
        int maxZ = MathHelper.floor(this.getEntityBoundingBox().maxZ);
        int minY = MathHelper.floor(this.getEntityBoundingBox().minY);

        int solid = 0;
        int total = 0;

        int dy = minY - 1;

        for (int dx = minX; dx <= maxX; ++dx) {
            for (int dz = minZ; dz <= maxZ; ++dz) {
                total++;
                if (this.world.getBlockState(new BlockPos(dx, dy, dz)).getMaterial().isSolid()) {
                    solid++;
                }
            }
        }

        return ((float) solid / (float) total) < 0.6F;
    }

    private void destroyBlocksInAABB(AxisAlignedBB box) {
        if (ForgeEventFactory.getMobGriefingEvent(world, this)) {
            for (BlockPos pos : WorldUtil.getAllInBB(box)) {
                if (EntityUtil.canDestroyBlock(world, pos, this)) {
                    world.destroyBlock(pos, false);
                }
            }
        }
    }

    @Override
    public int getVerticalFaceSpeed() {
        return 500;
    }

    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
        return calculateRange(source) <= 400 && super.attackEntityFrom(source, Math.round(damage / 8.0F));
    }

    public boolean attackEntityFromPart(EntityTFHydraPart part, DamageSource source, float damage) {
        // if we're in a wall, kill that wall
        if (!world.isRemote && source == DamageSource.IN_WALL) {
            destroyBlocksInAABB(part.getEntityBoundingBox());
        }

        HydraHeadContainer headCon = null;

        for (int i = 0; i < numHeads; i++) {
            if (hc[i].headEntity == part) {
                headCon = hc[i];
            }
        }

        double range = calculateRange(source);

        if (range > 400) {
            return false;
        }

        // ignore hits on dying heads, it's weird
        if (headCon != null && !headCon.isActive()) {
            return false;
        }

        boolean tookDamage;
        if (headCon != null && headCon.getCurrentMouthOpen() > 0.5) {
            tookDamage = super.attackEntityFrom(source, damage);
            headCon.addDamage(damage);
        } else {
            int armoredDamage = Math.round(damage / ARMOR_MULTIPLIER);
            tookDamage = super.attackEntityFrom(source, armoredDamage);

            if (headCon != null) {
                headCon.addDamage(armoredDamage);
            }
        }

        if (tookDamage) {
            this.ticksSinceDamaged = 0;
        }

        return tookDamage;
    }

    private double calculateRange(DamageSource damagesource) {
        return damagesource.getTrueSource() != null ? getDistanceSq(damagesource.getTrueSource()) : -1;
    }

    @Override
    public boolean attackEntityFrom(DamageSource src, float damage) {
        return src == DamageSource.OUT_OF_WORLD && super.attackEntityFrom(src, damage);
    }

    /**
     * We need to do this for the bounding boxes on the parts to become active
     */
    @Override
    public Entity[] getParts() {
        return partArray;
    }

    /**
     * This is set as off for the hydra, which has an enormous bounding box, but set as on for the parts.
     */
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    /**
     * If this is on, the player pushes us based on our bounding box rather than it going by parts
     */
    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
    }

    @Override
    public void knockBack(Entity entity, float strength, double xRatio, double zRatio) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.HYDRA_GROWL;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.HYDRA_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.HYDRA_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 2F;
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        // mark the lair as defeated
        if (!world.isRemote) {
            this.bossInfo.setPercent(0.0F);
            TFWorld.markStructureConquered(world, new BlockPos(this), TFFeature.HYDRA_LAIR);
        }
    }

    @Override
    public ResourceLocation getLootTable() {
        return LOOT_TABLE;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    protected boolean canBeRidden(Entity entity) {
        return false;
    }

    @Override
    protected void onDeathUpdate() {
        ++this.deathTime;

        // stop any head actions on death
        if (deathTime == 1) {
            for (int i = 0; i < numHeads; i++) {
                hc[i].setRespawnCounter(-1);
                if (hc[i].isActive()) {
                    hc[i].setNextState(HydraHeadContainer.State.IDLE);
                    hc[i].endCurrentAction();
                    hc[i].setHurtTime(200);
                }
            }
        }

        // heads die off one by one
        if (this.deathTime <= 140 && this.deathTime % 20 == 0) {
            int headToDie = (this.deathTime / 20) - 1;

            if (hc[headToDie].isActive()) {
                hc[headToDie].setNextState(HydraHeadContainer.State.DYING);
                hc[headToDie].endCurrentAction();
            }
        }

        if (this.deathTime == 200) {
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))) {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0) {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }

            this.setDead();
        }

        for (int i = 0; i < 20; ++i) {
            double vx = this.rand.nextGaussian() * 0.02D;
            double vy = this.rand.nextGaussian() * 0.02D;
            double vz = this.rand.nextGaussian() * 0.02D;
            EnumParticleTypes particle = rand.nextInt(2) == 0 ? EnumParticleTypes.EXPLOSION_LARGE : EnumParticleTypes.EXPLOSION_NORMAL;
            this.world.spawnParticle(particle,
                    this.posX + this.rand.nextFloat() * this.body.width * 2.0F - this.body.width,
                    this.posY + this.rand.nextFloat() * this.body.height,
                    this.posZ + this.rand.nextFloat() * this.body.width * 2.0F - this.body.width,
                    vx, vy, vz
            );
        }
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }
}
