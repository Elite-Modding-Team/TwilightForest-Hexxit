package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.util.ParticleHelper;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

@SuppressWarnings("deprecation")
public class ItemTFGlassSword extends ItemSword implements ModelRegisterCallback {
    private float attackSpeed;

    public ItemTFGlassSword(Item.ToolMaterial toolMaterial) {
        super(toolMaterial);
        this.setCreativeTab(TFItems.creativeTab);
        this.attackSpeed = 10.0F;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.attackEntityFrom(DamageSource.causeMobDamage(attacker).setDamageBypassesArmor(), (float) (this.getAttackDamage()));

        if (!((EntityPlayer) attacker).capabilities.isCreativeMode && attacker instanceof EntityPlayer) {
            attacker.world.playSound(null, attacker.posX, attacker.posY, attacker.posZ, Blocks.GLASS.getSoundType().getBreakSound(), attacker.getSoundCategory(), 1F, 0.5F);
            ParticleHelper.spawnParticles(target, EnumParticleTypes.BLOCK_CRACK, 20, 0.0, Block.getStateId(Blocks.STAINED_GLASS.getDefaultState()));
            stack.damageItem(stack.getMaxDamage() + 1, attacker);
        }

        return true;
    }

    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {
        super.addInformation(stack, world, tooltip, flags);
        tooltip.add(I18n.format(getTranslationKey() + ".tooltip"));
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Damage modifier", (double) this.getAttackDamage() + 3.0D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Speed modifier", (double) this.attackSpeed - 4.0D, 0));
        }

        return multimap;
    }
}