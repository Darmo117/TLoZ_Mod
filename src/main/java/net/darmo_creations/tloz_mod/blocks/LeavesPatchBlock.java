package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.WhirlwindEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class LeavesPatchBlock extends Block {
  private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 1, 16);

  public LeavesPatchBlock() {
    super(Properties.create(Material.LEAVES)
        .doesNotBlockMovement()
        .sound(SoundType.PLANT));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (entity instanceof WhirlwindEntity) {
      if (!world.isRemote) {
        world.destroyBlock(pos, true);
      }
    }
  }
}
