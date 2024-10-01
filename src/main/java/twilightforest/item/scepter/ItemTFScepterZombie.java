package twilightforest.item.scepter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import twilightforest.entity.EntityTFLoyalZombie;
import twilightforest.util.EntityUtil;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemTFScepterZombie extends ItemTFScepter {

    public ItemTFScepterZombie(EnumRarity rarity, int damage) {
        super(rarity, damage);
    }

    @Override
    public Item getRepairItem(ItemStack stack) {
        return Items.ROTTEN_FLESH;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {

        ItemStack stack = player.getHeldItem(hand);

        if (stack.getItemDamage() == stack.getMaxDamage()) {
        	return ActionResult.newResult(EnumActionResult.FAIL, stack);
        } else {
        	player.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
        	player.swingArm(hand);
        }

        if (!world.isRemote) {
            // what block is the player pointing at?
            RayTraceResult ray = EntityUtil.rayTrace(player, 20.0);

            if (ray != null && ray.hitVec != null) {
                EntityTFLoyalZombie zombie = new EntityTFLoyalZombie(world);
                zombie.setPositionAndRotation(ray.hitVec.x, ray.hitVec.y, ray.hitVec.z, 1.0F, 1.0F);
                zombie.setTamed(true);
                zombie.setOwnerId(player.getUniqueID());
                world.spawnEntity(zombie);

                stack.damageItem(1, player);
            }
            
            if (!player.isCreative())
                player.getCooldownTracker().setCooldown(this, 200);
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }
}
