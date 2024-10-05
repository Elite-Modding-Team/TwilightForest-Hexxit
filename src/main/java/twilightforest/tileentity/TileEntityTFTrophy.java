package twilightforest.tileentity;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.enums.BossVariant;

public class TileEntityTFTrophy extends TileEntitySkull {

    public int ticksExisted;
    private boolean shouldAnimate;

    @Override
    public void update() {
        super.update();

        if (this.world.isRemote) {
            BossVariant variant = BossVariant.getVariant(this.getSkullType());

            if (variant == BossVariant.HYDRA || variant == BossVariant.UR_GHAST) {
                if (this.world.isBlockPowered(this.getPos())) {
                    this.shouldAnimate = true;
                    this.ticksExisted++;
                } else this.shouldAnimate = false;
            }
        }
    }

    public boolean shouldAnimate() {
        return shouldAnimate;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}
