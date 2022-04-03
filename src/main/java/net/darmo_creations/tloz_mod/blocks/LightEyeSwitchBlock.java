package net.darmo_creations.tloz_mod.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class LightEyeSwitchBlock extends EyeSwitchBlock {
  private static final VoxelShape NORTH_OFF = makeCuboidShape(2, 3, 8, 14, 13, 16);
  private static final VoxelShape NORTH_ON = makeCuboidShape(2, 3, 4, 14, 13, 16);
  private static final VoxelShape SOUTH_OFF = makeCuboidShape(2, 3, 0, 14, 13, 8);
  private static final VoxelShape SOUTH_ON = makeCuboidShape(2, 3, 0, 14, 13, 12);
  private static final VoxelShape EAST_OFF = makeCuboidShape(0, 3, 2, 8, 13, 14);
  private static final VoxelShape EAST_ON = makeCuboidShape(0, 3, 2, 12, 13, 14);
  private static final VoxelShape WEST_OFF = makeCuboidShape(8, 3, 2, 16, 13, 14);
  private static final VoxelShape WEST_ON = makeCuboidShape(4, 3, 2, 16, 13, 14);

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

  // Toggle when hit by arrow
  @Override
  public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
    if (projectile instanceof SpectralArrowEntity) {
      this.toggleState(state, world, hit.getPos());
    }
    projectile.remove();
  }
}
