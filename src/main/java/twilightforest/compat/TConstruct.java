package twilightforest.compat;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.client.material.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.tools.TinkerTraits;
import twilightforest.block.RegisterBlockEvent;
import twilightforest.compat.tcon.texture.FieryInfoDeserializer;
import twilightforest.compat.tcon.texture.GradientMapInfoDeserializer;
import twilightforest.compat.tcon.trait.*;
import twilightforest.item.TFItems;

public class TConstruct {

    public static final Material nagascale = new Material("nagascale", 0x32_5D_25);
    public static final Material ironwood = new Material("ironwood", 0x83_7C_62);
    public static final Material steeleaf = new Material("steeleaf", 0x52_87_3A);
    public static final Material fierymetal = new Material("fierymetal", 0xFD_D4_5D);
    public static final Material knightmetal = new Material("knightmetal", 0xC4_E6_AE);
    public static final Material ravenFeather = new Material("raven_feather", 0x47_4C_52);
    public static final Material regal = new Material("regal"  , 0x77_77_77);

    public static final AbstractTrait twilit = new TraitTwilit();
    public static final AbstractTrait precipitate = new TraitPrecipitate();
    public static final AbstractTrait stalwart = new TraitStalwart();
    public static final AbstractTrait synergy = new TraitSynergy();
    public static final AbstractTrait veiled = new TraitVeiled();

    static void preInit() {
        TinkerRegistry.addMaterialStats(TConstruct.nagascale,
                new HeadMaterialStats(460, 8.9f, 4.3f, HarvestLevels.IRON),
                new HandleMaterialStats(0.5f, 50),
                new ExtraMaterialStats(150),
                new BowMaterialStats(0.6f, 2f, 0f),
                new ArrowShaftMaterialStats(1.4f, 20)
        );
        TinkerRegistry.integrate(TConstruct.nagascale).preInit();
        
        TinkerRegistry.addMaterialStats(TConstruct.ironwood,
                new HeadMaterialStats(450, 6f, 5f, HarvestLevels.DIAMOND),
                new HandleMaterialStats(1.1f, 100),
                new ExtraMaterialStats(120),
                new BowMaterialStats(0.7f, 1.5f, 7f),
                new ArrowShaftMaterialStats(1.25f, 15)
        );
        TinkerRegistry.integrate(new MaterialIntegration(TConstruct.ironwood, null, "Ironwood")).toolforge().preInit();

        TinkerRegistry.addMaterialStats(TConstruct.steeleaf,
                new HeadMaterialStats(180, 7f, 6f, HarvestLevels.OBSIDIAN),
                new HandleMaterialStats(0.8f, 100),
                new ExtraMaterialStats(90),
                new BowMaterialStats(1.2f, 1.5f, 2f),
                new ArrowShaftMaterialStats(0.6f, 10),
                new FletchingMaterialStats(1f, 0.8f)
        );
        TinkerRegistry.integrate(new MaterialIntegration(TConstruct.steeleaf, null, "Steeleaf")).toolforge().preInit();

        TinkerRegistry.addMaterialStats(TConstruct.fierymetal,
                new HeadMaterialStats(720, 7.2f, 6.6f, HarvestLevels.COBALT),
                new HandleMaterialStats(0.7f, 400),
                new ExtraMaterialStats(200),
                new BowMaterialStats(1f, 0.9f, 4f),
                new ArrowShaftMaterialStats(0.8f, 0)
        );
        TinkerRegistry.integrate(new MaterialIntegration(TConstruct.fierymetal, RegisterBlockEvent.moltenFiery, "Fiery")).toolforge().preInit();

        TinkerRegistry.addMaterialStats(TConstruct.knightmetal,
                new HeadMaterialStats(900, 7f, 6f, HarvestLevels.COBALT),
                new HandleMaterialStats(1.25f, 100),
                new ExtraMaterialStats(400),
                new BowMaterialStats(0.4f, 2f, 7.5f)
        );
        TinkerRegistry.integrate(new MaterialIntegration(TConstruct.knightmetal, RegisterBlockEvent.moltenKnightmetal, "Knightmetal")).preInit();

        TinkerRegistry.addMaterialStats(TConstruct.ravenFeather,
                new FletchingMaterialStats(0.95f, 1.15f)
        );
        TinkerRegistry.integrate(TConstruct.ravenFeather).preInit();
        
        TinkerRegistry.addMaterialStats(TConstruct.regal,
                new HeadMaterialStats(950, 8f, 7f, HarvestLevels.COBALT),
                new HandleMaterialStats(1.3f, 200),
                new ExtraMaterialStats(450),
                new BowMaterialStats(0.5f, 2.5f, 7f)
        );
        TinkerRegistry.integrate(new MaterialIntegration(TConstruct.regal, RegisterBlockEvent.moltenRegal, "Regal")).preInit();

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            MaterialRenderInfoLoader.addRenderInfo("gradient_map_colors", GradientMapInfoDeserializer.class);
            MaterialRenderInfoLoader.addRenderInfo("fierymetal", FieryInfoDeserializer.class);
        }
    }

    static void init() {
        TConstruct.nagascale.addItem(TFItems.naga_scale, 1, Material.VALUE_Ingot);
        TConstruct.nagascale
                .addTrait(TConstruct.twilit, MaterialTypes.HEAD)
                .addTrait(TConstruct.precipitate, MaterialTypes.HEAD)
                .addTrait(TConstruct.precipitate)
                .setCraftable(true).setCastable(false)
                .setRepresentativeItem(TFItems.naga_scale);
        
        TConstruct.ironwood.addCommonItems("Ironwood");
        TConstruct.ironwood
                .addTrait(TConstruct.twilit, MaterialTypes.HEAD)
                .addTrait(TinkerTraits.stiff, MaterialTypes.HEAD)
                .addTrait(TinkerTraits.ecological)
                .setCraftable(true).setCastable(false)
                .setRepresentativeItem(TFItems.ironwood_ingot);

        TConstruct.steeleaf.addCommonItems("Steeleaf");
        TConstruct.steeleaf
                .addTrait(TConstruct.twilit, MaterialTypes.HEAD)
                .addTrait(TConstruct.synergy, MaterialTypes.HEAD)
                .addTrait(TConstruct.synergy)
                .setCraftable(true).setCastable(false)
                .setRepresentativeItem(TFItems.steeleaf_ingot);

        TConstruct.fierymetal.addCommonItems("Fiery");
        TConstruct.fierymetal
                .addTrait(TinkerTraits.superheat)
                .addTrait(TConstruct.twilit, MaterialTypes.HEAD)
                .addTrait(TinkerTraits.autosmelt, MaterialTypes.HEAD)
                //.addTrait(TinkerTraits.superheat, MaterialTypes.HEAD)
                .addTrait(TinkerTraits.flammable, MaterialTypes.HEAD)
                .setCraftable(false).setCastable(true)
                .setRepresentativeItem(TFItems.fiery_ingot);

        TConstruct.knightmetal.addCommonItems("Knightmetal");
        TConstruct.knightmetal.addItem(TFItems.armor_shard, 1, Material.VALUE_Nugget);
        TConstruct.knightmetal.addItem(TFItems.block_and_chain, 1, (Material.VALUE_Ingot * 7) + Material.VALUE_Block);
        TConstruct.knightmetal
                .addTrait(TConstruct.twilit, MaterialTypes.HEAD)
                .addTrait(TConstruct.stalwart, MaterialTypes.HEAD)
                .addTrait(TConstruct.stalwart)
                .setCraftable(false).setCastable(true)
                .setRepresentativeItem(TFItems.knightmetal_ingot);

        TConstruct.ravenFeather.addItem(TFItems.raven_feather, 1, Material.VALUE_Ingot);
        TConstruct.ravenFeather
                .addTrait(TConstruct.twilit, MaterialTypes.HEAD)
                .addTrait(TConstruct.veiled, MaterialTypes.HEAD)
                .addTrait(TConstruct.veiled)
                .setCraftable(true).setCastable(false)
                .setRepresentativeItem(TFItems.raven_feather);
        
        TConstruct.regal.addCommonItems("Regal");
        TConstruct.regal.addItem(TFItems.regal_shard, 1, Material.VALUE_Nugget);
        TConstruct.regal
                .addTrait(TConstruct.twilit, MaterialTypes.HEAD)
                .addTrait(TinkerTraits.shocking, MaterialTypes.HEAD)
                .addTrait(TinkerTraits.shocking)
                .setCraftable(false).setCastable(true)
                .setRepresentativeItem(TFItems.regal_ingot);
    }

    static void postInit() {
        TinkerRegistry.registerSmelteryFuel(new FluidStack(RegisterBlockEvent.essenceFiery, 50), 1000);
    }
}
