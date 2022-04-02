package net.darmo_creations.tloz_mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Map;

public class SpikesBaseBlock extends Block {
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  public static final BooleanProperty NORTH = SixWayBlock.NORTH;
  public static final BooleanProperty EAST = SixWayBlock.EAST;
  public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
  public static final BooleanProperty WEST = SixWayBlock.WEST;
  protected static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = SixWayBlock.FACING_TO_PROPERTY_MAP.entrySet().stream()
      .filter(facingProperty -> facingProperty.getKey().getAxis().isHorizontal())
      .collect(Util.toMapCollector());

  public SpikesBaseBlock() {
    super(Properties.create(Material.ROCK));
    this.setDefaultState(this.getStateContainer().getBaseState()
        .with(POWERED, false)
        .with(NORTH, false)
        .with(SOUTH, false)
        .with(WEST, false)
        .with(EAST, false)
    );
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    IBlockReader world = context.getWorld();
    BlockPos pos = context.getPos();
    BlockPos north = pos.north();
    BlockPos south = pos.south();
    BlockPos west = pos.west();
    BlockPos east = pos.east();
    BlockState nState = world.getBlockState(north);
    BlockState sState = world.getBlockState(south);
    BlockState wState = world.getBlockState(west);
    BlockState eState = world.getBlockState(east);
    return this.getDefaultState()
        .with(NORTH, canAttachTo(nState))
        .with(SOUTH, canAttachTo(sState))
        .with(WEST, canAttachTo(wState))
        .with(EAST, canAttachTo(eState));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    return facing.getAxis().isHorizontal()
        ? state.with(FACING_TO_PROPERTY_MAP.get(facing), canAttachTo(facingState))
        : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
  }

  @Override
  public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
    this.updatePowerState(world, pos, state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
    this.updatePowerState(world, pos, state);
  }

  private boolean isPowered(World world, BlockPos pos) {
    for (Direction direction : Direction.values()) {
      if (world.isSidePowered(pos.offset(direction), direction)) {
        return true;
      }
    }
    return false;
  }

  private void updatePowerState(World world, BlockPos pos, BlockState state) {
    boolean powered = this.isPowered(world, pos);
    if (powered != state.get(POWERED)) {
      world.setBlockState(pos, state.with(POWERED, powered), 2);
      BlockPos up = pos.up();
      if (powered && world.isAirBlock(up)) {
        world.setBlockState(up, ModBlocks.SPIKES.getDefaultState(), 2);
        world.playSound(null, up, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 1, 1);
      } else if (!powered && world.getBlockState(up).getBlock() == ModBlocks.SPIKES) {
        world.setBlockState(up, Blocks.AIR.getDefaultState(), 2);
        world.playSound(null, up, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 1, 1);
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, Rotation rot) {
    switch (rot) {
      case CLOCKWISE_180:
        return state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
      case COUNTERCLOCKWISE_90:
        return state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
      case CLOCKWISE_90:
        return state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
      default:
        return state;
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState mirror(BlockState state, Mirror mirror) {
    switch (mirror) {
      case LEFT_RIGHT:
        return state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
      case FRONT_BACK:
        return state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
      default:
        return super.mirror(state, mirror);
    }
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(POWERED, NORTH, EAST, WEST, SOUTH);
  }

  public static boolean canAttachTo(final BlockState state) {
    return state.getBlock() instanceof SpikesBaseBlock;
  }
}
