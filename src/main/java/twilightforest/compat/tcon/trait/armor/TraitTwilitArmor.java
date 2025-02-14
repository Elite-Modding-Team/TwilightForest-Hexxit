package twilightforest.compat.tcon.trait.armor;

import com.google.common.collect.ImmutableList;

import c4.conarm.lib.armor.ArmorModifications;
import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.Util;
import twilightforest.world.TFWorld;

import java.util.List;

public class TraitTwilitArmor extends AbstractArmorTrait {
    public TraitTwilitArmor() {
        super("twilit", TextFormatting.GOLD);
    }

    @Override
    public ArmorModifications getModifications(EntityPlayer player, ArmorModifications mods, ItemStack armor, DamageSource source, double damage, int slot) {
        if (source.getTrueSource() instanceof EntityLivingBase) {

            // +20% resistance on Twilight Forest. +10% resistance outside it.
            if (TFWorld.isTwilightForest(player.world)) {
                mods.addEffectiveness(0.2F);
            } else {
                mods.addEffectiveness(0.1F);
            }
        }

        return super.getModifications(player, mods, armor, source, damage, slot);
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        String inside = String.format(LOC_Extra + ".inside_armor", getModifierIdentifier());
        String outside = String.format(LOC_Extra + ".outside_armor", getModifierIdentifier());

        return ImmutableList.of(
                Util.translateFormatted(inside, Util.dfPercent.format(0.2F)),
                Util.translateFormatted(outside, Util.dfPercent.format(0.1F))
        );
    }
}
