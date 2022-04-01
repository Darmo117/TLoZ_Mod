package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class ShockSwitchBlock extends Block implements IModBlock {
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  public static final BooleanProperty MANUAL_SWITCH_OFF = BooleanProperty.create("manual_switch_off");

  public ShockSwitchBlock() {
    super(Properties.create(Material.GLASS)
        .sound(SoundType.GLASS)
        .hardnessAndResistance(-1)
        .setAllowsSpawn((blockState, blockReader, pos, entityType) -> false)
        .setOpaque((blockState, blockReader, pos) -> false));
    this.setDefaultState(this.getStateContainer().getBaseState()
        .with(POWERED, false)
        .with(MANUAL_SWITCH_OFF, false));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return makeCuboidShape(1, 0, 1, 15, 15, 15);
  }

  // Allow right-click for ease of use
  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    return this.toggleState(state, world, pos) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
  }

  // Toggle when hit by player
  @SuppressWarnings("deprecation")
  @Override
  public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
    this.toggleState(state, world, pos);
  }

  // Toggle when hit by bomb
  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (entity instanceof BombEntity) {
      if (!world.isRemote) {
        this.toggleState(state, world, pos);
      }
      ((BombEntity) entity).explode();
    }
  }

  // Toggle when hit by arrow
  @SuppressWarnings("deprecation")
  @Override
  public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
    this.toggleState(state, world, hit.getPos());
  }

  // Reset if a barrier block is above
  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
    if (!world.isRemote && world.getBlockState(pos).matchesBlock(this)
        && world.getBlockState(pos.up()).getBlock() == Blocks.BARRIER && state.get(POWERED)) {
      this.toggleState(state, world, pos, true);
    }
  }

  public boolean toggleState(BlockState state, World world, final BlockPos pos) {
    return this.toggleState(state, world, pos, false);
  }

  public boolean toggleState(BlockState state, World world, final BlockPos pos, final boolean force) {
    boolean powered = state.get(POWERED);
    boolean manualSwitchOff = state.get(MANUAL_SWITCH_OFF);
    if (!force && powered && !manualSwitchOff) {
      return false;
    }

    powered = !powered;
    SoundEvent sound = powered ? SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;
    float pitch = powered ? 0.6F : 0.5F;
    world.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.3F, pitch);
    world.setBlockState(pos, state.with(POWERED, powered), 3);
    this.updateNeighbors(world, pos);
    return true;
  }

  private void updateNeighbors(World world, BlockPos pos) {
    world.notifyNeighborsOfStateChange(pos, this);
    world.notifyNeighborsOfStateChange(pos.down(), this);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
    if (!isMoving && !state.matchesBlock(newState.getBlock())) {
      if (state.get(POWERED)) {
        this.updateNeighbors(world, pos);
      }
      //noinspection ConstantConditions
      super.onReplaced(state, world, pos, newState, isMoving);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    return blockState.get(POWERED) ? 15 : 0;
  }

  @SuppressWarnings("deprecation")
  @Override
  public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    return blockState.get(POWERED) && side == Direction.UP ? 15 : 0; // Powers bottom block
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean canProvidePower(BlockState state) {
    return true;
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(POWERED, MANUAL_SWITCH_OFF);
  }
}
