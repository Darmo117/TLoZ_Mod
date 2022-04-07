package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.UpdateFlags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class SpikesBlock extends Block {
  public SpikesBlock() {
    super(Properties.create(Material.IRON)
        .notSolid()
        .setBlocksVision((blockState, blockReader, pos) -> false)
        .setAllowsSpawn((blockState, blockReader, pos, entityType) -> false)
        .sound(SoundType.METAL));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
    if (world.isAirBlock(pos.up())) {
      world.setBlockState(pos.up(), ModBlocks.SPIKES_EFFECT_AREA.getDefaultState(), UpdateFlags.UPDATE_BLOCK | UpdateFlags.SEND_TO_CLIENT);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.empty();
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.fullCube();
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
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    SpikesEffectAreaBlock.dealDamageAndKnockback(pos, entity);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean allowsMovement(BlockState state, IBlockReader world, BlockPos pos, PathType type) {
    return true;
  }
}
