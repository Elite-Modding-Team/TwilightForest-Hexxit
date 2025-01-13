package twilightforest.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFSteeleafSword extends ItemSword implements ModelRegisterCallback {

	public ItemTFSteeleafSword(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}
}
