package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFTowerTermite;

public class RenderTFTowerTermite extends RenderLiving<EntityTFTowerTermite> {

    private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("towertermite.png");

    public RenderTFTowerTermite(RenderManager manager, ModelBase model, float shadowSize)
    {
    	super(manager, model, shadowSize);
    }

    protected float getDeathMaxRotation(EntityTFTowerTermite entityLivingBaseIn)
    {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityTFTowerTermite entity)
    {
        return textureLoc;
    }
}
