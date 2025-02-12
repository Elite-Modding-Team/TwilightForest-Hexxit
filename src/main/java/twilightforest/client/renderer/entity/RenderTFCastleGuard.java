package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFCastleMob;

public class RenderTFCastleGuard extends RenderBiped<EntityTFCastleMob> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("castleguard.png");

	public RenderTFCastleGuard(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelBiped(), 0.5F);
        this.addLayer(new LayerBipedArmor(this));
    }
	
	protected void preRenderCallback(EntityTFCastleMob entitylivingbaseIn, float partialTickTime) {
    	GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityTFCastleMob entity) {
		return textureLoc;
	}
}
