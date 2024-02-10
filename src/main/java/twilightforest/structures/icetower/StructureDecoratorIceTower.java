package twilightforest.structures.icetower;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFDecorator;

public class StructureDecoratorIceTower extends StructureTFDecorator {

	public StructureDecoratorIceTower() {
		this.blockState = TFBlocks.aurora_block.getDefaultState();
		this.accentState = Blocks.ICE.getDefaultState();
		this.fenceState = Blocks.OAK_FENCE.getDefaultState(); // Unused (?)
		this.stairState = Blocks.BIRCH_STAIRS.getDefaultState(); // Unused (?)
		this.pillarState = TFBlocks.aurora_pillar.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y);
		this.platformState = TFBlocks.aurora_slab.getDefaultState();
		this.floorState = Blocks.PACKED_ICE.getDefaultState();
		this.randomBlocks = new StructureTFAuroraBricks();
	}

}
