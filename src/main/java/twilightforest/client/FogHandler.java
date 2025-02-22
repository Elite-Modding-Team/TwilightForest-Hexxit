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
    private static final float COLOR_BLEND_SPEED = 0.005F;
    private static final float DENSITY_BLEND_SPEED = 0.001F;
    private static float fogColor = 0.0F;
    private static float fogDensity = 1.0F;

    @SubscribeEvent
    public static void fogColors(EntityViewRenderEvent.FogColors event) {
        // Biome checks
        boolean isDark = isDark();
        boolean isSpooky = isSpooky();
        boolean isFiery = isFiery();
        boolean isFrosty = isFrosty();

        // Initialize target fog color variables
        float targetRed, targetGreen, targetBlue;

        // Set target fog colors based on biome conditions
        if (isFiery) { // Dark Red
            targetRed = 56.0F / 255.0F;
            targetGreen = 10.0F / 255.0F;
            targetBlue = 0.0F / 255.0F;
        } else if (isFrosty) { // Blue
            targetRed = 60.0F / 255.0F;
            targetGreen = 78.0F / 255.0F;
            targetBlue = 196.0F / 255.0F;
        } else if (isSpooky) { // Purple
            targetRed = 106.0F / 255.0F;
            targetGreen = 60.0F / 255.0F;
            targetBlue = 153.0F / 255.0F;
        } else if (isDark) { // Dark Green
            targetRed = 50.0F / 255.0F;
            targetGreen = 81.0F / 255.0F;
            targetBlue = 51.0F / 255.0F;
        } else { // Default (Green)
            targetRed = event.getRed();
            targetGreen = event.getGreen();
            targetBlue = event.getBlue();
        }

        // Smooth transition of fogColor blend ratio
        fogColor = MathHelper.clamp(fogColor + ((isFiery || isFrosty || isSpooky || isDark) ? COLOR_BLEND_SPEED : -COLOR_BLEND_SPEED), 0.0F, 1.0F);

        // Linear interpolation of fog colors
        fogColors[0] = (float) MathHelper.clampedLerp(event.getRed(), targetRed, fogColor);
        fogColors[1] = (float) MathHelper.clampedLerp(event.getGreen(), targetGreen, fogColor);
        fogColors[2] = (float) MathHelper.clampedLerp(event.getBlue(), targetBlue, fogColor);

        // Apply interpolated fog colors
        event.setRed(fogColors[0]);
        event.setGreen(fogColors[1]);
        event.setBlue(fogColors[2]);
    }

    @SubscribeEvent
    public static void fog(EntityViewRenderEvent.RenderFogEvent event) {
        boolean isSpooky = isSpooky();
        boolean isDark = isDark();

        // Handle fog density and distance based on biome conditions
        if (isSpooky || isDark || fogDensity < 1.0F) {
            float targetDistance = 48.0F;
            targetDistance = Math.min(targetDistance, event.getFarPlaneDistance());
            fogDensity = MathHelper.clamp(fogDensity + ((isSpooky || isDark) ? -DENSITY_BLEND_SPEED : DENSITY_BLEND_SPEED), 0.0F, 1.0F);

            float fogEnd = (float) MathHelper.clampedLerp(targetDistance, event.getFarPlaneDistance(), fogDensity);

            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);

            if (event.getFogMode() == -1) {
                GlStateManager.setFogStart(0.0F);
                GlStateManager.setFogEnd(fogEnd);
            } else {
                GlStateManager.setFogStart(fogEnd * 0.75F);
                GlStateManager.setFogEnd(fogEnd);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GlStateManager.glFogi(0x855a, 0x855b); // GL_EYE_PLANE
            }
        }
    }

    private static boolean isDark() {
        return Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world.getBiome(Minecraft.getMinecraft().player.getPosition()) == TFBiomes.darkForest;
    }

    private static boolean isSpooky() {
        return Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null && (Minecraft.getMinecraft().world.getBiome(Minecraft.getMinecraft().player.getPosition()) == TFBiomes.spookyForest || Minecraft.getMinecraft().world.getBiome(Minecraft.getMinecraft().player.getPosition()) == TFBiomes.darkForestCenter);
    }

    private static boolean isFiery() {
        return Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world.getBiome(Minecraft.getMinecraft().player.getPosition()) == TFBiomes.fireSwamp;
    }

    private static boolean isFrosty() {
        return Minecraft.getMinecraft().world != null && Minecraft.getMinecraft().player != null && (Minecraft.getMinecraft().world.getBiome(Minecraft.getMinecraft().player.getPosition()) == TFBiomes.snowy_forest || Minecraft.getMinecraft().world.getBiome(Minecraft.getMinecraft().player.getPosition()) == TFBiomes.glacier);
    }
}
