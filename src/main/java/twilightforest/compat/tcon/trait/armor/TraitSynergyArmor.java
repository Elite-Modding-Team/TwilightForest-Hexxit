package twilightforest.compat.tcon.trait.armor;

import c4.conarm.common.armor.utils.ArmorHelper;
import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.utils.ToolHelper;
import twilightforest.enums.CompressedVariant;
import twilightforest.item.TFItems;
import twilightforest.util.TFItemStackUtils;

public class TraitSynergyArmor extends AbstractArmorTrait {
    public TraitSynergyArmor() {
        super("synergy", TextFormatting.GREEN);
    }

    private static final float REPAIR_DAMPENER = 1.0F / 256.0F;

    @Override
    public void onArmorTick(ItemStack armor, World world, EntityPlayer player) {
        if (!world.isRemote && player instanceof EntityPlayer && ToolHelper.getCurrentDurability(armor) >= 1) {
            int healPower = 0;

            NonNullList<ItemStack> playerInv = ((EntityPlayer) player).inventory.mainInventory;

            for (int i = 0; i < 9; i++) {
                ItemStack stack = playerInv.get(i);
                if (stack.getItem() == TFItems.steeleaf_ingot) {
                    healPower += 1;
                } else if (stack.getItem() == TFItems.block_storage && stack.getMetadata() == CompressedVariant.STEELLEAF.ordinal()) {
                    healPower += 9;
                } else if (TFItemStackUtils.hasToolMaterial(stack, TFItems.TOOL_STEELEAF)) {
                    healPower += 2;
                }
            }

            ArmorHelper.healArmor(armor, averageInt(healPower * REPAIR_DAMPENER), player, EntityLiving.getSlotForItemStack(armor).getIndex());
        }
    }

    private static int averageInt(float value) {
        double floor = Math.floor(value);
        double rem = value - floor;
        return (int) floor + (Math.random() < rem ? 1 : 0);
    }
}
