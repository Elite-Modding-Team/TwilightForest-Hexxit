package twilightforest.biomes;

import net.minecraft.entity.passive.EntityMooshroom;
import twilightforest.TFFeature;

public class TFBiomeDeepMushrooms extends TFBiomeBase {

	public TFBiomeDeepMushrooms(BiomeProperties props) {
		super(props);

		getTFBiomeDecorator().setTreesPerChunk(1);

		getTFBiomeDecorator().setMushroomsPerChunk(12);
		getTFBiomeDecorator().setBigMushroomsPerChunk(8);

		getTFBiomeDecorator().myceliumPerChunk = 3;
		getTFBiomeDecorator().alternateCanopyChance = 0.9F;
		
		// custom creature list.
		spawnableCreatureList.clear();
		spawnableCreatureList.add(new SpawnListEntry(EntityMooshroom.class, 10, 4, 4));
	}

	@Override
	protected TFFeature getContainedFeature() {
		return TFFeature.MUSHROOM_TOWER;
	}
}
