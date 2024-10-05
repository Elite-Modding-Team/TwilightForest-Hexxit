package twilightforest.entity.ai;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import twilightforest.entity.boss.EntityTFLich;
import twilightforest.item.TFItems;
import twilightforest.item.scepter.ItemTFScepterLifeDrain;

public class EntityAITFLichPopMobs extends EntityAIBase {

    private final EntityTFLich lich;

    public EntityAITFLichPopMobs(EntityTFLich lich) {
        this.lich = lich;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        return !this.lich.isShadowClone() &&
                this.lich.getHealth() < this.lich.getMaxHealth() &&
                this.lich.getPopCooldown() == 0 &&
                !this.lich.world.getEntitiesWithinAABB(EntityLiving.class,
                        this.lich.getEntityBoundingBox().grow(32.0D, 16.0D, 32.0D),
                        e -> EntityTFLich.POPPABLE.contains(e.getClass()) && this.lich.getEntitySenses().canSee(e)).isEmpty();
    }

    @Override
    public void startExecuting() {
        lich.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.lifedrain_scepter));
    }

    @Override
    public void updateTask() {
        super.updateTask();

        // TODO: Add life drain scepter particles
        for (EntityLiving mob : lich.world.getEntitiesWithinAABB(EntityLiving.class, this.lich.getEntityBoundingBox().grow(32.0D, 16.0D, 32.0D), e -> EntityTFLich.POPPABLE.contains(e.getClass()))) {
            if (this.lich.getEntitySenses().canSee(mob)) {
                mob.setDead();
                mob.spawnExplosionParticle();

                // Play sounds
                this.lich.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 3.0F, 0.4F + this.lich.getRNG().nextFloat() * 0.2F);
                mob.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 0.7F, 2.0F + this.lich.getRNG().nextFloat() * 0.2F);

                // Heal 5% of max health
                this.lich.heal(this.lich.getMaxHealth() * 5.0F / 100.0F);

                this.lich.swingArm(EnumHand.MAIN_HAND);
                this.lich.setPopCooldown(40);
                break;
            }
        }
    }
}
