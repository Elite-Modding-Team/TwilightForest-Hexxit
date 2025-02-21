package twilightforest.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFApocalypseCube;
import twilightforest.entity.finalcastle.EntityTFHarbingerCube;

public class RenderTFHarbingerCube extends RenderLiving<EntityTFHarbingerCube> {

    private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("apocalypse2.png");

    public RenderTFHarbingerCube(RenderManager manager) {
        super(manager, new ModelTFApocalypseCube(), 1.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityTFHarbingerCube entity) {
        return textureLoc;
    }

    @Override
    protected void preRenderCallback(EntityTFHarbingerCube entity, float partialTicks) {
        float bounce = entity.ticksExisted + partialTicks;
        GlStateManager.translate(0F, MathHelper.sin((bounce) * 0.2F) * 0.15F, 0F);

        // flash

        float f1 = entity.deathTime;
        if (f1 > 0) {
            float f2 = 1.0F + MathHelper.sin(f1 * 100.0F) * f1 * 0.01F;

            if (f1 < 0.0F) {
                f1 = 0.0F;
            }

            if (f1 > 1.0F) {
                f1 = 1.0F;
            }

            f1 *= f1;
            f1 *= f1;
            float f3 = (1.0F + f1 * 0.4F) * f2;
            float f4 = (1.0F + f1 * 0.1F) / f2;
            GlStateManager.scale(f3, f4, f3);
        }
    }

    @Override
    protected void applyRotations(EntityTFHarbingerCube entity, float ageInTicks, float rotationYaw, float partialTicks) {
        GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
    }

    @Override
    protected int getColorMultiplier(EntityTFHarbingerCube entity, float brightness, float partialTicks) {
        if (entity.deathTime > 0) {
            float f2 = entity.deathTime + partialTicks;

            if ((int) (f2 / 2) % 2 == 0) {
                return 0;
            } else {
                int i = (int) (f2 * 0.2F * 255.0F);

                if (i < 0) {
                    i = 0;
                }

                if (i > 255) {
                    i = 255;
                }

                short short1 = 255;
                short short2 = 255;
                short short3 = 255;
                return i << 24 | short1 << 16 | short2 << 8 | short3;
            }
        } else {
            return 0;
        }
    }
}
