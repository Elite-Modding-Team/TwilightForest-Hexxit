package twilightforest.client.model.entity.finalcastle;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.entity.finalcastle.EntityTFCastleMarksman;

@SideOnly(Side.CLIENT)
public class ModelTFCastleMarksman extends ModelBiped {
    public ModelTFCastleMarksman() {
        this(0.0F);
    }

    public ModelTFCastleMarksman(float modelSize) {
        super(modelSize, 0.0F, 64, 32);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTickTime) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;
        ItemStack itemstack = entity.getHeldItem(EnumHand.MAIN_HAND);
        if (itemstack.getItem() instanceof ItemBow && ((EntityTFCastleMarksman) entity).isSwingingArms()) {
            if (entity.getPrimaryHand() == EnumHandSide.RIGHT) {
                this.rightArmPose = ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = ArmPose.BOW_AND_ARROW;
            }
        }
        super.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTickTime);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
        ItemStack itemstack = ((EntityLivingBase) entity).getHeldItemMainhand();
        EntityTFCastleMarksman marksman = (EntityTFCastleMarksman) entity;
        if (marksman.isSwingingArms() && (itemstack.isEmpty() || !(itemstack.getItem() instanceof ItemBow))) {
            float f = MathHelper.sin(this.swingProgress * (float) Math.PI);
            float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
            this.bipedRightArm.rotateAngleZ = 0.0F;
            this.bipedLeftArm.rotateAngleZ = 0.0F;
            this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
            this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
            this.bipedRightArm.rotateAngleX = (-(float) Math.PI / 2F);
            this.bipedLeftArm.rotateAngleX = (-(float) Math.PI / 2F);
            ModelRenderer var10000 = this.bipedRightArm;
            var10000.rotateAngleX -= f * 1.2F - f1 * 0.4F;
            var10000 = this.bipedLeftArm;
            var10000.rotateAngleX -= f * 1.2F - f1 * 0.4F;
            var10000 = this.bipedRightArm;
            var10000.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            var10000 = this.bipedLeftArm;
            var10000.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            var10000 = this.bipedRightArm;
            var10000.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            var10000 = this.bipedLeftArm;
            var10000.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        }
    }

    @Override
    public void postRenderArm(float scale, EnumHandSide side) {
        float f = side == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        ModelRenderer modelrenderer = this.getArmForSide(side);
        modelrenderer.rotationPointX += f;
        modelrenderer.postRender(scale);
        modelrenderer.rotationPointX -= f;
    }
}
