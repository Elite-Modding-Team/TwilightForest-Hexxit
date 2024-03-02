package twilightforest.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFFireBeetle;
import twilightforest.entity.EntityTFPinchBeetle;

public class RenderTFGenericLiving<T extends EntityLiving> extends RenderLiving<T> {

    private final ResourceLocation textureLoc;

    public RenderTFGenericLiving(RenderManager manager, ModelBase model, float shadowSize, String textureName) {
        super(manager, model, shadowSize);

        if (textureName.startsWith("textures")) {
            textureLoc = new ResourceLocation(textureName);
        } else {
            textureLoc = TwilightForestMod.getModelTexture(textureName);
        }
    }

    @Override
    protected float getDeathMaxRotation(EntityLiving entityLivingBaseIn) {
        if (entityLivingBaseIn instanceof EntityTFFireBeetle || entityLivingBaseIn instanceof EntityTFPinchBeetle) {
            return 180.0F;
        }
        return 90.0F;
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return textureLoc;
    }
}
