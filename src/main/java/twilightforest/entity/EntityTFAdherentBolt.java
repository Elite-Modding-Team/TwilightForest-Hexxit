package twilightforest.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;

public class EntityTFAdherentBolt extends EntityTFThrowable implements ITFProjectile {
	public EntityTFAdherentBolt(World world) {
		super(world);
	}

	public EntityTFAdherentBolt(World world, EntityLivingBase thrower) {
		super(world, thrower);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		makeTrail();
	}

	@Override
	protected float getGravityVelocity() {
		return 0.003F;
	}

	private void makeTrail() {
		for (int i = 0; i < 5; i++) {
			double dx = posX + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dy = posY + 0.5 * (rand.nextDouble() - rand.nextDouble());
			double dz = posZ + 0.5 * (rand.nextDouble() - rand.nextDouble());
			TwilightForestMod.proxy.spawnParticle(TFParticleType.ANNIHILATE, dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 3) {
			for (int i = 0; i < 8; ++i) {
				TwilightForestMod.proxy.spawnParticle(TFParticleType.ANNIHILATE, this.posX, this.posY, this.posZ, rand.nextGaussian() * 0.05D, rand.nextDouble() * 0.2D, rand.nextGaussian() * 0.05D);
			}
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@Override
	protected void onImpact(RayTraceResult ray) {
		if (!this.world.isRemote) {
			if (ray.entityHit instanceof EntityLivingBase && (thrower == null || (ray.entityHit != thrower && ray.entityHit != thrower.getRidingEntity()))) {
				if (ray.entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getThrower()), 2)
						&& world.getDifficulty() != EnumDifficulty.PEACEFUL) {
					int effectTime = world.getDifficulty() == EnumDifficulty.HARD ? 20 : 10;
					((EntityLivingBase) ray.entityHit).setFire(effectTime);
					((EntityLivingBase) ray.entityHit).addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, effectTime * 20, 0));
					((EntityLivingBase) ray.entityHit).addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, effectTime * 20, 0));
				}
			}

			this.world.setEntityState(this, (byte) 3);
			this.setDead();
			this.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F / (rand.nextFloat() * 0.2F + 0.4F));
		}
	}

}
