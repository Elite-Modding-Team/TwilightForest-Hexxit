package twilightforest.compat;

import c4.conarm.common.armor.traits.ArmorTraits;
import c4.conarm.lib.materials.ArmorMaterialType;
import c4.conarm.lib.materials.ArmorMaterials;
import c4.conarm.lib.materials.CoreMaterialStats;
import c4.conarm.lib.materials.PlatesMaterialStats;
import c4.conarm.lib.materials.TrimMaterialStats;
import slimeknights.tconstruct.library.TinkerRegistry;

public class ConstructsArmory {
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
        ArmorMaterials.addArmorTrait(TConstruct.ironwood, ArmorTraits.shielding);
        ArmorMaterials.addArmorTrait(TConstruct.ironwood, ArmorTraits.ecological);
        ArmorMaterials.addArmorTrait(TConstruct.regal, ArmorTraits.voltaic);
    }
}
