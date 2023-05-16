package twilightforest.tileentity.spawner;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TileEntityTFFinalBossSpawner extends TileEntityTFBossSpawner {

    public TileEntityTFFinalBossSpawner() {
        super(EntityList.getKey(EntityWroughtnaut.class));
    }

    @Override
    protected void initializeCreature(EntityLiving myCreature) {
        super.initializeCreature(myCreature);

        // set movement speed
        double movementSpeed = myCreature.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
        myCreature.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(movementSpeed * 1.5);

        // set glowing
        myCreature.setGlowing(true);
    }

    @Override
    protected boolean spawnMyBoss() {
        // create creature
        EntityLiving myCreature = makeMyCreature();
        myCreature.moveToBlockPosAndAngles(pos, 180.0F, 0.0F);
        myCreature.onInitialSpawn(world.getDifficultyForLocation(pos), null);

        // set creature's home to this
        initializeCreature(myCreature);

        // create lightning
        world.addWeatherEffect(new EntityLightningBolt(world, getPos().getX() + world.rand.nextGaussian(), getPos().getY(), getPos().getZ() + world.rand.nextGaussian(), true));
        world.addWeatherEffect(new EntityLightningBolt(world, getPos().getX() + world.rand.nextGaussian(), getPos().getY(), getPos().getZ() + world.rand.nextGaussian(), true));

        // play sound
        world.playSound(null, myCreature.getPosition(), TFSounds.FINALBOSS_SPAWN, SoundCategory.HOSTILE, 1.0F, 1.0F);

        // spawn it
        return world.spawnEntity(myCreature);
    }

    @Override
    protected int getRange() {
        return 30;
    }
}
