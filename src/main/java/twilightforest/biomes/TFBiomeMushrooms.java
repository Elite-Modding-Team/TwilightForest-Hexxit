package twilightforest.biomes;

import net.minecraft.entity.passive.EntityMooshroom;

public class TFBiomeMushrooms extends TFBiomeBase {

	public TFBiomeMushrooms(BiomeProperties props) {
		super(props);

		getTFBiomeDecorator().setTreesPerChunk(8);
		getTFBiomeDecorator().setMushroomsPerChunk(8);
		getTFBiomeDecorator().setBigMushroomsPerChunk(2);
		getTFBiomeDecorator().alternateCanopyChance = 0.2F;
		
		// custom creature list.
		spawnableCreatureList.add(new SpawnListEntry(EntityMooshroom.class, 10, 1, 2));
	}
}
