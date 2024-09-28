package twilightforest.compat;

import com.google.common.collect.Lists;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import twilightforest.block.TFBlocks;
import twilightforest.compat.jei.ScepterRepairingRecipeWrapper;
import twilightforest.item.TFItems;

import java.util.List;

@JEIPlugin
public class JEI implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(TFBlocks.uncrafting_table), VanillaRecipeCategoryUid.CRAFTING);

        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
        recipeTransferRegistry.addRecipeTransferHandler(new UncraftingRecipeTransferHandler());

        // ----- CRAFTING RECIPES ----- //
        registry.addRecipes(Lists.newArrayList(
                // Scepters
                new ScepterRepairingRecipeWrapper(TFItems.twilight_scepter, Items.ENDER_PEARL),
                new ScepterRepairingRecipeWrapper(TFItems.lifedrain_scepter, Items.FERMENTED_SPIDER_EYE),
                new ScepterRepairingRecipeWrapper(TFItems.zombie_scepter, Items.ROTTEN_FLESH, getStrengthPotions()),
                new ScepterRepairingRecipeWrapper(TFItems.shield_scepter, Items.GOLDEN_APPLE)
        ), VanillaRecipeCategoryUid.CRAFTING);
    }

    @SuppressWarnings("unused")
	private List<ItemStack> getScepters(Item scepter) {
        ItemStack stack = new ItemStack(scepter);
        ItemStack stackHalf = stack.copy();
        ItemStack stackFull = stack.copy();

        stackHalf.setItemDamage(stack.getItemDamage() / 2);
        stackFull.setItemDamage(stack.getItemDamage());

        return NonNullList.from(stack, stackHalf, stackFull);
    }

    private List<ItemStack> getStrengthPotions() {
        ItemStack potion = new ItemStack(Items.POTIONITEM);
        ItemStack splash = new ItemStack(Items.SPLASH_POTION);
        ItemStack lingering = new ItemStack(Items.LINGERING_POTION);
        return NonNullList.from(
                PotionUtils.addPotionToItemStack(potion.copy(), PotionTypes.STRENGTH), PotionUtils.addPotionToItemStack(potion.copy(), PotionTypes.LONG_STRENGTH), PotionUtils.addPotionToItemStack(potion, PotionTypes.STRONG_STRENGTH),
                PotionUtils.addPotionToItemStack(splash.copy(), PotionTypes.STRENGTH), PotionUtils.addPotionToItemStack(splash.copy(), PotionTypes.LONG_STRENGTH), PotionUtils.addPotionToItemStack(splash, PotionTypes.STRONG_STRENGTH),
                PotionUtils.addPotionToItemStack(lingering.copy(), PotionTypes.STRENGTH), PotionUtils.addPotionToItemStack(lingering.copy(), PotionTypes.LONG_STRENGTH), PotionUtils.addPotionToItemStack(lingering, PotionTypes.STRONG_STRENGTH)
        );
    }
}
