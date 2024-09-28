package twilightforest.item.scepter;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.item.ItemTF;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public abstract class ItemTFScepter extends ItemTF {

    public ItemTFScepter(EnumRarity rarity, int damage) {
        super(rarity);
        this.setMaxDamage(damage);
        this.setMaxStackSize(1);
    }

    public abstract Item getRepairItem(ItemStack stack);

    // The target player looks at
    protected final Entity getEntityTarget(World world, EntityLivingBase living) {
        Entity pointedEntity = null;
        double range = 20.0D;
        Vec3d srcVec = new Vec3d(living.posX, living.posY + living.getEyeHeight(), living.posZ);
        Vec3d lookVec = living.getLook(1.0F);
        Vec3d destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
        float var9 = 1.0F;
        List<Entity> possibleList = world.getEntitiesWithinAABBExcludingEntity(living, living.getEntityBoundingBox().expand(lookVec.x * range, lookVec.y * range, lookVec.z * range).grow(var9, var9, var9));
        double hitDist = 0;

        for (Entity possibleEntity : possibleList) {


            if (possibleEntity.canBeCollidedWith()) {
                float borderSize = possibleEntity.getCollisionBorderSize();
                AxisAlignedBB collisionBB = possibleEntity.getEntityBoundingBox().grow((double) borderSize, (double) borderSize, (double) borderSize);
                RayTraceResult interceptPos = collisionBB.calculateIntercept(srcVec, destVec);

                if (collisionBB.contains(srcVec)) {
                    if (0.0D < hitDist || hitDist == 0.0D) {
                        pointedEntity = possibleEntity;
                        hitDist = 0.0D;
                    }
                } else if (interceptPos != null) {
                    double possibleDist = srcVec.distanceTo(interceptPos.hitVec);

                    if (possibleDist < hitDist || hitDist == 0.0D) {
                        pointedEntity = possibleEntity;
                        hitDist = possibleDist;
                    }
                }
            }
        }
        return pointedEntity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {
        super.addInformation(stack, world, tooltip, flags);
        tooltip.add(I18n.format("twilightforest.scepter_charges", stack.getMaxDamage() - stack.getItemDamage()));
    }
}
