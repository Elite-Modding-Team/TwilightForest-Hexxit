package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.enums.FireJetVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;
import twilightforest.tileentity.TileEntityTFFlameJet;
import twilightforest.tileentity.TileEntityTFPoppingJet;
import twilightforest.tileentity.TileEntityTFSmoker;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

public class BlockTFFireJet extends Block implements ModelRegisterCallback {

	public static final IProperty<FireJetVariant> VARIANT = PropertyEnum.create("variant", FireJetVariant.class);
	private static final Set<FireJetVariant> ENCASED = EnumSet.of(FireJetVariant.ENCASED_JET_IDLE, FireJetVariant.ENCASED_JET_FLAME, FireJetVariant.ENCASED_SMOKER_OFF, FireJetVariant.ENCASED_SMOKER_ON, FireJetVariant.ENCASED_JET_POPPING);

	protected BlockTFFireJet() {
		super(Material.ROCK);
		this.setHardness(1.5F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(TFItems.creativeTab);
		this.setTickRandomly(true);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		final FireJetVariant[] values = FireJetVariant.values();
		final FireJetVariant variant = values[meta % values.length];

		return getDefaultState().withProperty(VARIANT, variant);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).ordinal();
	}

	@Override
	public int damageDropped(IBlockState state) {
		switch (state.getValue(VARIANT)) {
			case ENCASED_SMOKER_ON:
				state = state.withProperty(VARIANT, FireJetVariant.ENCASED_SMOKER_OFF);
				break;
			case ENCASED_JET_POPPING:
				state = state.withProperty(VARIANT, FireJetVariant.ENCASED_JET_IDLE);
				break;
			case ENCASED_JET_FLAME:
				state = state.withProperty(VARIANT, FireJetVariant.ENCASED_JET_IDLE);
				break;
			case JET_POPPING:
				state = state.withProperty(VARIANT, FireJetVariant.JET_IDLE);
				break;
			case JET_FLAME:
				state = state.withProperty(VARIANT, FireJetVariant.JET_IDLE);
				break;
			default:
				break;
		}

		return getMetaFromState(state);
	}

	@Override
	public Material getMaterial(IBlockState state) {
		return ENCASED.contains(state.getValue(VARIANT)) ? Material.WOOD : Material.ROCK;
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
		return ENCASED.contains(state.getValue(VARIANT)) ? MapColor.SAND : MapColor.GRASS;
	}

	@Override
	public int getLightValue(IBlockState state) {
		switch (state.getValue(VARIANT)) {
			case JET_FLAME:
			case ENCASED_JET_FLAME:
				return 15;
			case SMOKER:
			default:
				return 0;
		}
	}

	@Nullable
	@Override
	public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos) {
		switch (state.getValue(VARIANT)) {
			case JET_POPPING:
			case ENCASED_JET_POPPING:
				return PathNodeType.DANGER_FIRE;
			case JET_FLAME:
			case ENCASED_JET_FLAME:
				return PathNodeType.DAMAGE_FIRE;
			default:
				return null;
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
		if (!world.isRemote && state.getValue(VARIANT) == FireJetVariant.JET_IDLE) {
			BlockPos lavaPos = findLavaAround(world, pos.down());

			if (isLava(world, lavaPos)) {
				world.setBlockState(lavaPos, Blocks.AIR.getDefaultState());
				world.setBlockState(pos, state.withProperty(VARIANT, FireJetVariant.JET_POPPING), 2);
			}
		}
	}

	@Override
	@Deprecated
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block myBlockID, BlockPos fromPos) {

		if (world.isRemote) return;

		FireJetVariant variant = state.getValue(VARIANT);
		boolean powered = world.isBlockPowered(pos);

		if (variant == FireJetVariant.ENCASED_SMOKER_OFF && powered) {
			world.setBlockState(pos, state.withProperty(VARIANT, FireJetVariant.ENCASED_SMOKER_ON), 3);
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}

		if (variant == FireJetVariant.ENCASED_SMOKER_ON && !powered) {
			world.setBlockState(pos, state.withProperty(VARIANT, FireJetVariant.ENCASED_SMOKER_OFF), 3);
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}

		if (variant == FireJetVariant.ENCASED_JET_IDLE && powered) {
			world.setBlockState(pos, state.withProperty(VARIANT, FireJetVariant.ENCASED_JET_POPPING), 3);
			world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.6F);
		}
	}

	/**
	 * Find a full block of lava near the designated block.  This is intentionally not really that reliable.
	 */
	private BlockPos findLavaAround(World world, BlockPos pos) {
		if (isLava(world, pos)) {
			return pos;
		}

		for (int i = 0; i < 3; i++) {
			BlockPos randPos = pos.add(world.rand.nextInt(3) - 1, 0, world.rand.nextInt(3) - 1);
			if (isLava(world, randPos)) {
				return randPos;
			}
		}

		return pos;
	}

	private boolean isLava(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block b = state.getBlock();
		IProperty<Integer> levelProp = b instanceof BlockLiquid || b instanceof BlockFluidBase
				? BlockLiquid.LEVEL
				: null;
		return state.getMaterial() == Material.LAVA
				&& (levelProp == null || state.getValue(levelProp) == 0);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		switch (state.getValue(VARIANT)) {
			case SMOKER:
			case JET_POPPING:
			case JET_FLAME:
			case ENCASED_SMOKER_ON:
			case ENCASED_JET_POPPING:
			case ENCASED_JET_FLAME:
				return true;
			default:
				return false;
		}
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		switch (state.getValue(VARIANT)) {
			case SMOKER:
			case ENCASED_SMOKER_ON:
				return new TileEntityTFSmoker();
			case JET_POPPING:
				return new TileEntityTFPoppingJet(FireJetVariant.JET_FLAME);
			case JET_FLAME:
				return new TileEntityTFFlameJet(FireJetVariant.JET_IDLE);
			case ENCASED_JET_POPPING:
				return new TileEntityTFPoppingJet(FireJetVariant.ENCASED_JET_FLAME);
			case ENCASED_JET_FLAME:
				return new TileEntityTFFlameJet(FireJetVariant.ENCASED_JET_IDLE);
			default:
				return null;
		}
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
		list.add(new ItemStack(this, 1, FireJetVariant.SMOKER.ordinal()));
		list.add(new ItemStack(this, 1, FireJetVariant.JET_IDLE.ordinal()));
		list.add(new ItemStack(this, 1, FireJetVariant.ENCASED_SMOKER_OFF.ordinal()));
		list.add(new ItemStack(this, 1, FireJetVariant.ENCASED_JET_IDLE.ordinal()));
	}
	
	@Override
	public boolean isFireSource(@Nonnull World world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		FireJetVariant[] variants = {FireJetVariant.SMOKER, FireJetVariant.JET_IDLE, FireJetVariant.ENCASED_SMOKER_OFF, FireJetVariant.ENCASED_JET_IDLE};
		for (FireJetVariant variant : variants) {
			ModelUtils.registerToState(this, variant.ordinal(), getDefaultState().withProperty(VARIANT, variant));
		}
	}
}
