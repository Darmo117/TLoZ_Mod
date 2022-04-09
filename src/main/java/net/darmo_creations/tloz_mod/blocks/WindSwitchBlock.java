package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.WhirlwindEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class WindSwitchBlock extends SwitchBlock {
  public static final int POWERED_TIME = 20; // 1s

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  private static final VoxelShape SHAPE = makeCuboidShape(1, 0, 1, 15, 32, 15);
  private static final AxisAlignedBB AXIS_ALIGNED_BB = new AxisAlignedBB(0, 0, 0, 1, 2, 1);

  public WindSwitchBlock() {
    super(Properties.create(Material.WOOD).sound(SoundType.WOOD));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
    if (!world.isRemote && state.get(POWERED)) {
      if (!this.shouldBePowered(world, pos)) {
        this.toggleState(state, world, pos, true);
      } else {
        world.getPendingBlockTicks().scheduleTick(pos, this, POWERED_TIME);
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (!world.isRemote && this.shouldBePowered(world, pos)) {
      this.toggleState(state, world, pos);
      world.getPendingBlockTicks().scheduleTick(pos, this, POWERED_TIME);
    }
  }

  private boolean shouldBePowered(World world, BlockPos pos) {
    AxisAlignedBB axisalignedbb = AXIS_ALIGNED_BB.offset(pos);
    List<? extends Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
    return list.stream().anyMatch(e -> e instanceof WhirlwindEntity);
  }

  // TODO custom sound
  @Override
  protected SoundEvent getSwitchOnSound() {
    return super.getSwitchOnSound();
  }

  @Override
  protected SoundEvent getSwitchOffSound() {
    return null;
  }

  @Override
  protected Direction getStrongPowerDirection(BlockState blockState) {
    return Direction.DOWN;
  }
}
