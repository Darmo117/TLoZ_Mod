package net.darmo_creations.tloz_mod.blocks;

import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class FloorSwitchBlock extends AbstractPressurePlateBlock {
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  private static final VoxelShape SHAPE = makeCuboidShape(1, 0, 1, 15, 1, 15);

  public static final List<Class<? extends Entity>> ALLOWED_ENTITIES = new ArrayList<>();

  static {
    ALLOWED_ENTITIES.add(PlayerEntity.class);
    // TODO add possessed phantom
  }

  public FloorSwitchBlock() {
    super(Properties.create(Material.ROCK));
    this.setDefaultState(this.getStateContainer().getBaseState().with(POWERED, false));
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @Override
  public boolean canSpawnInBlock() {
    return false;
  }

  @Override
  protected int getPoweredDuration() {
    return 1;
  }

  @Override
  protected void playClickOnSound(IWorld world, BlockPos pos) {
    world.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
  }

  @Override
  protected void playClickOffSound(IWorld world, BlockPos pos) {
    world.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
  }

  @Override
  protected int computeRedstoneStrength(World world, BlockPos pos) {
    AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(pos);
    List<? extends Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);

    for (Entity entity : list) {
      if (ALLOWED_ENTITIES.stream().anyMatch(c -> c.isAssignableFrom(entity.getClass()))) {
        return 15;
      }
    }

    return 0;
  }

  @Override
  protected int getRedstoneStrength(BlockState state) {
    return state.get(POWERED) ? 15 : 0;
  }

  @Override
  protected BlockState setRedstoneStrength(BlockState state, int strength) {
    return state.with(POWERED, strength > 0);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(POWERED);
  }
}
