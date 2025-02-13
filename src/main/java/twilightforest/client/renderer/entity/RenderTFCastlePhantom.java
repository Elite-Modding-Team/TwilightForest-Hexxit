package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.finalcastle.ModelTFCastlePhantom;
import twilightforest.entity.finalcastle.EntityTFCastlePhantom;

import javax.annotation.Nonnull;

public class RenderTFCastlePhantom extends RenderBiped<EntityTFCastlePhantom> {
    private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("castleguard.png");

    public RenderTFCastlePhantom(RenderManager renderManager) {
        super(renderManager, new ModelTFCastlePhantom(), 0.5F);
        addLayer(new LayerCastlePhantom());
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityTFCastlePhantom wraith) {
        return null;
    }

    class LayerCastlePhantom implements LayerRenderer<EntityTFCastlePhantom> {
        @Override
        public void doRenderLayer(@Nonnull EntityTFCastlePhantom wraith, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            RenderTFCastlePhantom.this.bindTexture(textureLoc);
            GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            RenderTFCastlePhantom.this.getMainModel().render(wraith, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
