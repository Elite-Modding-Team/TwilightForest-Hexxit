package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GLContext;
import twilightforest.TwilightForestMod;
import twilightforest.biomes.TFBiomes;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Side.CLIENT)
public class FogHandler {
    private static final float[] fogColors = new float[3];
    private static float fogColor = 0.0F;
    private static float fogDensity = 1.0F;
    private static float red;
    private static float green;
    private static float blue;

    @SubscribeEvent
    public static void fogColors(EntityViewRenderEvent.FogColors event) {
        // Dark Forest
        boolean isDark = isDark();
        // Spooky Forest, Dark Forest Center
        boolean isSpooky = isSpooky();

        // Purple
        if (isSpooky) {
            red = 106.0F;
            green = 60.0F;
            blue = 153.0F;
        }
        // Dark Green
        else if (isDark) {
            red = 50.0F;
            green = 81.0F;
            blue = 51.0F;
        }
        // Default (Green)
        else {
            red = event.getRed();
            green = event.getGreen();
            blue = event.getBlue();
        }

        if (isSpooky || isDark || fogColor > 0.0F) {
            final float[] realColors = {event.getRed(), event.getGreen(), event.getBlue()};
            final float[] lerpColors = {red / 255.0F, green / 255.0F, blue / 255.0F};

            for (int i = 0; i < 3; i++) {
                final float real = realColors[i];
                final float lerp = lerpColors[i];
                final boolean inverse = real > lerp;
                fogColors[i] = real == lerp ? lerp : (float) MathHelper.clampedLerp(inverse ? lerp : real, inverse ? real : lerp, fogColor);
            }

            fogColor = MathHelper.clamp(fogColor, 0F, 1F);
            event.setRed(fogColors[0]);
            event.setGreen(fogColors[1]);
            event.setBlue(fogColors[2]);
        }
    }

    @SubscribeEvent
    public static void fog(EntityViewRenderEvent.RenderFogEvent event) {
        boolean isSpooky = isSpooky();
        boolean isDark = isDark();

        // Spooky Forest, Dark Forest, Dark Forest Center
        if (isSpooky || isDark || fogDensity < 1.0F) {
            float f = 48.0F;
            f = f >= event.getFarPlaneDistance() ? event.getFarPlaneDistance() : (float) MathHelper.clampedLerp(f, event.getFarPlaneDistance(), fogDensity);
            float shift = (float) (0.001F * event.getRenderPartialTicks());
            if (isSpooky || isDark)
                fogDensity -= shift;
            else
                fogDensity += shift;
            fogDensity = MathHelper.clamp(fogDensity, 0.0F, 1.0F);

            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);

            if (event.getFogMode() == -1) {
                GlStateManager.setFogStart(0.0F);
                GlStateManager.setFogEnd(f);
            } else {
                GlStateManager.setFogStart(f * 0.75F);
                GlStateManager.setFogEnd(f);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GlStateManager.glFogi(0x855a, 0x855b);
            }
        }
    }

    private static boolean isDark() {
        return Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world.getBiome(Minecraft.getMinecraft().player.getPosition()) == TFBiomes.darkForest;
    }

    private static boolean isSpooky() {
        return Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world.getBiome(Minecraft.getMinecraft().player.getPosition()) == TFBiomes.spookyForest ||
                Minecraft.getMinecraft().world.getBiome(Minecraft.getMinecraft().player.getPosition()) == TFBiomes.darkForestCenter;
    }
}
