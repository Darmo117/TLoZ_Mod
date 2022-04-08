package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.UpdateFlags;
import net.darmo_creations.tloz_mod.items.BossKeyEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;

public class BossDoorBlock extends Block {
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

  private static final VoxelShape NORTH_LOCKED = makeCuboidShape(1, 0, 1, 16, 32, 16);
  private static final VoxelShape EAST_LOCKED = makeCuboidShape(0, 0, 1, 15, 32, 16);
  private static final VoxelShape SOUTH_LOCKED = makeCuboidShape(0, 0, 0, 15, 32, 15);
  private static final VoxelShape WEST_LOCKED = makeCuboidShape(1, 0, 0, 16, 32, 15);

  private static final VoxelShape NORTH_UNLOCKED = makeCuboidShape(1, 0, 1, 16, 16, 16);
  private static final VoxelShape EAST_UNLOCKED = makeCuboidShape(0, 0, 1, 15, 16, 16);
  private static final VoxelShape SOUTH_UNLOCKED = makeCuboidShape(0, 0, 0, 15, 16, 15);
  private static final VoxelShape WEST_UNLOCKED = makeCuboidShape(1, 0, 0, 16, 16, 15);

  public BossDoorBlock() {
    super(Properties.create(Material.ROCK));
    this.setDefaultState(this.getStateContainer().getBaseState()
        .with(FACING, Direction.NORTH)
        .with(LOCKED, true));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    boolean locked = state.get(LOCKED);
    switch (state.get(FACING)) {
      default:
      case NORTH:
        return locked ? NORTH_LOCKED : NORTH_UNLOCKED;
      case SOUTH:
        return locked ? SOUTH_LOCKED : SOUTH_UNLOCKED;
      case WEST:
        return locked ? WEST_LOCKED : WEST_UNLOCKED;
      case EAST:
        return locked ? EAST_LOCKED : EAST_UNLOCKED;
    }
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
  }

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {
    this.activate(world.getBlockState(pos), world, pos, entity);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    this.activate(state, world, pos, entity);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
    if (!state.get(LOCKED)) {
      return;
    }
    Pair<BlockPos, BlockPos> neighborsPos = this.getNeighborsPositions(world, pos);
    BlockState state1 = world.getBlockState(neighborsPos.getLeft());
    BlockState state2 = world.getBlockState(neighborsPos.getRight());
    if (state1.getBlock() == this && state2.getBlock() == this && (!state1.get(LOCKED) || !state2.get(LOCKED))) {
      world.setBlockState(pos, state.with(LOCKED, false),
          UpdateFlags.UPDATE_BLOCK | UpdateFlags.SEND_TO_CLIENT);
    }
  }

  private void activate(BlockState state, World world, BlockPos pos, Entity entity) {
    if (!world.isRemote && state.get(LOCKED) && entity instanceof BossKeyEntity) {
      entity.remove();
      world.setBlockState(pos, state.with(LOCKED, false),
          UpdateFlags.UPDATE_BLOCK | UpdateFlags.SEND_TO_CLIENT);
      Pair<BlockPos, BlockPos> neighborsPos = this.getNeighborsPositions(world, pos);
      BlockState state1 = world.getBlockState(neighborsPos.getLeft());
      BlockState state2 = world.getBlockState(neighborsPos.getRight());
      if (state1.getBlock() == this && state2.getBlock() == this) {
        if (state1.get(LOCKED)) {
          world.setBlockState(neighborsPos.getRight(), state1.with(LOCKED, false),
              UpdateFlags.UPDATE_BLOCK | UpdateFlags.SEND_TO_CLIENT);
        }
        if (state2.get(LOCKED)) {
          world.setBlockState(neighborsPos.getLeft(), state2.with(LOCKED, false),
              UpdateFlags.UPDATE_BLOCK | UpdateFlags.SEND_TO_CLIENT);
        }
      }
    }
  }

  private Pair<BlockPos, BlockPos> getNeighborsPositions(World world, BlockPos pos) {
    switch (world.getBlockState(pos).get(FACING)) {
      case NORTH:
        return new ImmutablePair<>(pos.east(), pos.south());
      case EAST:
        return new ImmutablePair<>(pos.west(), pos.south());
      case SOUTH:
        return new ImmutablePair<>(pos.west(), pos.north());
      case WEST:
        return new ImmutablePair<>(pos.east(), pos.north());
      default:
        throw new RuntimeException("invalid value for FACING property");
    }
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FACING, LOCKED);
  }
}
