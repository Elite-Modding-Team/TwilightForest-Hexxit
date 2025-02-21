package twilightforest.entity.finalcastle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.entity.NoClipMoveHelper;
import twilightforest.item.TFItems;

import java.util.Random;

public class EntityTFCastlePhantom extends EntityTFCastleMob {
    public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/castle_phantom");

    public EntityTFCastlePhantom(World world) {
        super(world);
        moveHelper = new NoClipMoveHelper(this);
        noClip = true;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(4, new AIAttack(this));
        this.tasks.addTask(5, new AIFlyTowardsTarget(this));
        this.tasks.addTask(6, new AIRandomFly(this));
        this.tasks.addTask(7, new AILookAround(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
    }

    @Override
    public boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if (this.isInWater()) {
            this.moveRelative(strafe, vertical, forward, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.8F;
            this.motionY *= 0.8F;
            this.motionZ *= 0.8F;
        } else if (this.isInLava()) {
            this.moveRelative(strafe, vertical, forward, 0.02F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5F;
            this.motionY *= 0.5F;
            this.motionZ *= 0.5F;
        } else {
            float f = 0.91F;
            if (this.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                IBlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }

            float f1 = 0.16277136F / (f * f * f);
            this.moveRelative(strafe, vertical, forward, this.onGround ? 0.1F * f1 : 0.02F);
            f = 0.91F;
            if (this.onGround) {
                BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
                IBlockState underState = this.world.getBlockState(underPos);
                f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.91F;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= f;
            this.motionY *= f;
            this.motionZ *= f;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0F;
        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.regal_axe));
        this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TFItems.regal_helmet));
        this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(TFItems.regal_chestplate));
    }

    @Override
    public ResourceLocation getLootTable() {
        return LOOT_TABLE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TFSounds.WRAITH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TFSounds.WRAITH;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFSounds.WRAITH;
    }

    static class AIFlyTowardsTarget extends EntityAIBase {
        private final EntityTFCastlePhantom taskOwner;

        AIFlyTowardsTarget(EntityTFCastlePhantom wraith) {
            this.taskOwner = wraith;
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            return taskOwner.getAttackTarget() != null;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {
            EntityLivingBase target = taskOwner.getAttackTarget();
            if (target != null) taskOwner.getMoveHelper().setMoveTo(target.posX, target.posY, target.posZ, 0.5F);
        }
    }

    // Similar to EntityAIAttackMelee but simpler (no pathfinding)
    static class AIAttack extends EntityAIBase {
        private final EntityTFCastlePhantom taskOwner;
        private int attackTick = 20;

        AIAttack(EntityTFCastlePhantom taskOwner) {
            this.taskOwner = taskOwner;
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase target = taskOwner.getAttackTarget();

            return target != null && target.getEntityBoundingBox().maxY > taskOwner.getEntityBoundingBox().minY && target.getEntityBoundingBox().minY < taskOwner.getEntityBoundingBox().maxY && taskOwner.getDistanceSq(target) <= 4.0D;
        }

        @Override
        public void updateTask() {
            if (attackTick > 0) {
                attackTick--;
            }
        }

        @Override
        public void resetTask() {
            attackTick = 20;
        }

        @Override
        public void startExecuting() {
            if (taskOwner.getAttackTarget() != null) taskOwner.attackEntityAsMob(taskOwner.getAttackTarget());
            attackTick = 20;
        }
    }

    static class AIRandomFly extends EntityAIBase {
        private final EntityTFCastlePhantom parentEntity;

        public AIRandomFly(EntityTFCastlePhantom wraith) {
            this.parentEntity = wraith;
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            if (parentEntity.getAttackTarget() != null) return false;
            EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();
            double d0 = entitymovehelper.getX() - this.parentEntity.posX;
            double d1 = entitymovehelper.getY() - this.parentEntity.posY;
            double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            return d3 < 1.0D || d3 > 3600.0D;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {
            Random random = this.parentEntity.getRNG();
            double d0 = this.parentEntity.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.parentEntity.posY + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.parentEntity.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 0.5D);
        }
    }

    public static class AILookAround extends EntityAIBase {
        private final EntityTFCastlePhantom parentEntity;

        public AILookAround(EntityTFCastlePhantom wraith) {
            this.parentEntity = wraith;
            this.setMutexBits(2);
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public void updateTask() {
            if (this.parentEntity.getAttackTarget() == null) {
                this.parentEntity.rotationYaw = -((float) MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ)) * (180F / (float) Math.PI);
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
            } else {
                EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();

                if (entitylivingbase.getDistanceSq(this.parentEntity) < 4096.0D) {
                    double d1 = entitylivingbase.posX - this.parentEntity.posX;
                    double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
                    this.parentEntity.rotationYaw = -((float) MathHelper.atan2(d1, d2)) * (180F / (float) Math.PI);
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
                }
            }
        }
    }
}
