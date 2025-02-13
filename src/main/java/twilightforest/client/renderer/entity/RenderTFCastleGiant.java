package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.finalcastle.EntityTFCastleGiant;

public class RenderTFCastleGiant extends RenderBiped<EntityTFCastleGiant> {
    private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("castleguard.png");

    public RenderTFCastleGiant(RenderManager manager) {
        super(manager, new ModelBiped(), 1.8F);
        this.addLayer(new LayerBipedArmor(this));
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTFCastleGiant entity) {
        return textureLoc;
    }

    @Override
    protected void preRenderCallback(EntityTFCastleGiant entity, float partialTicks) {
        float scale = 4.0F;
        GlStateManager.scale(scale, scale, scale);
    }
}
