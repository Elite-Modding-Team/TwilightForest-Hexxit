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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFGiantMiner;
import twilightforest.item.TFItems;
import twilightforest.potions.TFPotions;

public class EntityTFCastleGiant extends EntityTFGiantMiner {
    public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/castle_giant");
    
    public EntityTFCastleGiant(World world) {
        super(world);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(60.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(120.0D);
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
        setEquipmentBasedOnDifficulty(difficulty);
        setEnchantmentBasedOnDifficulty(difficulty);
        return data;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.regal_sword));
        this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TFItems.regal_helmet));
        this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(TFItems.regal_chestplate));
        this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(TFItems.regal_leggings));
        this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(TFItems.regal_boots));
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
    public boolean attackEntityAsMob(Entity entity) {
        boolean flag = super.attackEntityAsMob(entity);

        if (flag && entity instanceof EntityLivingBase) {
            float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();
            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 80 * (int) f, 1)); // 4 seconds
        }

        return flag;
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
    
	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
