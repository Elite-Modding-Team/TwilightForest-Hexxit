package twilightforest.compat.tcon.trait;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.traits.AbstractProjectileTrait;
import twilightforest.world.TFWorld;

import javax.annotation.Nullable;
import java.util.List;

public class TraitTwilit extends AbstractProjectileTrait {
    public TraitTwilit() {
        super("twilit", TextFormatting.GOLD);
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        if (TFWorld.isTwilightForest(event.getEntity().world)) {
            event.setNewSpeed(event.getNewSpeed() + 2.0F);
        } else {
            event.setNewSpeed(event.getNewSpeed() + 1.0F);
        }
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        if (TFWorld.isTwilightForest(target.world))
            return super.damage(tool, player, target, damage, newDamage + 2.0F, isCritical);
        else
            return super.damage(tool, player, target, damage, newDamage + 1.0F, isCritical);
    }

    @Override
    public void onLaunch(EntityProjectileBase projectileBase, World world, @Nullable EntityLivingBase shooter) {
        if (TFWorld.isTwilightForest(projectileBase.world)) {
            projectileBase.motionX += (projectileBase.motionX * 2.0F * 0.1f);
            projectileBase.motionY += (projectileBase.motionY * 2.0F * 0.1f);
            projectileBase.motionZ += (projectileBase.motionZ * 2.0F * 0.1f);
        } else {

            projectileBase.motionX += (projectileBase.motionX * 1.0F * 0.1f);
            projectileBase.motionY += (projectileBase.motionY * 1.0F * 0.1f);
            projectileBase.motionZ += (projectileBase.motionZ * 1.0F * 0.1f);
        }
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        String inside = String.format(LOC_Extra + ".inside", getModifierIdentifier());
        String outside = String.format(LOC_Extra + ".outside", getModifierIdentifier());

        return ImmutableList.of(
                Util.translateFormatted(inside, Util.df.format(1.0F)),
                Util.translateFormatted(outside, Util.df.format(2.0F))
        );
    }
}
