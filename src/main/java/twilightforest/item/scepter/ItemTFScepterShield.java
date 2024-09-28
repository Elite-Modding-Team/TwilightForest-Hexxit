package twilightforest.item.scepter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;

import javax.annotation.Nonnull;

public class ItemTFScepterShield extends ItemTFScepter {

    public ItemTFScepterShield(EnumRarity rarity, int damage) {
        super(rarity, damage);
    }

    // I don't care if the golden apple is a notch apple
    @Override
    public Item getRepairItem(ItemStack stack) {
        return Items.GOLDEN_APPLE;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (stack.getItemDamage() == stack.getMaxDamage()) {
            return ActionResult.newResult(EnumActionResult.FAIL, stack);
        }

        if (!world.isRemote && player.hasCapability(CapabilityList.SHIELDS, null)) {
            IShieldCapability cap = player.getCapability(CapabilityList.SHIELDS, null);
            if(cap != null)
                cap.replenishShields();
            stack.damageItem(1, player);
        }

        if (!player.isCreative())
            player.getCooldownTracker().setCooldown(this, 1200);

        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public float getXpRepairRatio(@Nonnull ItemStack stack) {
        return 0.1f;
    }
}
