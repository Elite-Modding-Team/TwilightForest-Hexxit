package twilightforest.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.entity.EntityIceArrow;

public class ItemTFIceBow extends ItemTFBowBase {

	public ItemTFIceBow() {
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public EntityArrow customizeArrow(EntityArrow arrow) {
		if (arrow.shootingEntity instanceof EntityLivingBase) {
			return new EntityIceArrow(arrow.world, (EntityLivingBase) arrow.shootingEntity);
		}
		return arrow;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repairWith) {
		return repairWith.getItem() == Item.getItemFromBlock(Blocks.ICE) || super.getIsRepairable(toRepair, repairWith);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(I18n.format(getTranslationKey() + ".tooltip"));
	}
}
