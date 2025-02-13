package twilightforest.client.model.entity.finalcastle;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelTFCastlePhantom extends ModelBiped {
    public ModelRenderer dress;

    public ModelTFCastlePhantom() {
        super();
        float f = 0.0F;
        dress = new ModelRenderer(this, 40, 16);
        dress.addBox(-4F, 12.0F, -2F, 8, 12, 4, f);
        dress.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        bipedHead.render(scale);
        bipedBody.render(scale);
        bipedRightArm.render(scale);
        bipedLeftArm.render(scale);
        dress.render(scale);
        bipedHeadwear.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        float var8 = MathHelper.sin(this.swingProgress * (float) Math.PI);
        float var9 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightArm.rotateAngleY = -(0.1F - var8 * 0.6F);
        this.bipedLeftArm.rotateAngleY = 0.1F - var8 * 0.6F;
        this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F);
        this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F);
        this.bipedRightArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
        this.bipedLeftArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
        this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
    }
}
