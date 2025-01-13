package twilightforest.item;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFNagaArmor extends ItemTFArmor implements ModelRegisterCallback {
	protected ItemTFNagaArmor(ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn, EnumRarity rarity) {
		super(materialIn, equipmentSlotIn, rarity);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String layer) {
		if (slot == EntityEquipmentSlot.LEGS) {
			return TwilightForestMod.ARMOR_DIR + "naga_scale_2.png";
		} else {
			return TwilightForestMod.ARMOR_DIR + "naga_scale_1.png";
		}
	}
}
