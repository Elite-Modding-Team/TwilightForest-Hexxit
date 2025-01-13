package twilightforest.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFSteeleafPick extends ItemPickaxe implements ModelRegisterCallback {

	protected ItemTFSteeleafPick(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}
}
