package twilightforest.entity.finalcastle;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.item.TFItems;

public class EntityTFCastleWarrior extends EntityTFCastleMob {
    public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/castle_warrior");

    public EntityTFCastleWarrior(World world) {
        super(world);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
        setEquipmentBasedOnDifficulty(difficulty);
        setEnchantmentBasedOnDifficulty(difficulty);
        return data;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);

        if (this.rand.nextDouble() <= 0.4D && !(this instanceof EntityTFCastleMarksman)) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.regal_axe));
        } else {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.regal_sword));
        }

        this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(TFItems.regal_helmet));
        this.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(TFItems.regal_chestplate));
        this.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(TFItems.regal_leggings));
        this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(TFItems.regal_boots));
    }

    @Override
    public ResourceLocation getLootTable() {
        return LOOT_TABLE;
    }
}
