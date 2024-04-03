package twilightforest.entity.boss;

import java.util.List;

import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFIceMob;

public class EntityTFIceCrystal extends EntityTFIceMob {
    public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/ice_crystal");
    private static final float EXPLOSION_RADIUS = 1.0F;

    public int crystalAge;
    public int maxCrystalAge = -1;

    public EntityTFIceCrystal(World world) {
        super(world);
        this.setSize(0.6F, 1.8F);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(2, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(3, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }

    @Override
    public ResourceLocation getLootTable() {
        return LOOT_TABLE;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 8;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.ICE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.ICE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.ICE_DEATH;
    }

    public void setToDieIn10Seconds() {
        this.maxCrystalAge = 200;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!world.isRemote) {
            this.crystalAge++;
            if (this.maxCrystalAge > 0 && this.crystalAge >= this.maxCrystalAge) {
                this.setHealth(0.0F);
            }
        }
    }

    @Override
    protected void onDeathUpdate() {
        ++this.deathTime;

        if (this.deathTime == (this.maxCrystalAge > 0 ? 20 : 60)) {
            if (!world.isRemote) {
                List<Entity> list = EntityTFIceCrystal.this.world.getEntitiesWithinAABBExcludingEntity((Entity) EntityTFIceCrystal.this, EntityTFIceCrystal.this.getEntityBoundingBox().grow(3.0D));

                boolean mobGriefing = ForgeEventFactory.getMobGriefingEvent(world, this);
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, EntityTFIceCrystal.EXPLOSION_RADIUS, mobGriefing);
                this.playSound(TFSounds.ICE_DEATH, 2.5F, 0.5F);

                for (Entity entity : list) {
                    if (entity instanceof EntityLivingBase) {
                        ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(PotionHandler.FROZEN, 20 * 5, 0));
                    }
                }
            }
            // Fake to trigger super's behaviour
            deathTime = 19;
            super.onDeathUpdate();
            deathTime = this.maxCrystalAge > 0 ? 20 : 60;
        }
    }
}
