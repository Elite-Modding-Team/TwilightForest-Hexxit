package twilightforest.structures.finalcastle;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.*;
import twilightforest.enums.BossVariant;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleBossGazebo extends StructureTFComponentOld {

    @SuppressWarnings("unused")
    public ComponentTFFinalCastleBossGazebo() {
    }

    public ComponentTFFinalCastleBossGazebo(TFFeature feature, Random rand, int i, StructureTFComponentOld keep) {
        super(feature, i);
        this.spawnListIndex = -1; // no monsters

        this.setCoordBaseMode(keep.getCoordBaseMode());
        this.boundingBox = new StructureBoundingBox(keep.getBoundingBox().minX + 14, keep.getBoundingBox().maxY + 2, keep.getBoundingBox().minZ + 14, keep.getBoundingBox().maxX - 14, keep.getBoundingBox().maxY + 13, keep.getBoundingBox().maxZ - 14);
    }

    @Override
    public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
        this.deco = new StructureTFDecoratorCastle();
        this.deco.blockState = TFBlocks.castle_rune_brick.getDefaultState().withProperty(BlockTFCastleMagic.COLOR, EnumDyeColor.BLUE);
        this.deco.fenceState = TFBlocks.force_field.getDefaultState().withProperty(BlockTFForceField.COLOR, EnumDyeColor.PURPLE);
    }

    @Override
    public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
        int width = 20;
        int height = 5;

        // walls
        for (Rotation rotation : RotationUtil.ROTATIONS) {
            this.fillBlocksRotated(world, sbb, 0, 0, 0, 0, height, width, deco.fenceState, rotation);
        }

        // corner pillars
        this.fillWithBlocks(world, sbb, 0, 0, 0, 0, height, 0, deco.blockState, deco.blockState, false);
        this.fillWithBlocks(world, sbb, width, 0, 0, width, height, 0, deco.blockState, deco.blockState, false);
        this.fillWithBlocks(world, sbb, 0, 0, width, 0, height, width, deco.blockState, deco.blockState, false);
        this.fillWithBlocks(world, sbb, width, 0, width, width, height, width, deco.blockState, deco.blockState, false);

        // door
        final IBlockState castleDoor = TFBlocks.castle_door.getDefaultState();
        this.fillWithBlocks(world, sbb, (width / 2) - 1, 0, 0, (width / 2) + 1, 3, 0, castleDoor.withProperty(BlockTFCastleDoor.LOCK_INDEX, 2), AIR, false);

        // roof
        this.fillWithBlocks(world, sbb, 0, height + 1, 0, width, height + 1, width, deco.blockState, deco.blockState, false);
        this.fillWithBlocks(world, sbb, (width / 2) - 1, height + 1, (width / 2) - 1, (width / 2) + 1, height + 1, (width / 2) + 1, deco.fenceState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X), deco.fenceState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X), false);

        //this.placeSignAtCurrentPosition(world, 10, 0, 10, sbb, "Final Boss Here", "You win!", "discord.gg/6v3z26B");

        //setInvisibleTextEntity(world, 10, 0, 10, sbb, "Final Boss Here", true, 2.3f);
        //setInvisibleTextEntity(world, 10, 0, 10, sbb, "You win!", true, 2.0f);
        //setInvisibleTextEntity(world, 10, 0, 10, sbb, "You can join the Twilight Forest Discord server to follow",true, 1.0f);
        //setInvisibleTextEntity(world, 10, 0, 10, sbb, "the latest updates on this castle and other content at:",true, 0.7f);
        //setInvisibleTextEntity(world, 10, 0, 10, sbb, "discord.experiment115.com", true, 0.4f);

        setBlockState(world, TFBlocks.boss_spawner.getDefaultState().withProperty(BlockTFBossSpawner.VARIANT, BossVariant.FINAL_BOSS), 10, 1, 10, sbb);

        return true;
    }
}
