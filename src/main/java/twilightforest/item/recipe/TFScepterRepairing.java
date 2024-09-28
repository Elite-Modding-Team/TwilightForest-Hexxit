package twilightforest.item.recipe;

import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import twilightforest.item.scepter.ItemTFScepter;
import twilightforest.item.scepter.ItemTFScepterZombie;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class TFScepterRepairing extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack wand = ItemStack.EMPTY;
        int wandSlot = -1;

        boolean fuel = false;
        boolean extra = false;

        // Find the wand
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemTFScepter) {
                if (stack.getItemDamage() == 0) return false;
                wand = stack;
                wandSlot = i;
                break;
            }
        }

        if (wandSlot == -1) return false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (i == wandSlot) continue;
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();
                if (item instanceof ItemTFScepter) return false; // if it's a different wand, return false
                boolean anything = false; // If the stack in slot is fuel or extra. If not, return false.
                if (!fuel) { // Handling for duplicate fuels
                    fuel = ((ItemTFScepter) wand.getItem()).getRepairItem(wand) == stack.getItem();
                    anything = fuel;
                }
                if (!extra) { // Handling for duplicate potions
                    PotionType type = PotionUtils.getPotionFromItem(stack);
                    extra = type == PotionTypes.STRENGTH || type == PotionTypes.STRONG_STRENGTH || type == PotionTypes.LONG_STRENGTH;
                    anything |= extra;
                }
                if (!anything) return false;
            }
        }

        return fuel && extra(extra, wand);
    }

    private boolean extra(boolean extra, ItemStack wand) {
        return !(wand.getItem() instanceof ItemTFScepterZombie) || extra;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemTFScepter) {
                if (stack.getMaxDamage() == stack.getItemDamage()) return ItemStack.EMPTY;
                ItemStack out = stack.copy();
                out.setItemDamage(0);
                return out;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width == 2 || height == 2;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
}
