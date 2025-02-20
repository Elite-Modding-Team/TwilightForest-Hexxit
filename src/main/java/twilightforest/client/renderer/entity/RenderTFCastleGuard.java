package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.finalcastle.EntityTFCastleMob;

public class RenderTFCastleGuard extends RenderBiped<EntityTFCastleMob> {
    private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("castleguard.png");

    public RenderTFCastleGuard(RenderManager renderManager) {
        super(renderManager, new ModelBiped(), 0.5F);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this));
    }

    @Override
    protected void preRenderCallback(EntityTFCastleMob entity, float partialTickTime) {
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTFCastleMob entity) {
        return textureLoc;
    }
}
