package twilightforest.entity.finalcastle;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.potion.PotionHandler;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.potions.TFPotions;

public class EntityTFCastleMob extends EntityMob {
    public EntityTFCastleMob(World world) {
        super(world);
        this.setSize(0.6F, 1.8F);
        ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
    }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAIOpenDoor(this, false));
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false) {
            @Override
            protected double getAttackReachSqr(EntityLivingBase attackTarget) {
                return this.attacker.width * this.attacker.height;
            }
        });
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(70.0D);
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
        setEquipmentBasedOnDifficulty(difficulty);
        setEnchantmentBasedOnDifficulty(difficulty);
        return data;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        boolean flag = super.attackEntityAsMob(entity);

        if (flag && entity instanceof EntityLivingBase) {
            float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();
            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 80 * (int) f, 1)); // 4 seconds
        }

        return flag;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setDropChance(EntityEquipmentSlot.MAINHAND, -100.0F);
        this.setDropChance(EntityEquipmentSlot.OFFHAND, -100.0F);
        this.setDropChance(EntityEquipmentSlot.HEAD, -100.0F);
        this.setDropChance(EntityEquipmentSlot.CHEST, -100.0F);
        this.setDropChance(EntityEquipmentSlot.LEGS, -100.0F);
        this.setDropChance(EntityEquipmentSlot.FEET, -100.0F);

        if (this.rand.nextFloat() < 0.4F) {
            int i = this.rand.nextInt(2);
            float f = 0.05F;

            if (this.rand.nextFloat() < 0.095F) {
                ++i;
            }

            if (this.rand.nextFloat() < 0.095F) {
                ++i;
            }

            if (this.rand.nextFloat() < 0.095F) {
                ++i;
            }

            boolean flag = true;

            for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values()) {
                if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                    ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);

                    if (!flag && this.rand.nextFloat() < f) {
                        break;
                    }

                    flag = false;

                    if (itemstack.isEmpty()) {
                        Item item = getArmorByChance(entityequipmentslot, i);

                        if (item != null) {
                            this.setItemStackToSlot(entityequipmentslot, new ItemStack(item));
                        }
                    }
                }
            }
        }
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
        } else if (entity instanceof EntityWroughtnaut || entity instanceof EntityTFCastleMob) {
            return this.getTeam() == null && entity.getTeam() == null;
        } else {
            return false;
        }
    }

    // Immune to ice effects
    @Override
    public boolean isPotionApplicable(PotionEffect effect) {
        return effect.getPotion() != TFPotions.frosty && effect.getPotion() != PotionHandler.FROZEN && super.isPotionApplicable(effect);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGround, IBlockState state, BlockPos pos) {
    }

    @Override
    public float getEyeHeight() {
        return 1.62F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return MMSounds.ENTITY_WROUGHT_STEP;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MMSounds.ENTITY_WROUGHT_STEP;
    }

    protected SoundEvent getStepSound() {
        return MMSounds.ENTITY_WROUGHT_STEP;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block) {
        this.playSound(this.getStepSound(), 0.1F, 0.65F);
    }
}
