package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityTFSpikeBlock extends Entity {
	private EntityTFBlockGoblin goblin;
	private boolean isCollideBlock;

	public EntityTFSpikeBlock(World world) {
		super(world);
		setSize(0.75F, 0.75F);
	}

	public EntityTFSpikeBlock(EntityTFBlockGoblin goblin) {
		this(goblin.getWorld());
		this.goblin = goblin;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		if (this.goblin != null && !this.goblin.isEntityAlive()) {
			this.doFall();
		}

		this.ticksExisted++;

		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;

		for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) {
		}
		for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) {
		}
		for (; rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) {
		}
		for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) {
		}
	}
	
	public void doFall() {
		if (this.onGround && !this.isCollideBlock) {
			this.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.65F, 0.75F);
			this.isCollideBlock = true;
		} else {
			this.motionY += -0.04F;
			
			this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		}
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean isEntityEqual(Entity entity) {
		return this == entity || this.goblin == entity;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
	}
}
