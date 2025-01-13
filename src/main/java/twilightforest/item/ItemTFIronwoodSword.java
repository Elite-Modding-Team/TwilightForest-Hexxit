package twilightforest.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFIronwoodSword extends ItemSword implements ModelRegisterCallback {

	public ItemTFIronwoodSword(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}
}
