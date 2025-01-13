package twilightforest.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import twilightforest.block.TFBlocks;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nonnull;

public class ItemTFMazebreakerPick extends ItemPickaxe implements ModelRegisterCallback {
	protected ItemTFMazebreakerPick(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public float getDestroySpeed(@Nonnull ItemStack stack, IBlockState state) {
		float destroySpeed = super.getDestroySpeed(stack, state);
		return state.getBlock() == TFBlocks.maze_stone ? destroySpeed * 16F : destroySpeed;
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}
}
