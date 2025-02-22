package twilightforest.entity.finalcastle;

import java.util.List;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.potions.TFPotions;

public class EntityTFHarbingerCube extends EntityMob {
    public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/harbinger_cube");
    private static final float EXPLOSION_RADIUS = 1.0F;

    public EntityTFHarbingerCube(World world) {
        super(world);
        this.setSize(1.9F, 2.4F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(2, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
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
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGround, IBlockState state, BlockPos pos) {
    }

    @Override
    protected void onDeathUpdate() {
        ++this.deathTime;

        if (this.deathTime == 40) // delay until 2 seconds
        {
            if (!world.isRemote) {
                List<Entity> list = EntityTFHarbingerCube.this.world.getEntitiesWithinAABBExcludingEntity((Entity) EntityTFHarbingerCube.this, EntityTFHarbingerCube.this.getEntityBoundingBox().grow(4.0D));

                boolean mobGriefing = ForgeEventFactory.getMobGriefingEvent(world, this);
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, EntityTFHarbingerCube.EXPLOSION_RADIUS, mobGriefing);
                this.playSound(TFSounds.FINALBOSS_DEATH, 2.5F, 3.0F);

                for (Entity entity : list) {
                    if (entity instanceof EntityLivingBase) {
                        ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * 20, 0));
                    }
                }
            }
            // Fake to trigger super's behaviour
            deathTime = 19;
            super.onDeathUpdate();
            deathTime = 40;
        }
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_TABLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_BLAZE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_TNT_PRIMED;
    }
}
