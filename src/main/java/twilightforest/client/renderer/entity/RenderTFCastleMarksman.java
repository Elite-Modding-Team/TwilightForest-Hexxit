package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.finalcastle.ModelTFCastleMarksman;
import twilightforest.entity.finalcastle.EntityTFCastleMob;

public class RenderTFCastleMarksman extends RenderBiped<EntityTFCastleMob> {
    private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("castleguard.png");

    public RenderTFCastleMarksman(RenderManager renderManager) {
        super(renderManager, new ModelTFCastleMarksman(), 0.5F);
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
