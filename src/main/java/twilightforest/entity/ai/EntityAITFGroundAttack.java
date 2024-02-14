package twilightforest.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import twilightforest.entity.EntityTFMinotaur;
import twilightforest.entity.boss.EntityTFMinoshroom;

import java.util.List;

public class EntityAITFGroundAttack extends EntityAIBase {
    private static final double MIN_RANGE_SQ = 2.0D;
    private static final double MAX_RANGE_SQ = 48.0D;
    private static final int FREQ = 18;

    private EntityTFMinoshroom attacker;
    private EntityLivingBase attackTarget;
    private int attackTick;

    public EntityAITFGroundAttack(EntityTFMinoshroom entityTFMinoshroom) {
        this.attacker = entityTFMinoshroom;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        this.attackTarget = this.attacker.getAttackTarget();

        if (this.attackTarget == null) {
            return false;
        } else {
            double distance = this.attacker.getDistanceSq(this.attackTarget);
            if (distance < MIN_RANGE_SQ || distance > MAX_RANGE_SQ) {
                return false;
            } else if (!this.attacker.onGround) {
                return false;
            } else {

                if (this.attacker.canEntityBeSeen(attackTarget)) {
                    return this.attacker.getRNG().nextInt(FREQ) == 0;
                } else {
                    return this.attacker.getRNG().nextInt(FREQ - 4) == 0;
                }

            }

        }
    }

    @Override
    public void startExecuting() {
        attackTick = 12 + attacker.getRNG().nextInt(12);
        attacker.setMaxCharge(attackTick);
        attacker.setGroundAttackCharge(true);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.attackTick >= 0;
    }

    @Override
    public void resetTask() {
        this.attackTick = 0;
        this.attackTarget = null;
    }

    @Override
    public void updateTask() {
        // Look where we're going
    	if (this.attackTick == 12) this.attacker.playSound(SoundEvents.ENTITY_ITEM_BREAK, 2.0F, 2.0F + this.attacker.getRNG().nextFloat() * 0.1F);
        this.attacker.getLookHelper().setLookPositionWithEntity(attackTarget, 30.0F, 30.0F);
        this.attacker.getMoveHelper().action = EntityMoveHelper.Action.WAIT;

        if (this.attackTick-- <= 0) {
            this.attacker.setGroundAttackCharge(false);
            this.attacker.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 2.0F, 1.0F + this.attacker.getRNG().nextFloat() * 0.1F);

            AxisAlignedBB selection = new AxisAlignedBB(this.attacker.getPosition().getX() - 7.5F, this.attacker.getPosition().getY(), this.attacker.getPosition().getZ() - 7.5F, this.attacker.getPosition().getX() + 7.5F, this.attacker.getPosition().getY() + 3.0F, this.attacker.getPosition().getZ() + 7.5F);

            List<Entity> hit = attacker.world.getEntitiesWithinAABB(Entity.class, selection, entity -> !(entity instanceof EntityTFMinotaur));
            for (Entity entity : hit) {
                if (entity == this.attacker) {
                    continue;

                }

                if (entity instanceof EntityLivingBase) {
                    if (entity.onGround) {
                        entity.motionY += 0.23;
                        entity.attackEntityFrom(DamageSource.causeMobDamage(this.attacker).setDamageBypassesArmor(), (float) (this.attacker.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue() * 0.5F));
                    }
                }
            }

        }
    }
}