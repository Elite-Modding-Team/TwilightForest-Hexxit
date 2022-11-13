package twilightforest.tileentity.spawner;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import twilightforest.entity.boss.EntityTFFinalKobold;

public class TileEntityTFFinalBossSpawner extends TileEntityTFBossSpawner {

	public TileEntityTFFinalBossSpawner() {
		super(EntityList.getKey(EntityTFFinalKobold.class));
	}

	@Override
	protected void initializeCreature(EntityLiving myCreature) {
		super.initializeCreature(myCreature);
		myCreature.setCustomNameTag(myCreature.getDisplayName().getUnformattedText());
	}
}
