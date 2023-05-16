package twilightforest;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Side.CLIENT)
public class TFEventListenerClient {

    public static final ResourceLocation BEATEN_TF = new ResourceLocation(TwilightForestMod.ID, "progress_finalboss");

    @SubscribeEvent
    public static void onFinalBossDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof EntityWroughtnaut) {
            EntityWroughtnaut finalBoss = (EntityWroughtnaut) event.getEntityLiving();
            World world = finalBoss.getEntityWorld();
            if (world.provider.getDimension() != TFConfig.dimension.dimensionID) return;
            world.playSound(null, finalBoss.getPosition(), TFSounds.FINALBOSS_DEATH, SoundCategory.HOSTILE, 1.0F, 1.0F);
        }
    }

    public static Advancement getSideAdvancement(ResourceLocation id) {
        return Minecraft.getMinecraft().player.connection.getAdvancementManager().getAdvancementList().getAdvancement(id);
    }

    public static boolean hasAdvancement(ResourceLocation id) {
        AdvancementProgress progress = Minecraft.getMinecraft().player.connection.getAdvancementManager().advancementToProgress.get(getSideAdvancement(id));
        return progress != null && progress.isDone();
    }
}
