package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.util.ParticleHelper;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemTFGlassSword extends ItemSword implements ModelRegisterCallback {

	public ItemTFGlassSword(Item.ToolMaterial toolMaterial) {
		super(toolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		attacker.world.playSound(null, attacker.posX, attacker.posY, attacker.posZ, Blocks.GLASS.getSoundType().getBreakSound(), attacker.getSoundCategory(), 1F, 0.5F);
		ParticleHelper.spawnParticles(target, EnumParticleTypes.BLOCK_CRACK, 20, 0.0, Block.getStateId(Blocks.STAINED_GLASS.getDefaultState()));
		stack.damageItem(stack.getMaxDamage() + 1, attacker);
		return true;
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		super.getSubItems(tab, items);

		if (isInCreativeTab(tab)) {
			ItemStack stack = new ItemStack(this);
			NBTTagCompound tags = new NBTTagCompound();
			tags.setBoolean("Unbreakable", true);
			stack.setTagCompound(tags);
			items.add(stack);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(I18n.format(getTranslationKey() + ".tooltip"));
	}
}