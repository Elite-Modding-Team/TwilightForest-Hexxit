package twilightforest.compat.tcon.trait.armor;

import c4.conarm.common.armor.utils.ArmorHelper;
import c4.conarm.lib.traits.AbstractArmorTrait;
import c4.conarm.lib.utils.ConstructUtils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.potion.TinkerPotion;

public class TraitPrecipitateArmor extends AbstractArmorTrait {
    public static TinkerPotion precipitatePotion = new precipitatePotion();

    public TraitPrecipitateArmor() {
        super("precipitate", TextFormatting.DARK_GREEN);
    }

    @Override
    public void onAbilityTick(int level, World world, EntityPlayer player) {
        if (player.getHealth() <= player.getMaxHealth() * 0.5D) {
            precipitatePotion.apply(player, 25, (int) ArmorHelper.getArmorAbilityLevel(player, this.getIdentifier()));
        }
    }

    private static class precipitatePotion extends TinkerPotion {
        public static final String UUID = "7c1fa654-5187-4b20-b65f-80a119717e81";

        public precipitatePotion() {
            super(ConstructUtils.getResource("precipitatePotion"), false, false);
            this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, UUID, 0.15D, 2);
        }

        public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
            return modifier.getAmount() * (double) (amplifier);
        }
    }

}
