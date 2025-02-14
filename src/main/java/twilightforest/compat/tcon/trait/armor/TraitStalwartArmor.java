package twilightforest.compat.tcon.trait.armor;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class TraitStalwartArmor extends AbstractArmorTrait {
    public TraitStalwartArmor() {
        super("stalwart", TextFormatting.GRAY);
    }

    @Override
    public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
        if (random.nextInt(5) == 0)
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 200, 1));

        return super.onHurt(armor, player, source, damage, newDamage, evt);
    }
}
