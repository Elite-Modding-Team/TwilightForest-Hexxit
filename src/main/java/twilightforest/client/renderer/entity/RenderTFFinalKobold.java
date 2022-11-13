package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import twilightforest.entity.boss.EntityTFFinalKobold;

public class RenderTFFinalKobold extends RenderTFBiped<EntityTFFinalKobold> {

	private final float scale;

	public RenderTFFinalKobold(RenderManager manager, ModelBiped modelBiped, float shadowSize, String textureName) {
		super(manager, modelBiped, shadowSize, textureName);
		this.scale = 8.0F;
	}

	@Override
	protected void preRenderCallback(EntityTFFinalKobold entitylivingbaseIn, float partialTickTime) {
		GlStateManager.scale(this.scale, this.scale, this.scale);
	}
}
