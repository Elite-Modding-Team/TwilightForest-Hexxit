package twilightforest.entity.boss;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;

public class EntityTFFinalKobold extends EntityMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/final_kobold");

	public EntityTFFinalKobold(World world) {
		super(world);
		setSize(5.6F, 7.7F);
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0, false) {
			@Override
			protected double getAttackReachSqr(EntityLivingBase attackTarget) {
				return this.attacker.width * this.attacker.width + attackTarget.width;
			}
		});
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 16.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(30);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(20);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(32);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1024);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.14);
	}

	@Override
	protected void onDeathUpdate() {
		super.onDeathUpdate();

		for (int i = 0; i < 20; ++i) {
			float f = (rand.nextFloat() - 0.5F) * 8.0F;
			float f1 = (rand.nextFloat() - 0.5F) * 4.0F;
			float f2 = (rand.nextFloat() - 0.5F) * 8.0F;
			world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + (double)f, posY + 2.0 + (double)f1, posZ + (double)f2, 0.0, 0.0, 0.0);
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return TFSounds.FINAL_KOBOLD_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return TFSounds.FINAL_KOBOLD_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return TFSounds.FINAL_KOBOLD_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_IRONGOLEM_STEP, 1.0F, 1.0F);
	}

	@Override
	public ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
