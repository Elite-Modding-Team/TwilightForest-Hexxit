package twilightforest.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFSteeleafShovel extends ItemSpade implements ModelRegisterCallback {

	public ItemTFSteeleafShovel(Item.ToolMaterial toolMaterial) {
		super(toolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}
}
