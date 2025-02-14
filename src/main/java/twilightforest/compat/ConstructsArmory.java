package twilightforest.compat;

import c4.conarm.common.armor.traits.ArmorTraits;
import c4.conarm.lib.materials.ArmorMaterials;
import c4.conarm.lib.materials.CoreMaterialStats;
import c4.conarm.lib.materials.PlatesMaterialStats;
import c4.conarm.lib.materials.TrimMaterialStats;
import c4.conarm.lib.traits.AbstractArmorTrait;
import slimeknights.tconstruct.library.TinkerRegistry;
import twilightforest.compat.tcon.trait.armor.TraitPrecipitateArmor;
import twilightforest.compat.tcon.trait.armor.TraitStalwartArmor;
import twilightforest.compat.tcon.trait.armor.TraitSynergyArmor;
import twilightforest.compat.tcon.trait.armor.TraitTwilitArmor;

public class ConstructsArmory {
    // Armor Traits
    public static final AbstractArmorTrait twilit_armor = new TraitTwilitArmor();
    public static final AbstractArmorTrait precipitate_armor = new TraitPrecipitateArmor();
    public static final AbstractArmorTrait synergy_armor = new TraitSynergyArmor();
    public static final AbstractArmorTrait stalwart_armor = new TraitStalwartArmor();

    static void preInit() {
        TinkerRegistry.addMaterialStats(TConstruct.nagascale,
                new CoreMaterialStats(17.0F, 16.5F),
                new PlatesMaterialStats(0.7F, 5.0F, 2.0F),
                new TrimMaterialStats(8.0F)
        );

        TinkerRegistry.addMaterialStats(TConstruct.ironwood,
                new CoreMaterialStats(18.0F, 16.0F),
                new PlatesMaterialStats(1.1F, 8.0F, 1.0F),
                new TrimMaterialStats(7.8F)
        );

        TinkerRegistry.addMaterialStats(TConstruct.steeleaf,
                new CoreMaterialStats(11.0F, 17.0F),
                new PlatesMaterialStats(0.8F, 8.0F, 1.0F),
                new TrimMaterialStats(7.2F)
        );

        TinkerRegistry.addMaterialStats(TConstruct.knightmetal,
                new CoreMaterialStats(20.0F, 19.0F),
                new PlatesMaterialStats(1.25F, 8.0F, 3.0F),
                new TrimMaterialStats(16.0F)
        );

        TinkerRegistry.addMaterialStats(TConstruct.regal,
                new CoreMaterialStats(22.5F, 22.0F),
                new PlatesMaterialStats(1.3F, 12.0F, 4.5F),
                new TrimMaterialStats(16.5F)
        );
    }

    static void init() {
        ArmorMaterials.addArmorTrait(TConstruct.nagascale, twilit_armor);
        ArmorMaterials.addArmorTrait(TConstruct.nagascale, precipitate_armor);
        ArmorMaterials.addArmorTrait(TConstruct.ironwood, twilit_armor);
        ArmorMaterials.addArmorTrait(TConstruct.ironwood, ArmorTraits.shielding);
        ArmorMaterials.addArmorTrait(TConstruct.ironwood, ArmorTraits.ecological);
        ArmorMaterials.addArmorTrait(TConstruct.steeleaf, twilit_armor);
        ArmorMaterials.addArmorTrait(TConstruct.steeleaf, synergy_armor);
        ArmorMaterials.addArmorTrait(TConstruct.knightmetal, twilit_armor);
        ArmorMaterials.addArmorTrait(TConstruct.knightmetal, stalwart_armor);
        ArmorMaterials.addArmorTrait(TConstruct.regal, twilit_armor);
        ArmorMaterials.addArmorTrait(TConstruct.regal, ArmorTraits.voltaic);
    }
}
