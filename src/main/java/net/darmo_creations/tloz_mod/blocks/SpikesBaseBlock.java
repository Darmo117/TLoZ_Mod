package net.darmo_creations.tloz_mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SpikesBaseBlock extends Block implements IModBlock {
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public SpikesBaseBlock() {
    super(Properties.create(Material.ROCK));
    this.setDefaultState(this.getStateContainer().getBaseState()
        .with(POWERED, false));
  }

  @Override
  public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
    this.updateState(world, pos, state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
    this.updateState(world, pos, state);
  }

  private boolean isPowered(World world, BlockPos pos) {
    for (Direction direction : Direction.values()) {
      if (world.isSidePowered(pos.offset(direction), direction)) {
        return true;
      }
    }
    return false;
  }

  private void updateState(World world, BlockPos pos, BlockState state) {
    boolean powered = this.isPowered(world, pos);
    if (powered != state.get(POWERED)) {
      world.setBlockState(pos, state.with(POWERED, powered), 2);
      if (powered && world.isAirBlock(pos.up())) {
        world.setBlockState(pos.up(), ModBlocks.SPIKES.getDefaultState(), 2);
      } else if (!powered && world.getBlockState(pos.up()).getBlock() == ModBlocks.SPIKES) {
        world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2);
      }
    }
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(POWERED);
  }
}
