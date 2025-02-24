package twilightforest.loot;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import twilightforest.TwilightForestMod;
import twilightforest.entity.*;
import twilightforest.entity.boss.EntityTFHydra;
import twilightforest.entity.boss.EntityTFIceCrystal;
import twilightforest.entity.boss.EntityTFLich;
import twilightforest.entity.boss.EntityTFMinoshroom;
import twilightforest.entity.boss.EntityTFNaga;
import twilightforest.entity.boss.EntityTFSnowQueen;
import twilightforest.entity.boss.EntityTFYetiAlpha;
import twilightforest.entity.passive.*;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFTreasure {
	// For easy testing:
	// /give @p chest 1 0 {"display":{"Name":"Master Loot Crate"},"BlockEntityTag":{"LootTable":"twilightforest:entities/all_bosses"}}

	public static final TFTreasure hill1 = new TFTreasure("hill_1");
	public static final TFTreasure hill2 = new TFTreasure("hill_2");
	public static final TFTreasure hill3 = new TFTreasure("hill_3");
	public static final TFTreasure hedgemaze = new TFTreasure("hedge_maze");
	public static final TFTreasure labyrinth_room = new TFTreasure("labyrinth_room");
	public static final TFTreasure labyrinth_deadend = new TFTreasure("labyrinth_dead_end");
	public static final TFTreasure tower_room = new TFTreasure("tower_room");
	public static final TFTreasure tower_library = new TFTreasure("tower_library");
	public static final TFTreasure basement = new TFTreasure("basement");
	public static final TFTreasure labyrinth_vault = new TFTreasure("labyrinth_vault");
	public static final TFTreasure darktower_cache = new TFTreasure("darktower_cache");
	public static final TFTreasure darktower_key = new TFTreasure("darktower_key");
	public static final TFTreasure darktower_boss = new TFTreasure("darktower_boss");
	public static final TFTreasure tree_cache = new TFTreasure("tree_cache");
	public static final TFTreasure stronghold_cache = new TFTreasure("stronghold_cache");
	public static final TFTreasure stronghold_room = new TFTreasure("stronghold_room");
	public static final TFTreasure stronghold_boss = new TFTreasure("stronghold_boss");
	public static final TFTreasure aurora_cache = new TFTreasure("aurora_cache");
	public static final TFTreasure aurora_room = new TFTreasure("aurora_room");
	public static final TFTreasure aurora_boss = new TFTreasure("aurora_boss");
	public static final TFTreasure troll_garden = new TFTreasure("troll_garden");
	public static final TFTreasure troll_vault = new TFTreasure("troll_vault");
	public static final TFTreasure graveyard = new TFTreasure("graveyard");
	public static final TFTreasure final_castle = new TFTreasure("final_castle");

	public static final ResourceLocation FINAL_BOSS_LOOT_TABLE = TwilightForestMod.prefix("entities/final_boss");

	public static void init() {
		// Preload all entity tables
		LootTableList.register(EntityTFArmoredGiant.LOOT_TABLE);
		LootTableList.register(EntityTFBird.LOOT_TABLE);
		LootTableList.register(EntityTFBighorn.SHEARED_LOOT_TABLE);
		EntityTFBighorn.COLORED_LOOT_TABLES.values().forEach(LootTableList::register);
		LootTableList.register(EntityTFBlockGoblin.LOOT_TABLE);
		LootTableList.register(EntityTFBoar.LOOT_TABLE);
		LootTableList.register(EntityTFBunny.LOOT_TABLE);
		LootTableList.register(EntityTFDeathTome.LOOT_TABLE);
		LootTableList.register(EntityTFDeathTome.HURT_LOOT_TABLE);
		LootTableList.register(EntityTFDeer.LOOT_TABLE);
		LootTableList.register(EntityTFFireBeetle.LOOT_TABLE);
		LootTableList.register(EntityTFGiantMiner.LOOT_TABLE);
		LootTableList.register(EntityTFGoblinKnightUpper.LOOT_TABLE);
		LootTableList.register(EntityTFHelmetCrab.LOOT_TABLE);
		LootTableList.register(EntityTFHydra.LOOT_TABLE);
		LootTableList.register(EntityTFIceCrystal.LOOT_TABLE);
		LootTableList.register(EntityTFIceExploder.LOOT_TABLE);
		LootTableList.register(EntityTFIceShooter.LOOT_TABLE);
		LootTableList.register(EntityTFKobold.LOOT_TABLE);
		LootTableList.register(EntityTFLich.LOOT_TABLE);
		LootTableList.register(EntityTFMazeSlime.LOOT_TABLE);
		LootTableList.register(EntityTFMiniGhast.LOOT_TABLE);
		LootTableList.register(EntityTFMinoshroom.LOOT_TABLE);
		LootTableList.register(EntityTFMinotaur.LOOT_TABLE);
		LootTableList.register(EntityTFNaga.LOOT_TABLE);
		LootTableList.register(EntityTFPenguin.LOOT_TABLE);
		LootTableList.register(EntityTFQuestRam.LOOT_TABLE);
		LootTableList.register(EntityTFQuestRam.REWARD_LOOT_TABLE);
		LootTableList.register(EntityTFRaven.LOOT_TABLE);
		LootTableList.register(EntityTFRedcap.LOOT_TABLE);
		LootTableList.register(EntityTFSkeletonDruid.LOOT_TABLE);
		LootTableList.register(EntityTFSlimeBeetle.LOOT_TABLE);
		LootTableList.register(EntityTFSnowGuardian.LOOT_TABLE);
		LootTableList.register(EntityTFSnowQueen.LOOT_TABLE);
		LootTableList.register(EntityTFSquirrel.LOOT_TABLE);
		LootTableList.register(EntityTFTinyBird.LOOT_TABLE);
		LootTableList.register(EntityTFTowerGolem.LOOT_TABLE);
		LootTableList.register(EntityTFTowerTermite.LOOT_TABLE);
		LootTableList.register(EntityTFTroll.LOOT_TABLE);
		LootTableList.register(EntityTFWinterWolf.LOOT_TABLE);
		LootTableList.register(EntityTFWraith.LOOT_TABLE);
		LootTableList.register(EntityTFYeti.LOOT_TABLE);
		LootTableList.register(EntityTFYetiAlpha.LOOT_TABLE);
		LootTableList.register(FINAL_BOSS_LOOT_TABLE);

		LootFunctionManager.registerFunction(new LootFunctionEnchant.Serializer());
		LootFunctionManager.registerFunction(new LootFunctionModItemSwap.Serializer());

		LootConditionManager.registerCondition(new LootConditionIsMinion.Serializer());
		LootConditionManager.registerCondition(new LootConditionModExists.Serializer());
	}

	private final ResourceLocation lootTable;

	private TFTreasure(String path) {
		lootTable = TwilightForestMod.prefix(String.format("structures/%s/%s", path, path));

		// only preload the primary table, the subtables will be loaded on-demand the first time the primary table is used
		LootTableList.register(lootTable);
	}

	public void generateChest(World world, BlockPos pos, boolean trapped) {
		world.setBlockState(pos, trapped ? Blocks.TRAPPED_CHEST.getDefaultState() : Blocks.CHEST.getDefaultState(), 2);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityChest) {
			((TileEntityChest) te).setLootTable(lootTable, world.getSeed() * pos.getX() + pos.getY() ^ pos.getZ());
		}
	}

	public void generateChestContents(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityChest)
			((TileEntityChest) te).setLootTable(lootTable, world.getSeed() * pos.getX() + pos.getY() ^ pos.getZ());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLootTableLoad(LootTableLoadEvent event) {
		if (event.getName().equals(new ResourceLocation(MowziesMobs.MODID, "entities/frostmaw"))) {
			LootTable lootAlphaYeti = event.getLootTableManager().getLootTableFromLocation(EntityTFYetiAlpha.LOOT_TABLE);
			event.getTable().addPool(lootAlphaYeti.getPool("fur"));
			event.getTable().addPool(lootAlphaYeti.getPool("icebombs"));
			event.getTable().addPool(lootAlphaYeti.getPool("shader"));
		}

		if (event.getName().equals(new ResourceLocation(MowziesMobs.MODID, "entities/ferrous_wroughtnaut"))) {
			LootTable lootFinalBoss = event.getLootTableManager().getLootTableFromLocation(FINAL_BOSS_LOOT_TABLE);
			event.getTable().addPool(lootFinalBoss.getPool("trophy"));
			event.getTable().addPool(lootFinalBoss.getPool("sword"));
		}
	}
}
