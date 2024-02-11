package twilightforest.entity.boss;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.entity.EntityTFThrowable;

public class EntityTFLichBomb extends EntityTFThrowable {

	public EntityTFLichBomb(World world) {
		super(world);
	}

	public EntityTFLichBomb(World world, EntityLivingBase thrower) {
		super(world, thrower);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		makeTrail();
	}

	private void makeTrail() {
		for (int i = 0; i < 1; i++) {
			double sx = 0.5 * (rand.nextDouble() - rand.nextDouble()) + this.motionX;
			double sy = 0.5 * (rand.nextDouble() - rand.nextDouble()) + this.motionY;
			double sz = 0.5 * (rand.nextDouble() - rand.nextDouble()) + this.motionZ;


			double dx = posX + sx;
			double dy = posY + sy;
			double dz = posZ + sz;

			world.spawnParticle(EnumParticleTypes.FLAME, dx, dy, dz, sx * -0.25, sy * -0.25, sz * -0.25);
		}
	}

	@Override
	public boolean isBurning() {
		return true;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public float getCollisionBorderSize() {
		return 1.0F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		super.attackEntityFrom(source, amount);
		
		if (!this.world.isRemote && source.getTrueSource() != null) {
			Vec3d vec3d = source.getTrueSource().getLookVec();
			// reflect faster and more accurately
			this.shoot(vec3d.x, vec3d.y, vec3d.z, 1.5F, 0.1F);  // reflect faster and more accurately

			if (source.getImmediateSource() instanceof EntityLivingBase)
				this.thrower = (EntityLivingBase) source.getImmediateSource();

			return true;
		}
		
		return false;
	}

	private void explode() {
		if (!this.world.isRemote) {
			this.world.newExplosion(this, this.posX, this.posY, this.posZ, 2F, false, false);
			this.setDead();
		}
	}

	@Override
	protected float getGravityVelocity() {
		return 0.001F;
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (result.entityHit instanceof EntityTFLichBolt
				|| result.entityHit instanceof EntityTFLichBomb) {
			return;
		}

		explode();
	}
}
