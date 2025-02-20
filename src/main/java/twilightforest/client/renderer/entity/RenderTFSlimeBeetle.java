package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFSlimeBeetle;
import twilightforest.entity.EntityTFSlimeBeetle;

public class RenderTFSlimeBeetle extends RenderLiving<EntityTFSlimeBeetle> {

    private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("slimebeetle.png");

    public RenderTFSlimeBeetle(RenderManager manager, ModelBase par1ModelBase, float shadowSize) {
        super(manager, par1ModelBase, shadowSize);
        addLayer(new LayerInner());
    }

    @Override
    protected float getDeathMaxRotation(EntityTFSlimeBeetle entityLivingBaseIn) {
        return 180.0F;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTFSlimeBeetle entity) {
        return textureLoc;
    }

    class LayerInner implements LayerRenderer<EntityTFSlimeBeetle> {
        private final ModelBase innerModel = new ModelTFSlimeBeetle(true);

        @Override
        public void doRenderLayer(EntityTFSlimeBeetle entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            if (!entity.isInvisible()) {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableNormalize();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                this.innerModel.setModelAttributes(RenderTFSlimeBeetle.this.getMainModel());
                this.innerModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GlStateManager.disableBlend();
                GlStateManager.disableNormalize();
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return true;
        }
    }
}
