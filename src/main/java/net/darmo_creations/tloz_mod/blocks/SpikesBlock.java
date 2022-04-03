package net.darmo_creations.tloz_mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

// TODO make passable to phantoms
public class SpikesBlock extends Block {
  protected static final VoxelShape SHAPE = makeCuboidShape(1, 0, 1, 15, 32, 15);

  public SpikesBlock() {
    super(Properties.create(Material.IRON)
        .notSolid()
        .setBlocksVision((blockState, blockReader, pos) -> false)
        .sound(SoundType.METAL));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    return facing == Direction.DOWN && !state.isValidPosition(world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
    BlockPos blockpos = pos.down();
    return hasSolidSideOnTop(world, blockpos) || hasEnoughSolidSide(world, blockpos, Direction.UP);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
    entityIn.attackEntityFrom(DamageSource.CACTUS, 1);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
    return false;
  }
}
