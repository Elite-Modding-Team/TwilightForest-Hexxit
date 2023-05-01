package twilightforest.mixins.mowziesmobs;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.spawn.SpawnHandler;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.TwilightForestMod;

@Mixin(value = MowziesMobs.class, remap = false)
public class MowziesMobsMainMixin {
    @Redirect(method = "init(Lnet/minecraftforge/fml/common/event/FMLPreInitializationEvent;)V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/registry/GameRegistry;registerWorldGenerator(Lnet/minecraftforge/fml/common/IWorldGenerator;I)V"))
    public void voidWorldGen(IWorldGenerator generator, int modGenerationWeight) {
        TwilightForestMod.LOGGER.info("Disabling Mowzie's Mobs world generation...");
    }

    @Redirect(method = "init(Lnet/minecraftforge/fml/common/event/FMLPreInitializationEvent;)V", at = @At(value = "INVOKE", target = "Lcom/bobmowzie/mowziesmobs/server/spawn/SpawnHandler;registerSpawnPlacementTypes()V"))
    public void voidSpawnPlacementTypes(SpawnHandler instance) {
        TwilightForestMod.LOGGER.info("Disabling Mowzie's Mobs spawn placement types...");
    }

    @Redirect(method = "init(Lnet/minecraftforge/fml/common/event/FMLPostInitializationEvent;)V", at = @At(value = "INVOKE", target = "Lcom/bobmowzie/mowziesmobs/server/spawn/SpawnHandler;registerSpawns()V"))
    public void voidSpawns(SpawnHandler instance) {
        TwilightForestMod.LOGGER.info("Disabling Mowzie's Mobs spawns...");
    }
}
