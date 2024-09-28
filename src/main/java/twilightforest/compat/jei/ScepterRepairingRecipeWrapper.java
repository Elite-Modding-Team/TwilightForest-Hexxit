package twilightforest.compat.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScepterRepairingRecipeWrapper implements ICraftingRecipeWrapper {

    private final List<List<ItemStack>> inputs;
    private final ItemStack output;

    public ScepterRepairingRecipeWrapper(Item scepter, Item repairItem, List<ItemStack> extra) {
        List<ItemStack> scepterList, repairItemList, extraList;

        {
            ItemStack stack = new ItemStack(scepter);
            ItemStack stackHalf = stack.copy();
            ItemStack stackFull = stack.copy();

            stackHalf.setItemDamage(stack.getItemDamage() / 2);
            stackFull.setItemDamage(stack.getItemDamage());

            scepterList = NonNullList.from(stack, stackHalf, stackFull);
            this.output = stack;
        }

        {
            repairItemList = Collections.singletonList(new ItemStack(repairItem));
            extraList = extra;
        }

        this.inputs = new ArrayList<>();
        this.inputs.add(scepterList);
        this.inputs.add(repairItemList);
        if (extraList != null) this.inputs.add(extraList);
    }

    public ScepterRepairingRecipeWrapper(Item scepter, Item repairItem) {
        this(scepter, repairItem, null);
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, this.inputs);
        ingredients.setOutput(VanillaTypes.ITEM, this.output);
    }
}
