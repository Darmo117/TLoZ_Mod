package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.PickableEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class ShockSwitchBlock extends SwitchBlock implements ExplodableBlock {
  public ShockSwitchBlock() {
    super(Properties.create(Material.GLASS)
        .sound(SoundType.GLASS)
        .hardnessAndResistance(-1)
        .setAllowsSpawn((blockState, blockReader, pos, entityType) -> false)
        .setOpaque((blockState, blockReader, pos) -> false));
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    return facing == Direction.DOWN && !state.isValidPosition(world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
    BlockPos blockpos = pos.down();
    return hasSolidSideOnTop(world, blockpos) || hasEnoughSolidSide(world, blockpos, Direction.UP);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    return makeCuboidShape(2, 0, 2, 14, 16, 14);
  }

  // Toggle state on right-click
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

  // Toggle when hit by pickable object
  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {
    if (entity instanceof PickableEntity) {
      if (!world.isRemote) {
        this.toggleState(world.getBlockState(pos), world, pos);
      }
      ((PickableEntity) entity).die();
    }
  }

  // Toggle when hit by pickable object
  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (entity instanceof PickableEntity) {
      if (!world.isRemote) {
        this.toggleState(state, world, pos);
      }
      ((PickableEntity) entity).die();
    }
  }

  // Toggle when hit by arrow
  @SuppressWarnings("deprecation")
  @Override
  public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
    if (projectile instanceof AbstractArrowEntity) {
      this.toggleState(state, world, hit.getPos());
      projectile.remove();
    }
  }

  @Override
  public void onBombExplosion(World world, BlockPos pos) {
    this.toggleState(world.getBlockState(pos), world, pos);
  }

  @Override
  protected Direction getStrongPowerDirection(BlockState blockState) {
    return Direction.DOWN;
  }

  @SuppressWarnings("deprecation")
  @Override
  public PushReaction getPushReaction(BlockState state) {
    return PushReaction.DESTROY;
  }
}
