package twilightforest.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import twilightforest.entity.boss.EntityTFLich;
import twilightforest.item.TFItems;

public class EntityAITFLichPopMobs extends EntityAIBase {

    private final EntityTFLich lich;

    public EntityAITFLichPopMobs(EntityTFLich lich) {
        this.lich = lich;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return !this.lich.isShadowClone() && this.lich.getHealth() < this.lich.getMaxHealth() && this.lich.getPopCooldown() == 0 && !this.lich.world.getEntitiesWithinAABB(EntityLiving.class, this.lich.getEntityBoundingBox().grow(32.0D, 16.0D, 32.0D), e -> EntityTFLich.POPPABLE.contains(e.getClass()) && this.lich.getEntitySenses().canSee(e)).isEmpty();
    }

    @Override
    public void startExecuting() {
        lich.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.lifedrain_scepter));
    }

    @Override
    public void updateTask() {
        super.updateTask();

        for (EntityLiving mob : lich.world.getEntitiesWithinAABB(EntityLiving.class, this.lich.getEntityBoundingBox().grow(32.0D, 16.0D, 32.0D), e -> EntityTFLich.POPPABLE.contains(e.getClass()))) {
            if (this.lich.getEntitySenses().canSee(mob)) {
                spawnRedParticles(lich.world, lich.posX, lich.posY + lich.getEyeHeight(), lich.posZ, mob.posX, mob.posY + mob.getEyeHeight(), mob.posZ);
                spawnShatteringParticles(mob.world, mob);
                mob.spawnExplosionParticle();

                // Play sounds
                this.lich.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 3.0F, 0.4F + this.lich.getRNG().nextFloat() * 0.2F);
                mob.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 0.7F, 2.0F + this.lich.getRNG().nextFloat() * 0.2F);

                mob.setDead();

                // Heal 5% of max health
                this.lich.heal(this.lich.getMaxHealth() * 5.0F / 100.0F);

                this.lich.swingArm(EnumHand.MAIN_HAND);
                this.lich.setPopCooldown(40);
                break;
            }
        }
    }

    private void spawnRedParticles(World world, double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
        // make particle trail
        int particles = 32;
        for (int i = 0; i < particles; i++) {
            double trailFactor = i / (particles - 1.0D);
            float f = 1.0F;
            float f1 = 0.5F;
            float f2 = 0.5F;
            double tx = srcX + (destX - srcX) * trailFactor + world.rand.nextGaussian() * 0.005;
            double ty = srcY + (destY - srcY) * trailFactor + world.rand.nextGaussian() * 0.005;
            double tz = srcZ + (destZ - srcZ) * trailFactor + world.rand.nextGaussian() * 0.005;
            world.spawnParticle(EnumParticleTypes.SPELL_MOB, tx, ty, tz, f, f1, f2);
        }
    }

    /**
     * Animates the target falling apart into a rain of shatter particles
     */
    protected void spawnShatteringParticles(World world, EntityLivingBase target) {
        int itemId = Item.getIdFromItem(Items.ROTTEN_FLESH);
        for (int i = 0; i < 50; ++i) {
            double gaussX = world.rand.nextGaussian() * 0.02D;
            double gaussY = world.rand.nextGaussian() * 0.02D;
            double gaussZ = world.rand.nextGaussian() * 0.02D;
            double gaussFactor = 10.0D;
            world.spawnParticle(EnumParticleTypes.ITEM_CRACK, target.posX + world.rand.nextFloat() * target.width * 2.0F - target.width - gaussX * gaussFactor, target.posY + world.rand.nextFloat() * target.height - gaussY * gaussFactor, target.posZ + world.rand.nextFloat() * target.width * 2.0F - target.width - gaussZ * gaussFactor, gaussX, gaussY, gaussZ, itemId);
        }
    }
}
