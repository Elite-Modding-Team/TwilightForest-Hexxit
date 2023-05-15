package twilightforest.tileentity.spawner;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;

import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;

public class TileEntityTFAlphaYetiSpawner extends TileEntityTFBossSpawner {

	public TileEntityTFAlphaYetiSpawner() {
		super(EntityList.getKey(EntityFrostmaw.class));
	}

	@Override
	public boolean anyPlayerInRange() {
		EntityPlayer closestPlayer = world.getClosestPlayer(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, getRange(), false);
		return closestPlayer != null && closestPlayer.posY > pos.getY() - 4;
	}

	@Override
	protected int getRange() {
		return LONG_RANGE;
	}
}
