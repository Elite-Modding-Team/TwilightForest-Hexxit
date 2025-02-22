package twilightforest.entity.finalcastle;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.EntityTFAdherentBolt;
import twilightforest.entity.ITFCharger;
import twilightforest.potions.TFPotions;

public class EntityTFAdherent extends EntityMob implements IRangedAttackMob, ITFCharger {

    public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/adherent");

    private static final DataParameter<Boolean> CHARGE_FLAG = EntityDataManager.createKey(EntityTFAdherent.class, DataSerializers.BOOLEAN);

    public EntityTFAdherent(World world) {
        super(world);
        this.setSize(0.8F, 2.2F);

        this.moveHelper = new EntityFlyHelper(this);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackRanged(this, 1.25D, 20, 10.0F));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(CHARGE_FLAG, false);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.45D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
    }

    @Override
    protected PathNavigate createNavigator(World worldIn) {
        PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
        pathnavigateflying.setCanOpenDoors(false);
        pathnavigateflying.setCanFloat(true);
        pathnavigateflying.setCanEnterDoors(true);
        return pathnavigateflying;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        Entity entity = source.getTrueSource();

        if (entity != null && entity instanceof EntityLivingBase && entity.isOnSameTeam(this)) {
            return false;
        }

        return super.attackEntityFrom(source, damage);
    }

    @Override
    public boolean isOnSameTeam(Entity entity) {
        if (entity == null) {
            return false;
        } else if (entity == this) {
            return true;
        } else if (super.isOnSameTeam(entity)) {
            return true;
        } else if (entity instanceof EntityWroughtnaut || entity instanceof EntityTFCastleMob || entity instanceof EntityTFCastleGiant || entity instanceof EntityTFAdherent || entity instanceof EntityTFHarbingerCube) {
            return this.getTeam() == null && entity.getTeam() == null;
        } else {
            return false;
        }
    }

    // Immune to ice effects and wither
    @Override
    public boolean isPotionApplicable(PotionEffect effect) {
        return effect.getPotion() != TFPotions.frosty && effect.getPotion() != PotionHandler.FROZEN && effect.getPotion() != MobEffects.WITHER && super.isPotionApplicable(effect);
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase attackTarget, float extraDamage) {
        EntityTFAdherentBolt adherentBolt = new EntityTFAdherentBolt(this.world, this);
        playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));

        // [VanillaCopy] adapted from EntitySnowman, with lower velocity and inaccuracy calculation
        double d0 = attackTarget.posY + (double) attackTarget.getEyeHeight() - 1.100000023841858D;
        double d1 = attackTarget.posX - this.posX;
        double d2 = d0 - adherentBolt.posY;
        double d3 = attackTarget.posZ - this.posZ;
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        adherentBolt.shoot(d1, d2 + (double) f, d3, 0.6F, 10 - this.world.getDifficulty().getId() * 4);

        this.world.spawnEntity(adherentBolt);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= 0.6D;
        }

        if (this.world.isRemote) {
            for (int i = 0; i < 1; ++i) {
                TwilightForestMod.proxy.spawnParticle(TFParticleType.ANNIHILATE, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_BLAZE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE;
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    } // todo 1.12

    @Override
    public boolean isCharging() {
        return dataManager.get(CHARGE_FLAG);
    }

    @Override
    public void setCharging(boolean flag) {
        dataManager.set(CHARGE_FLAG, flag);
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_TABLE;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }
}
