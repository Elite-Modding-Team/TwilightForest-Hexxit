package twilightforest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFSounds;
import twilightforest.potions.TFPotions;

public class EntityTFIceSnowball extends EntityTFThrowable {

	private static final int DAMAGE = 8;

	public EntityTFIceSnowball(World world) {
		super(world);
	}

	public EntityTFIceSnowball(World world, EntityLivingBase thrower) {
		super(world, thrower);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		makeTrail();
	}

	@Override
	protected float getGravityVelocity() {
		return 0.006F;
	}

	public void makeTrail() {
		for (int i = 0; i < 2; i++) {
			double dx = posX + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dy = posY + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dz = posZ + 0.5 * (rand.nextDouble() - rand.nextDouble());
			world.spawnParticle(EnumParticleTypes.SNOWBALL, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		super.attackEntityFrom(source, amount);
		die();
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int j = 0; j < 8; ++j) {
				this.world.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!world.isRemote && result.entityHit instanceof EntityLivingBase) {
			int freezeTime = world.getDifficulty() == EnumDifficulty.HARD ? 8 : 4;
			result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), DAMAGE);
			((EntityLivingBase) result.entityHit).addPotionEffect(new PotionEffect(TFPotions.frosty, freezeTime * 20, 0));
		}

		die();
	}

	private void die() {
		if (!this.world.isRemote) {
			this.world.setEntityState(this, (byte) 3);
			this.setDead();
			this.playSound(TFSounds.ICE_HURT, 1.0F, 1.5F / (rand.nextFloat() * 0.2F + 0.4F));
		}
	}
}
