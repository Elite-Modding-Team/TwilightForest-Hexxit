package twilightforest.client;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFConfig;
import twilightforest.TFEventListener;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.RegisterBlockEvent;
import twilightforest.block.TFBlocks;
import twilightforest.client.renderer.TFWeatherRenderer;
import twilightforest.client.texture.GradientMappedTexture;
import twilightforest.client.texture.GradientNode;
import twilightforest.client.texture.MoltenFieryTexture;
import twilightforest.compat.TFCompat;
import twilightforest.compat.ie.IEShaderRegister;
import twilightforest.world.TFWorld;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Side.CLIENT)
public class TFClientEvents {

    public static final GradientNode[] KNIGHTMETAL_GRADIENT_MAP = {
            new GradientNode(0.0f, 0xFF_33_32_32),
            new GradientNode(0.1f, 0xFF_6A_73_5E),
            new GradientNode(0.15f, 0xFF_80_8C_72),
            new GradientNode(0.3f, 0xFF_A3_B3_91),
            new GradientNode(0.6f, 0xFF_C4_D6_AE),
            new GradientNode(1.0f, 0xFF_E7_FC_CD)
    };
    public static final GradientNode[] FIERY_ESSENCE_GRADIENT_MAP = {
            new GradientNode(0.2f, 0xFF_3D_17_17),
            new GradientNode(0.8f, 0xFF_5C_0B_0B)
    };
    public static final GradientNode[] EASY_GRAYSCALING_MAP = {
            new GradientNode(0.0f, 0xFF_80_80_80),
            new GradientNode(0.5f, 0xFF_AA_AA_AA), // AAAAAAaaaaaaaaaaa
            new GradientNode(1.0f, 0xFF_FF_FF_FF)
    };
    public static final float PI = (float) Math.PI;
    private static final int SINE_TICKER_BOUND = (int) ((PI * 200.0F) - 1.0F);

    //FIXME shove onto an external player layer, like armor
    //@SubscribeEvent
    //public static void renderLivingPre(RenderLivingEvent.Pre<EntityLivingBase> event) {
    //	// Shields
    //	if (event.getEntity().hasCapability(CapabilityList.SHIELDS, null)) {
    //		ShaderManager.useShader(ShaderManager.shieldShader, ShaderManager.TIME);
    //	}
    //}

    public static SoundEvent[] musicSlices = new SoundEvent[]{
            TFSounds.BOSS_MUSIC_START,
            TFSounds.BOSS_MUSIC_MAIN_1,
            TFSounds.BOSS_MUSIC_MAIN_2,
            TFSounds.BOSS_MUSIC_MAIN_3,
            TFSounds.BOSS_MUSIC_MAIN_4,
            TFSounds.BOSS_MUSIC_MAIN_5,
            TFSounds.BOSS_MUSIC_MAIN_6,
            TFSounds.BOSS_MUSIC_MAIN_7,
            TFSounds.BOSS_MUSIC_MAIN_8,
            TFSounds.BOSS_MUSIC_MAIN_9,
            TFSounds.BOSS_MUSIC_MAIN_10,
            TFSounds.BOSS_MUSIC_MAIN_11,
            TFSounds.BOSS_MUSIC_MAIN_12,
            TFSounds.BOSS_MUSIC_MAIN_13,
            TFSounds.BOSS_MUSIC_MAIN_14,
            TFSounds.BOSS_MUSIC_MAIN_15,
            TFSounds.BOSS_MUSIC_END,
    };
    public static int musicIndex = 0;
    public static int musicTime = 0;
    public static int time = 0;
    public static float rotationTicker = 0;
    public static float sineTicker = 0;
    private static int rotationTickerI = 0;
    private static int sineTickerI = 0;

    @SubscribeEvent
    public static void texStitch(TextureStitchEvent.Pre evt) {
        TextureMap map = evt.getMap();

        map.registerSprite(new ResourceLocation(TwilightForestMod.ID, "particles/snow_0"));
        map.registerSprite(new ResourceLocation(TwilightForestMod.ID, "particles/snow_1"));
        map.registerSprite(new ResourceLocation(TwilightForestMod.ID, "particles/snow_2"));
        map.registerSprite(new ResourceLocation(TwilightForestMod.ID, "particles/snow_3"));
        map.registerSprite(new ResourceLocation(TwilightForestMod.ID, "particles/annihilate_particle"));
        map.registerSprite(new ResourceLocation(TwilightForestMod.ID, "particles/firefly"));
        map.registerSprite(new ResourceLocation(TwilightForestMod.ID, "particles/fallen_leaf"));

        map.setTextureEntry(new MoltenFieryTexture(new ResourceLocation("minecraft", "blocks/lava_still"), RegisterBlockEvent.moltenFieryStill));
        map.setTextureEntry(new MoltenFieryTexture(new ResourceLocation("minecraft", "blocks/lava_flow"), RegisterBlockEvent.moltenFieryFlow));
        map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("minecraft", "blocks/lava_still"), RegisterBlockEvent.moltenKnightmetalStill, true, KNIGHTMETAL_GRADIENT_MAP));
        map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("minecraft", "blocks/lava_flow"), RegisterBlockEvent.moltenKnightmetalFlow, true, KNIGHTMETAL_GRADIENT_MAP));
        map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("minecraft", "blocks/water_still"), RegisterBlockEvent.essenceFieryStill, true, FIERY_ESSENCE_GRADIENT_MAP));
        map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("minecraft", "blocks/water_flow"), RegisterBlockEvent.essenceFieryFlow, true, FIERY_ESSENCE_GRADIENT_MAP));
        map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("minecraft", "blocks/lava_still"), RegisterBlockEvent.moltenRegalStill, true, EASY_GRAYSCALING_MAP));
        map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("minecraft", "blocks/lava_flow"), RegisterBlockEvent.moltenRegalFlow, true, EASY_GRAYSCALING_MAP));

        if (TFCompat.IMMERSIVEENGINEERING.isActivated()) {
            map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("immersiveengineering", "revolvers/shaders/revolver_grip"), IEShaderRegister.PROCESSED_REVOLVER_GRIP_LAYER, true, EASY_GRAYSCALING_MAP));
            map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("immersiveengineering", "revolvers/shaders/revolver_0"), IEShaderRegister.PROCESSED_REVOLVER_LAYER, true, EASY_GRAYSCALING_MAP));
            map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("immersiveengineering", "items/shaders/chemthrower_0"), IEShaderRegister.PROCESSED_CHEMTHROW_LAYER, true, EASY_GRAYSCALING_MAP));
            map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("immersiveengineering", "items/shaders/drill_diesel_0"), IEShaderRegister.PROCESSED_DRILL_LAYER, true, EASY_GRAYSCALING_MAP));
            map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("immersiveengineering", "items/shaders/railgun_0"), IEShaderRegister.PROCESSED_RAILGUN_LAYER, true, EASY_GRAYSCALING_MAP));
            map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("immersiveengineering", "items/shaders/shield_0"), IEShaderRegister.PROCESSED_SHIELD_LAYER, true, EASY_GRAYSCALING_MAP));
            //	map.setTextureEntry( new GradientMappedTexture( new ResourceLocation( "immersiveengineering", ""                                ), IEShaderRegister.PROCESSED_MINECART_LAYER     , true, EASY_GRAYSCALING_MAP ));
            map.setTextureEntry(new GradientMappedTexture(new ResourceLocation("immersiveengineering", "blocks/shaders/balloon_0"), IEShaderRegister.PROCESSED_BALLOON_LAYER, true, EASY_GRAYSCALING_MAP));

            final String[] types = new String[]{"1_0", "1_2", "1_4", "1_5", "1_6"};

            for (IEShaderRegister.CaseType caseType : IEShaderRegister.CaseType.everythingButMinecart()) {
                for (String type : types) {
                    map.setTextureEntry(new GradientMappedTexture(
                            IEShaderRegister.ModType.IMMERSIVE_ENGINEERING.provideTex(caseType, type),
                            IEShaderRegister.ModType.TWILIGHT_FOREST.provideTex(caseType, type),
                            true, EASY_GRAYSCALING_MAP
                    ));
                }
            }
        }
    }

    /**
     * Stop the game from rendering the mount health for unfriendly creatures
     */
    @SubscribeEvent
    public static boolean preOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTHMOUNT) {
            if (TFEventListener.isRidingUnfriendly(Minecraft.getMinecraft().player)) {
                event.setCanceled(true);
                return false;
            }
        }
        return true;
    }

    /**
     * Render various effects such as an iced entity
     */
    @SubscribeEvent
    public static void renderLivingPost(RenderLivingEvent.Post<EntityLivingBase> event) {
        for (RenderEffect effect : RenderEffect.VALUES) {
            if (effect.shouldRender(event.getEntity(), false)) {
                effect.render(event.getEntity(), event.getRenderer(), event.getX(), event.getY(), event.getZ(), event.getPartialRenderTick(), false);
            }
        }
    }

    /**
     * Render effects in first-person perspective
     */
    @SubscribeEvent
    public static void renderWorldLast(RenderWorldLastEvent event) {

        if (!TFConfig.firstPersonEffects) return;

        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        if (settings.thirdPersonView != 0 || settings.hideGUI) return;

        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        if (entity instanceof EntityLivingBase) {
            Render<? extends Entity> renderer = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(entity);
            if (renderer instanceof RenderLivingBase<?>) {
                for (RenderEffect effect : RenderEffect.VALUES) {
                    if (effect.shouldRender((EntityLivingBase) entity, true)) {
                        effect.render((EntityLivingBase) entity, (RenderLivingBase<? extends EntityLivingBase>) renderer, 0.0, 0.0, 0.0, event.getPartialTicks(), true);
                    }
                }
            }
        }
    }

    /**
     * On the tick, we kill the vignette
     */
    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Minecraft minecraft = Minecraft.getMinecraft();

            boolean fancyGraphics = minecraft.gameSettings.fancyGraphics;
            TFBlocks.twilight_leaves.setGraphicsLevel(fancyGraphics);
            TFBlocks.twilight_leaves_3.setGraphicsLevel(fancyGraphics);
            TFBlocks.magic_leaves.setGraphicsLevel(fancyGraphics);

            // only fire if we're in the twilight forest
            if (minecraft.world != null && TFWorld.isTwilightForest(minecraft.world)) {
                // vignette
                if (minecraft.ingameGUI != null) {
                    minecraft.ingameGUI.prevVignetteBrightness = 0.0F;
                }
            }//*/

            if (minecraft.player != null && TFEventListener.isRidingUnfriendly(minecraft.player)) {
                if (minecraft.ingameGUI != null) {
                    minecraft.ingameGUI.setOverlayMessage("", false);
                }
            }
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        time++;

        Minecraft mc = Minecraft.getMinecraft();
        float partial = mc.getRenderPartialTicks();

        rotationTickerI = (rotationTickerI >= 359 ? 0 : rotationTickerI + 1);
        sineTickerI = (sineTickerI >= SINE_TICKER_BOUND ? 0 : sineTickerI + 1);

        rotationTicker = rotationTickerI + partial;
        sineTicker = sineTicker + partial;

        BugModelAnimationHelper.animate();

        if (!mc.isGamePaused() && mc.world != null && mc.world.provider.getWeatherRenderer() instanceof TFWeatherRenderer) {
            ((TFWeatherRenderer) mc.world.provider.getWeatherRenderer()).tick();
        }
    }

    @SubscribeEvent
    public static void bossMusicFight(LivingEvent.LivingUpdateEvent event) {
        try {
            EntityLivingBase entity = event.getEntityLiving();
            if (entity.world.isRemote || !(entity instanceof EntityWroughtnaut)) return;
            EntityWroughtnaut boss = (EntityWroughtnaut) entity;
            if (musicTime == 0 && boss.getHealth() > 0) {
                // fight is ongoing
                if (boss.getAttackTarget() != null && boss.isActive()) {
                    // stop other background music
                    if (musicIndex == 0) {
                        stopSounds();
                    }
                    // play current music slice
                    playBossMusic(musicIndex);
                    // set music time
                    musicTime = 113;
                    // skip to first main music slice if end of queue is reached
                    if (musicIndex == 15) {
                        musicIndex = 1;
                    }
                    // advance to next music slice otherwise
                    else {
                        musicIndex++;
                    }
                }
                // fight is interrupted, play end music slice and reset
                else if (musicIndex != 0) {
                    playBossMusic(16);
                    musicIndex = 0;
                }
            }
            // reduce music time
            if (musicTime > 0) {
                musicTime--;
            }
        } catch (Exception ignored) {
        }
    }

    @SubscribeEvent
    public static void bossMusicDeath(LivingDeathEvent event) {
        try {
            EntityLivingBase entity = event.getEntityLiving();
            if (entity.world.isRemote || !(entity instanceof EntityWroughtnaut)) return;
            // stop fight music
            stopSounds();
            // play end music slice
            playBossMusic(16);
            // play death sound
            TwilightForestMod.proxy.playSoundAtClientPlayer(TFSounds.FINALBOSS_DEATH);
            // reset music index
            musicIndex = 0;
            // reset music time
            musicTime = 0;
        } catch (Exception ignored) {
        }
    }

    public static void playBossMusic(int index) {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMusicRecord(musicSlices[index]));
    }

    public static void stopSounds() {
        Minecraft.getMinecraft().getSoundHandler().stopSounds();
    }
}
