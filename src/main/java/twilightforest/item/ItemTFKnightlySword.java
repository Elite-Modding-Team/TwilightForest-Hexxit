package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFEventListener;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class ItemTFKnightlySword extends ItemSword implements ModelRegisterCallback {

	private static final int BONUS_DAMAGE = 2;

	public ItemTFKnightlySword(Item.ToolMaterial material) {
		super(material);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@SubscribeEvent
	public static void onDamage(LivingHurtEvent evt) {
		EntityLivingBase target = evt.getEntityLiving();

		if (!target.world.isRemote && evt.getSource().getImmediateSource() instanceof EntityLivingBase) {
			ItemStack weapon = ((EntityLivingBase) evt.getSource().getImmediateSource()).getHeldItemMainhand();
			
			if (!weapon.isEmpty()) {
				if (target.getTotalArmorValue() > 0 && (weapon.getItem() == TFItems.knightmetal_pickaxe || weapon.getItem() == TFItems.knightmetal_sword)) {
					if(TFEventListener.armorPercentage(target) > 0) {
						int moreBonus = (int) (BONUS_DAMAGE * TFEventListener.armorPercentage(target));
						evt.setAmount(evt.getAmount() + moreBonus);
					} else {
						evt.setAmount(evt.getAmount() + BONUS_DAMAGE);
					}
					// enchantment attack sparkles
					((WorldServer) target.world).getEntityTracker().sendToTrackingAndSelf(target, new SPacketAnimation(target, 5));
				} else if(target.getTotalArmorValue() == 0 && weapon.getItem() == TFItems.knightmetal_axe) {
					evt.setAmount(evt.getAmount() + BONUS_DAMAGE);
					// enchantment attack sparkles
					((WorldServer) target.world).getEntityTracker().sendToTrackingAndSelf(target, new SPacketAnimation(target, 5));
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flags) {
		super.addInformation(stack, world, list, flags);
		list.add(I18n.format(getTranslationKey() + ".tooltip"));
	}
}
