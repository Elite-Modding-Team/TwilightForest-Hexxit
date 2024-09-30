package twilightforest.item.scepter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ItemTFScepterLifeDrain extends ItemTFScepter {

    public ItemTFScepterLifeDrain(EnumRarity rarity, int damage) {
        super(rarity, damage);
    }

    private void spawnRedParticles(World world, double srcX, double srcY, double srcZ, double destX, double destY, double destZ) {
        // make particle trail
        int particles = 32;
        for (int i = 0; i < particles; i++) {
            double trailFactor = i / (particles - 1.0D);
            float f = 1.0F;
            float f1 = 0.5F;
            float f2 = 0.5F;
            double tx = srcX + (destX - srcX) * trailFactor + world.rand.nextGaussian() * 0.005;
            double ty = srcY + (destY - srcY) * trailFactor + world.rand.nextGaussian() * 0.005;
            double tz = srcZ + (destZ - srcZ) * trailFactor + world.rand.nextGaussian() * 0.005;
            world.spawnParticle(EnumParticleTypes.SPELL_MOB, tx, ty, tz, f, f1, f2);
        }
    }

    /**
     * Animates the target falling apart into a rain of shatter particles
     */
    protected void spawnShatteringParticles(World world, EntityLivingBase target) {
        int itemId = Item.getIdFromItem(Items.ROTTEN_FLESH);
        for (int i = 0; i < 50; ++i) {
            double gaussX = itemRand.nextGaussian() * 0.02D;
            double gaussY = itemRand.nextGaussian() * 0.02D;
            double gaussZ = itemRand.nextGaussian() * 0.02D;
            double gaussFactor = 10.0D;
            world.spawnParticle(EnumParticleTypes.ITEM_CRACK, target.posX + itemRand.nextFloat() * target.width * 2.0F - target.width - gaussX * gaussFactor, target.posY + itemRand.nextFloat() * target.height - gaussY * gaussFactor, target.posZ + itemRand.nextFloat() * target.width * 2.0F - target.width - gaussZ * gaussFactor, gaussX, gaussY, gaussZ, itemId);
        }
    }

    @Override
    public Item getRepairItem(ItemStack stack) {
        return Items.FERMENTED_SPIDER_EYE;
    }

    private float getMaxHealth(EntityLivingBase target) {
        return (float) target.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase living, int count) {
        World world = living.world;

        if (stack.getItemDamage() >= this.getMaxDamage(stack)) {
            // do not use
            living.resetActiveHand();
            return;
        }

        if (count % 5 == 0) {

            // is the player looking at an entity
            Entity pointedEntity = getEntityTarget(world, living);

            if (pointedEntity != null && pointedEntity instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase) pointedEntity;

                if (target.getActivePotionEffect(MobEffects.SLOWNESS) != null || target.getHealth() < 1) {

                    if (target.getHealth() <= 3) {
                        // make it explode
                        spawnRedParticles(world, living.posX, living.posY + living.getEyeHeight(), living.posZ, target.posX, target.posY + target.getEyeHeight(), target.posZ);
                        if (target instanceof EntityLiving) {
                            ((EntityLiving) target).spawnExplosionParticle();
                        }
                        target.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F, ((itemRand.nextFloat() - itemRand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        spawnShatteringParticles(world, target);
                        if (!world.isRemote) {
                            target.setDead();
                            target.onDeath(DamageSource.causeIndirectMagicDamage(living, living));
                        }
                        living.resetActiveHand();
                    } else {
                        // we have hit this creature recently
                        if (!world.isRemote) {
                            target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(living, living), 3);

                            // only do lifting effect on creatures weaker than the player
                            if (getMaxHealth(target) <= getMaxHealth(living)) {
                                target.motionX = 0;
                                target.motionY = 0.2;
                                target.motionZ = 0;
                            }

                            target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 2));

                            if (count % 10 == 0) {
                                // heal the player
                                living.heal(1);
                                // and give foods
                                if (living instanceof EntityPlayer)
                                    ((EntityPlayer) living).getFoodStats().addStats(1, 0.1F);
                            }
                        }
                    }
                } else {
                    // this is a new creature to start draining
                    spawnRedParticles(world, living.posX, living.posY + living.getEyeHeight(), living.posZ, target.posX, target.posY + target.getEyeHeight(), target.posZ);

                    living.playSound(SoundEvents.ENTITY_ZOMBIE_INFECT, 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);

                    if (!world.isRemote) {
                        target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(living, living), 1);

                        // only do lifting effect on creatures weaker than the player
                        if (getMaxHealth(target) <= getMaxHealth(living)) {
                            target.motionX = 0;
                            target.motionY = 0.2;
                            target.motionZ = 0;
                        }

                        target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 2));
                    }
                }

                if (!world.isRemote) {
                    stack.damageItem(1, living);
                }
            }
        }
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return 72000;
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return oldStack.getItem() == newStack.getItem();
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || newStack.getItem() != oldStack.getItem();
    }
}
