package twilightforest.compat;

import c4.conarm.common.armor.traits.ArmorTraits;
import c4.conarm.lib.materials.ArmorMaterials;
import c4.conarm.lib.materials.CoreMaterialStats;
import c4.conarm.lib.materials.PlatesMaterialStats;
import c4.conarm.lib.materials.TrimMaterialStats;
import slimeknights.tconstruct.library.TinkerRegistry;

public class ConstructsArmory {
    static void preInit() {
        TinkerRegistry.addMaterialStats(TConstruct.ironwood,
                new CoreMaterialStats(2.5F, 3),
                new PlatesMaterialStats(1, 1, 0),
                new TrimMaterialStats(0.5F)
        );
    }

    static void init() {
        ArmorMaterials.addArmorTrait(TConstruct.ironwood, ArmorTraits.ecological);
    }
}
