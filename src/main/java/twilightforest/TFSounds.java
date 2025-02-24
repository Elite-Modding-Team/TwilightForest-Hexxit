package twilightforest;

import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import twilightforest.entity.*;
import twilightforest.entity.boss.*;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public final class TFSounds {

	public static final SoundEvent KOBOLD_DEATH = createEvent("mob.kobold.die");
	public static final SoundEvent KOBOLD_AMBIENT = createEvent("mob.kobold.kobold");
	public static final SoundEvent KOBOLD_HURT = createEvent("mob.kobold.hurt");
	public static final SoundEvent KOBOLD_PARROT = createEvent("mob.kobold.parrot");
	public static final SoundEvent CICADA = createEvent("mob.cicada");
	public static final SoundEvent NAGA_HISS = createEvent("mob.naga.hiss");
	public static final SoundEvent NAGA_HURT = createEvent("mob.naga.hurt");
	public static final SoundEvent NAGA_RATTLE = createEvent("mob.naga.rattle");
	public static final SoundEvent NAGA_PARROT = createEvent("mob.naga.parrot");
	public static final SoundEvent RAVEN_CAW = createEvent("mob.raven.caw");
	public static final SoundEvent RAVEN_SQUAWK = createEvent("mob.raven.squawk");
	public static final SoundEvent REDCAP_DEATH = createEvent("mob.redcap.die");
	public static final SoundEvent REDCAP_AMBIENT = createEvent("mob.redcap.redcap");
	public static final SoundEvent REDCAP_HURT = createEvent("mob.redcap.hurt");
	public static final SoundEvent REDCAP_PARROT = createEvent("mob.redcap.parrot");
	public static final SoundEvent TINYBIRD_CHIRP = createEvent("mob.tinybird.chirp");
	public static final SoundEvent TINYBIRD_HURT = createEvent("mob.tinybird.hurt");
	public static final SoundEvent TINYBIRD_SONG = createEvent("mob.tinybird.song");
	public static final SoundEvent URGHAST_TRAP_ACTIVE = createEvent("mob.urghast.trapactive");
	public static final SoundEvent URGHAST_TRAP_ON = createEvent("mob.urghast.trapon");
	public static final SoundEvent URGHAST_TRAP_SPINDOWN = createEvent("mob.urghast.trapspindown");
	public static final SoundEvent URGHAST_TRAP_WARMUP = createEvent("mob.urghast.trapwarmup");
	public static final SoundEvent WRAITH = createEvent("mob.wraith.wraith");
	public static final SoundEvent WRAITH_PARROT = createEvent("mob.wraith.parrot");
	public static final SoundEvent HYDRA_HURT = createEvent("mob.hydra.hurt");
	public static final SoundEvent HYDRA_DEATH = createEvent("mob.hydra.death");
	public static final SoundEvent HYDRA_GROWL = createEvent("mob.hydra.growl");
	public static final SoundEvent HYDRA_ROAR = createEvent("mob.hydra.roar");
	public static final SoundEvent HYDRA_WARN = createEvent("mob.hydra.warn");
	public static final SoundEvent HYDRA_PARROT = createEvent("mob.hydra.parrot");
	public static final SoundEvent MOSQUITO = createEvent("mob.mosquito.mosquito");
	public static final SoundEvent MOSQUITO_PARROT = createEvent("mob.mosquito.parrot");
	public static final SoundEvent ICE_AMBIENT = createEvent("mob.ice.noise");
	public static final SoundEvent ICE_HURT = createEvent("mob.ice.hurt");
	public static final SoundEvent ICE_DEATH = createEvent("mob.ice.death");
	public static final SoundEvent ICE_PARROT = createEvent("mob.ice.parrot");
	public static final SoundEvent MINOTAUR_PARROT = createEvent("mob.minotaur.parrot");
	public static final SoundEvent ALPHAYETI_ALERT = createEvent("mob.alphayeti.alert");
	public static final SoundEvent ALPHAYETI_DIE = createEvent("mob.alphayeti.die");
	public static final SoundEvent ALPHAYETI_GRAB = createEvent("mob.alphayeti.grab");
	public static final SoundEvent ALPHAYETI_GROWL = createEvent("mob.alphayeti.growl");
	public static final SoundEvent ALPHAYETI_HURT = createEvent("mob.alphayeti.hurt");
	public static final SoundEvent ALPHAYETI_PANT = createEvent("mob.alphayeti.pant");
	public static final SoundEvent ALPHAYETI_ROAR = createEvent("mob.alphayeti.roar");
	public static final SoundEvent ALPHAYETI_THROW = createEvent("mob.alphayeti.throw");
	public static final SoundEvent ALPHAYETI_PARROT = createEvent("mob.alphayeti.parrot");
	public static final SoundEvent DEER_DEATH = createEvent("mob.deer.death");
	public static final SoundEvent DEER_HURT = createEvent("mob.deer.hurt");
	public static final SoundEvent DEER_IDLE = createEvent("mob.deer.idle");
	public static final SoundEvent MISTWOLF_TARGET = createEvent("mob.mistwolf.target");
	public static final SoundEvent MISTWOLF_HURT = createEvent("mob.mistwolf.hurt");
	public static final SoundEvent MISTWOLF_IDLE = createEvent("mob.mistwolf.idle");
	public static final SoundEvent MISTWOLF_PARROT = createEvent("mob.mistwolf.parrot");
	public static final SoundEvent TOME_DEATH = createEvent("mob.tome.death");
	public static final SoundEvent TOME_HURT = createEvent("mob.tome.hurt");
	public static final SoundEvent TOME_IDLE = createEvent("mob.tome.idle");
	public static final SoundEvent TOME_PARROT = createEvent("mob.tome.parrot");
	public static final SoundEvent FINALBOSS_SPAWN = createEvent("mob.finalboss.spawn");
	public static final SoundEvent FINALBOSS_DEATH = createEvent("mob.finalboss.death");

	public static final SoundEvent SLIDER = createEvent("random.slider");

	public static final SoundEvent MUSIC = createEvent("music.bg");

	public static final SoundEvent BOSS_MUSIC_START = createEvent("music.boss.start");
	public static final SoundEvent BOSS_MUSIC_END = createEvent("music.boss.end");
	public static final SoundEvent BOSS_MUSIC_MAIN_1 = createEvent("music.boss.main1");
	public static final SoundEvent BOSS_MUSIC_MAIN_2 = createEvent("music.boss.main2");
	public static final SoundEvent BOSS_MUSIC_MAIN_3 = createEvent("music.boss.main3");
	public static final SoundEvent BOSS_MUSIC_MAIN_4 = createEvent("music.boss.main4");
	public static final SoundEvent BOSS_MUSIC_MAIN_5 = createEvent("music.boss.main5");
	public static final SoundEvent BOSS_MUSIC_MAIN_6 = createEvent("music.boss.main6");
	public static final SoundEvent BOSS_MUSIC_MAIN_7 = createEvent("music.boss.main7");
	public static final SoundEvent BOSS_MUSIC_MAIN_8 = createEvent("music.boss.main8");
	public static final SoundEvent BOSS_MUSIC_MAIN_9 = createEvent("music.boss.main9");
	public static final SoundEvent BOSS_MUSIC_MAIN_10 = createEvent("music.boss.main10");
	public static final SoundEvent BOSS_MUSIC_MAIN_11 = createEvent("music.boss.main11");
	public static final SoundEvent BOSS_MUSIC_MAIN_12 = createEvent("music.boss.main12");
	public static final SoundEvent BOSS_MUSIC_MAIN_13 = createEvent("music.boss.main13");
	public static final SoundEvent BOSS_MUSIC_MAIN_14 = createEvent("music.boss.main14");
	public static final SoundEvent BOSS_MUSIC_MAIN_15 = createEvent("music.boss.main15");

	private static SoundEvent createEvent(String sound) {
		ResourceLocation name = TwilightForestMod.prefix(sound);
		return new SoundEvent(name).setRegistryName(name);
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(
				KOBOLD_DEATH,
				KOBOLD_AMBIENT,
				KOBOLD_HURT,
				KOBOLD_PARROT,
				CICADA,
				NAGA_HISS,
				NAGA_HURT,
				NAGA_RATTLE,
				NAGA_PARROT,
				RAVEN_CAW,
				RAVEN_SQUAWK,
				REDCAP_DEATH,
				REDCAP_AMBIENT,
				REDCAP_HURT,
				REDCAP_PARROT,
				TINYBIRD_CHIRP,
				TINYBIRD_HURT,
				TINYBIRD_SONG,
				URGHAST_TRAP_ACTIVE,
				URGHAST_TRAP_ON,
				URGHAST_TRAP_SPINDOWN,
				URGHAST_TRAP_WARMUP,
				WRAITH,
				WRAITH_PARROT,
				HYDRA_DEATH,
				HYDRA_GROWL,
				HYDRA_HURT,
				HYDRA_ROAR,
				HYDRA_WARN,
				HYDRA_PARROT,
				MOSQUITO,
				MOSQUITO_PARROT,
				ICE_AMBIENT,
				ICE_DEATH,
				ICE_HURT,
				ICE_PARROT,
				MINOTAUR_PARROT,
				ALPHAYETI_ALERT,
				ALPHAYETI_DIE,
				ALPHAYETI_GRAB,
				ALPHAYETI_GROWL,
				ALPHAYETI_HURT,
				ALPHAYETI_PANT,
				ALPHAYETI_ROAR,
				ALPHAYETI_THROW,
				ALPHAYETI_PARROT,
				DEER_DEATH,
				DEER_HURT,
				DEER_IDLE,
				MISTWOLF_TARGET,
				MISTWOLF_HURT,
				MISTWOLF_IDLE,
				MISTWOLF_PARROT,
				TOME_DEATH,
				TOME_HURT,
				TOME_IDLE,
				TOME_PARROT,
				FINALBOSS_SPAWN,
				FINALBOSS_DEATH,
				SLIDER,
				MUSIC,
				BOSS_MUSIC_START,
				BOSS_MUSIC_END,
				BOSS_MUSIC_MAIN_1,
				BOSS_MUSIC_MAIN_2,
				BOSS_MUSIC_MAIN_3,
				BOSS_MUSIC_MAIN_4,
				BOSS_MUSIC_MAIN_5,
				BOSS_MUSIC_MAIN_6,
				BOSS_MUSIC_MAIN_7,
				BOSS_MUSIC_MAIN_8,
				BOSS_MUSIC_MAIN_9,
				BOSS_MUSIC_MAIN_10,
				BOSS_MUSIC_MAIN_11,
				BOSS_MUSIC_MAIN_12,
				BOSS_MUSIC_MAIN_13,
				BOSS_MUSIC_MAIN_14,
				BOSS_MUSIC_MAIN_15
		);

		registerParrotSounds();
	}

	private static void registerParrotSounds() {

		EntityParrot.registerMimicSound(EntityTFKobold.class, KOBOLD_PARROT);
		EntityParrot.registerMimicSound(EntityTFRedcap.class, REDCAP_PARROT);
		EntityParrot.registerMimicSound(EntityTFRedcapSapper.class, REDCAP_PARROT);
		EntityParrot.registerMimicSound(EntityTFBlockGoblin.class, REDCAP_PARROT);
		EntityParrot.registerMimicSound(EntityTFBoggard.class, REDCAP_PARROT);
		EntityParrot.registerMimicSound(EntityTFWraith.class, WRAITH_PARROT);
		EntityParrot.registerMimicSound(EntityTFMosquitoSwarm.class, MOSQUITO_PARROT);
		EntityParrot.registerMimicSound(EntityTFIceExploder.class, ICE_PARROT);
		EntityParrot.registerMimicSound(EntityTFIceShooter.class, ICE_PARROT);
		EntityParrot.registerMimicSound(EntityTFSnowGuardian.class, ICE_PARROT);
		EntityParrot.registerMimicSound(EntityTFLoyalZombie.class, SoundEvents.E_PARROT_IM_ZOMBIE);
		EntityParrot.registerMimicSound(EntityTFMinotaur.class, MINOTAUR_PARROT);
		EntityParrot.registerMimicSound(EntityTFKingSpider.class, SoundEvents.E_PARROT_IM_SPIDER);
		EntityParrot.registerMimicSound(EntityTFHedgeSpider.class, SoundEvents.E_PARROT_IM_SPIDER);
		EntityParrot.registerMimicSound(EntityTFSwarmSpider.class, SoundEvents.E_PARROT_IM_SPIDER);
		EntityParrot.registerMimicSound(EntityTFTowerBroodling.class, SoundEvents.E_PARROT_IM_SPIDER);
		EntityParrot.registerMimicSound(EntityTFHostileWolf.class, MISTWOLF_PARROT);
		EntityParrot.registerMimicSound(EntityTFWinterWolf.class, MISTWOLF_PARROT);
		EntityParrot.registerMimicSound(EntityTFSkeletonDruid.class, SoundEvents.E_PARROT_IM_SKELETON);
		EntityParrot.registerMimicSound(EntityTFTowerGhast.class, SoundEvents.E_PARROT_IM_GHAST);
		EntityParrot.registerMimicSound(EntityTFMiniGhast.class, SoundEvents.E_PARROT_IM_GHAST);
		EntityParrot.registerMimicSound(EntityTFTowerTermite.class, SoundEvents.E_PARROT_IM_SILVERFISH);
		EntityParrot.registerMimicSound(EntityTFDeathTome.class, TOME_PARROT);

		EntityParrot.registerMimicSound(EntityTFHydra.class, HYDRA_PARROT);
		EntityParrot.registerMimicSound(EntityTFKnightPhantom.class, WRAITH_PARROT);
		EntityParrot.registerMimicSound(EntityTFLich.class, SoundEvents.E_PARROT_IM_BLAZE);
		EntityParrot.registerMimicSound(EntityTFMinoshroom.class, MINOTAUR_PARROT);
		EntityParrot.registerMimicSound(EntityTFNaga.class, NAGA_PARROT);
		EntityParrot.registerMimicSound(EntityTFSnowQueen.class, ICE_PARROT);
		EntityParrot.registerMimicSound(EntityTFUrGhast.class, SoundEvents.E_PARROT_IM_GHAST);
		EntityParrot.registerMimicSound(EntityTFYetiAlpha.class, ALPHAYETI_PARROT);
	}

	private TFSounds() {}

}
