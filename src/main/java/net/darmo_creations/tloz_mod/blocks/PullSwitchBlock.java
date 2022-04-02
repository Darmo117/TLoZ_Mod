package net.darmo_creations.tloz_mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class PullSwitchBlock extends SwitchBlock {
  public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

  private static final VoxelShape NORTH_OFF = makeCuboidShape(2, 3, 8, 14, 13, 16);
  private static final VoxelShape NORTH_ON = makeCuboidShape(2, 3, 4, 14, 13, 16);
  private static final VoxelShape SOUTH_OFF = makeCuboidShape(2, 3, 0, 14, 13, 8);
  private static final VoxelShape SOUTH_ON = makeCuboidShape(2, 3, 0, 14, 13, 12);
  private static final VoxelShape EAST_OFF = makeCuboidShape(0, 3, 2, 8, 13, 14);
  private static final VoxelShape EAST_ON = makeCuboidShape(0, 3, 2, 12, 13, 14);
  private static final VoxelShape WEST_OFF = makeCuboidShape(8, 3, 2, 16, 13, 14);
  private static final VoxelShape WEST_ON = makeCuboidShape(4, 3, 2, 16, 13, 14);

  public PullSwitchBlock() {
    super(Properties.create(Material.MISCELLANEOUS));
    this.setDefaultState(this.getStateContainer().getBaseState()
        .with(POWERED, false)
        .with(MANUAL_SWITCH_OFF, false)
        .with(HORIZONTAL_FACING, Direction.NORTH));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    boolean on = state.get(POWERED);
    switch (state.get(HORIZONTAL_FACING)) {
      case NORTH:
        return on ? NORTH_ON : NORTH_OFF;
      case SOUTH:
        return on ? SOUTH_ON : SOUTH_OFF;
      case WEST:
        return on ? WEST_ON : WEST_OFF;
      case EAST:
        return on ? EAST_ON : EAST_OFF;
    }
    return VoxelShapes.empty();
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    Direction face = context.getFace();
    if (face.getAxis() == Direction.Axis.Y) {
      return Blocks.AIR.getDefaultState();
    }
    return this.getDefaultState().with(HORIZONTAL_FACING, face);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(POWERED, MANUAL_SWITCH_OFF, HORIZONTAL_FACING);
  }

  @Override
  protected Direction getStrongPowerDirection(BlockState blockState) {
    return blockState.get(HORIZONTAL_FACING).getOpposite();
  }
}
