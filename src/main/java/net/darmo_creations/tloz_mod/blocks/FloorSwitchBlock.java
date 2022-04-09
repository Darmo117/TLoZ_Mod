package net.darmo_creations.tloz_mod.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FloorSwitchBlock extends SwitchBlock {
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  private static final VoxelShape SHAPE = makeCuboidShape(1, 0, 1, 15, 1, 15);
  private static final AxisAlignedBB PRESSURE_AABB = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.25, 0.875);

  public static final List<Class<? extends Entity>> ALLOWED_ENTITIES = new ArrayList<>();

  static {
    ALLOWED_ENTITIES.add(PlayerEntity.class);
    // TODO add possessed phantom
  }

  public FloorSwitchBlock() {
    super(Properties.create(Material.ROCK));
    this.setDefaultState(this.getDefaultState().with(MANUAL_SWITCH_OFF, true));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @Override
  public boolean canSpawnInBlock() {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
    if (!world.isRemote && state.get(POWERED)) {
      if (!this.shouldBePressed(world, pos)) {
        this.updateState(world, pos, state);
      } else {
        world.getPendingBlockTicks().scheduleTick(pos, this, 1);
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (!world.isRemote && !state.get(POWERED) && this.shouldBePressed(world, pos)) {
      this.updateState(world, pos, state);
    }
  }

  private boolean shouldBePressed(World world, BlockPos pos) {
    AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(pos);
    List<? extends Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
    return list.stream().anyMatch(e -> ALLOWED_ENTITIES.stream().anyMatch(c -> c.isAssignableFrom(e.getClass())));
  }

  protected void updateState(World world, BlockPos pos, BlockState state) {
    this.toggleState(state, world, pos);
    if (state.get(MANUAL_SWITCH_OFF)) {
      world.getPendingBlockTicks().scheduleTick(pos, this, 1);
    }
  }

  @Override
  protected Direction getStrongPowerDirection(BlockState blockState) {
    return Direction.DOWN;
  }
}
