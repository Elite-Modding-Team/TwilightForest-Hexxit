package twilightforest.item.scepter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import twilightforest.entity.EntityTFTwilightWandBolt;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemTFScepterTwilight extends ItemTFScepter {

    public ItemTFScepterTwilight(EnumRarity rarity, int damage) {
        super(rarity, damage);
    }

    @Override
    public Item getRepairItem(ItemStack stack) {
        return Items.ENDER_PEARL;
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

		if (stack.getItemDamage() == stack.getMaxDamage()) {
			return ActionResult.newResult(EnumActionResult.FAIL, player.getHeldItem(hand));
		} else {
			player.playSound(SoundEvents.ENTITY_ENDEREYE_DEATH, 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);

			if (!world.isRemote) {
				world.spawnEntity(new EntityTFTwilightWandBolt(world, player));
				stack.damageItem(1, player);
			}

			return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
    }
}
