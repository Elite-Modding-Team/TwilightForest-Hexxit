package twilightforest.block;

import java.util.Random;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

public class BlockTFAuroralizedGlass extends BlockGlass implements ModelRegisterCallback {

    public BlockTFAuroralizedGlass() {
        super(Material.ROCK, false);
        
        this.setCreativeTab(TFItems.creativeTab);
        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.GLASS);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
