package twilightforest.tileentity.spawner;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TileEntityTFFinalBossSpawner extends TileEntityTFBossSpawner {

    public TileEntityTFFinalBossSpawner() {
        super(EntityList.getKey(EntityWroughtnaut.class));
    }

    @Override
    public boolean anyPlayerInRange() {
        for (int i = 0; i < world.playerEntities.size(); i++) {
            EntityPlayer player = world.playerEntities.get(i);
            if (player.posY > pos.getY() - 2 && EntitySelectors.NOT_SPECTATING.apply(player)) {
                double distance = player.getDistanceSq(pos.getX(), pos.getY(), pos.getZ());
                if (getRange() < 0.0D || distance < getRange() * getRange()) return true;
            }
        }
        return false;
    }

    @Override
    public void update() {
        if (spawnedBoss || world.getDifficulty() == EnumDifficulty.PEACEFUL || !anyPlayerInRange()) {
            return;
        }

        if (world.isRemote) {
            // spawn particles
            double rx = pos.getX() + world.rand.nextFloat();
            double ry = pos.getY() + world.rand.nextFloat();
            double rz = pos.getZ() + world.rand.nextFloat();
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, rx, ry, rz, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME, rx, ry, rz, 0.0D, 0.0D, 0.0D);
        } else {
            if (spawnMyBoss()) {
                world.destroyBlock(pos, false);
                spawnedBoss = true;

                // create lightning
                world.addWeatherEffect(new EntityLightningBolt(world, getPos().getX() + world.rand.nextGaussian(), getPos().getY(), getPos().getZ() + world.rand.nextGaussian(), true));
                world.addWeatherEffect(new EntityLightningBolt(world, getPos().getX() + world.rand.nextGaussian(), getPos().getY(), getPos().getZ() + world.rand.nextGaussian(), true));
            }
        }
    }

    @Override
    protected boolean spawnMyBoss() {
        // create creature
        EntityLiving myCreature = makeMyCreature();
        myCreature.moveToBlockPosAndAngles(pos, 180.0F, 0.0F);
        myCreature.onInitialSpawn(world.getDifficultyForLocation(pos), null);

        // set creature's home to this
        initializeCreature(myCreature);

        // spawn it
        return world.spawnEntity(myCreature);
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
    protected int getRange() {
        return 30;
    }
}
