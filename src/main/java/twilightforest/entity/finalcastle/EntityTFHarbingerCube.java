package twilightforest.entity.finalcastle;

import java.util.List;

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
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;

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
    protected void onDeathUpdate() {
        ++this.deathTime;

        if (this.deathTime == 60) // delay until 3 seconds
        {
            if (!world.isRemote) {
                List<Entity> list = EntityTFHarbingerCube.this.world.getEntitiesWithinAABBExcludingEntity((Entity) EntityTFHarbingerCube.this, EntityTFHarbingerCube.this.getEntityBoundingBox().grow(4.0D));

                boolean mobGriefing = ForgeEventFactory.getMobGriefingEvent(world, this);
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, EntityTFHarbingerCube.EXPLOSION_RADIUS, mobGriefing);
                this.playSound(TFSounds.FINALBOSS_DEATH, 2.5F, 3.0F);

                for (Entity entity : list) {
                    if (entity instanceof EntityLivingBase) {
                        ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * 15, 0));
                    }
                }
            }
            // Fake to trigger super's behaviour
            deathTime = 19;
            super.onDeathUpdate();
            deathTime = 60;
        }
    }

    @Override
    protected ResourceLocation getLootTable() {
        return LOOT_TABLE;
    }
}
