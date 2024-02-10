package twilightforest.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.entity.EntitySeekerArrow;

public class ItemTFSeekerBow extends ItemTFBowBase {

	public ItemTFSeekerBow() {
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public EntityArrow customizeArrow(EntityArrow arrow) {
		if (arrow.shootingEntity instanceof EntityLivingBase) {
			return new EntitySeekerArrow(arrow.world, (EntityLivingBase) arrow.shootingEntity);
		}
		return arrow;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(I18n.format(getTranslationKey() + ".tooltip"));
	}
}
