package twilightforest.entity.boss;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import twilightforest.entity.EntityTFThrowable;
import twilightforest.entity.EntityTFYeti;
import twilightforest.potions.TFPotions;

import java.util.List;

public class EntityTFIceBomb extends EntityTFThrowable {
    private int zoneTimer = 101;
    private boolean hasHit;

    public EntityTFIceBomb(World world) {
        super(world);
    }

    public EntityTFIceBomb(World world, EntityLivingBase thrower) {
        super(world, thrower);
    }

    @Override
    protected void onImpact(RayTraceResult ray) {
        this.motionY = 0;
        this.hasHit = true;

        if (!world.isRemote)
            this.doTerrainEffects();
    }

    private void doTerrainEffects() {

        final int range = 2;

        int ix = MathHelper.floor(this.lastTickPosX);
        int iy = MathHelper.floor(this.lastTickPosY);
        int iz = MathHelper.floor(this.lastTickPosZ);

        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                if (Math.abs(x) == range && Math.abs(z) == range) continue;
                for (int y = -range; y <= range; y++) {
                    this.doTerrainEffect(new BlockPos(ix + x, iy + y, iz + z));
                }
            }
        }
    }

    /**
     * Freeze water, put snow on snowable surfaces
     */
    private void doTerrainEffect(BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getMaterial() == Material.WATER) {
            this.world.setBlockState(pos, Blocks.ICE.getDefaultState());
        }
        if (state.getMaterial() == Material.LAVA) {
            this.world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
        }
        if (this.world.isAirBlock(pos) && Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, pos)) {
            this.world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.hasHit) {
            this.motionX *= 0.1D;
            this.motionY *= 0.1D;
            this.motionZ *= 0.1D;

            this.zoneTimer--;
            makeIceZone();

            if (!world.isRemote && this.zoneTimer <= 0) {
                world.playEvent(2001, new BlockPos(this), Block.getStateId(Blocks.ICE.getDefaultState()));
                setDead();
            }
        } else {
            makeTrail();
        }
    }

    public void makeTrail() {
        int stateId = Block.getStateId(Blocks.SNOW.getDefaultState());
        for (int i = 0; i < 10; i++) {
            double dx = posX + 0.5D * (rand.nextDouble() - rand.nextDouble());
            double dy = posY + 0.5D * (rand.nextDouble() - rand.nextDouble()) + 0.5D;
            double dz = posZ + 0.5D * (rand.nextDouble() - rand.nextDouble());

            world.spawnParticle(EnumParticleTypes.FALLING_DUST, dx, dy, dz, -motionX, -motionY, -motionZ, stateId);
        }
    }

    private void makeIceZone() {
        if (this.world.isRemote) {
            // sparkles
            int stateId = Block.getStateId(Blocks.SNOW.getDefaultState());
            for (int i = 0; i < 20; i++) {
                double dx = this.posX + (rand.nextFloat() - rand.nextFloat()) * 3.5F;
                double dy = this.posY + (rand.nextFloat() - rand.nextFloat()) * 3.5F;
                double dz = this.posZ + (rand.nextFloat() - rand.nextFloat()) * 3.5F;

                world.spawnParticle(EnumParticleTypes.FALLING_DUST, dx, dy, dz, 0, 0, 0, stateId);
            }
        } else {
            if (this.zoneTimer == 99) this.doTerrainEffects();
            if (this.zoneTimer % 20 == 0) {
                hitNearbyEntities();
            }
        }
    }

    private void hitNearbyEntities() {
        List<EntityLivingBase> nearby = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(3, 2, 3));

        for (EntityLivingBase entity : nearby) {
            if (entity != this.getThrower()) {
                if (entity instanceof EntityTFYeti) {
                    // TODO: make "frozen yeti" entity?
                    BlockPos pos = new BlockPos(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);
                    world.setBlockState(pos, Blocks.ICE.getDefaultState());
                    world.setBlockState(pos.up(), Blocks.ICE.getDefaultState());

                    entity.setDead();
                } else {
                    entity.attackEntityFrom(DamageSource.MAGIC, 1);
                    entity.addPotionEffect(new PotionEffect(TFPotions.frosty, 20 * 5, 2));
                }
            }
        }
    }

    public IBlockState getBlock() {
        return Blocks.PACKED_ICE.getDefaultState();
    }

    @Override
    protected float getGravityVelocity() {
        return this.hasHit ? 0F : 0.025F;
    }
}
