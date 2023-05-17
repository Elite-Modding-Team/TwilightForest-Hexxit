package twilightforest.tileentity.spawner;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TileEntityTFFinalBossSpawner extends TileEntityTFBossSpawner {

    public TileEntityTFFinalBossSpawner() {
        super(EntityList.getKey(EntityWroughtnaut.class));
    }

    @Override
    public void update() {
        if (spawnedBoss || !anyPlayerInRange()) {
            return;
        }

        // compute affected area
        AxisAlignedBB boundingBox = new AxisAlignedBB(pos).grow(getRange());

        if (world.isRemote) {
            // spawn particles
            double rx = pos.getX() + world.rand.nextFloat();
            double ry = pos.getY() + world.rand.nextFloat();
            double rz = pos.getZ() + world.rand.nextFloat();
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, rx, ry, rz, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME, rx, ry, rz, 0.0D, 0.0D, 0.0D);

            // play sound
            world.getEntitiesWithinAABB(EntityPlayer.class, boundingBox).forEach(player -> {
                player.playSound(TFSounds.FINALBOSS_SPAWN, 1.0F, 1.0F);
            });
        } else {
            if (world.getDifficulty() != EnumDifficulty.PEACEFUL) {
                if (spawnMyBoss()) {
                    world.destroyBlock(pos, false);
                    spawnedBoss = true;

                    // create lightning
                    world.addWeatherEffect(new EntityLightningBolt(world, getPos().getX() + world.rand.nextGaussian(), getPos().getY(), getPos().getZ() + world.rand.nextGaussian(), true));
                    world.addWeatherEffect(new EntityLightningBolt(world, getPos().getX() + world.rand.nextGaussian(), getPos().getY(), getPos().getZ() + world.rand.nextGaussian(), true));

                    // display title
                    world.getEntitiesWithinAABB(EntityPlayerMP.class, boundingBox).forEach(player -> {
                        SPacketTitle title = new SPacketTitle(SPacketTitle.Type.TITLE, new TextComponentTranslation("twilightforest.title.finalboss.spawn").setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
                        SPacketTitle subtitle = new SPacketTitle(SPacketTitle.Type.SUBTITLE, new TextComponentTranslation("twilightforest.subtitle.finalboss.spawn").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)));
                        player.connection.sendPacket(title);
                        player.connection.sendPacket(subtitle);
                    });
                }
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
        return 28;
    }
}
