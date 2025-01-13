package twilightforest.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFIronwoodPick extends ItemPickaxe implements ModelRegisterCallback {

	protected ItemTFIronwoodPick(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}
}
