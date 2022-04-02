package net.darmo_creations.tloz_mod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A block that protects the player from any enemy.
 * <p>
 * Generates {@link SafeZoneEffectAreaBlock}s above itself when placed.
 */
public class SafeZoneBlock extends Block implements IModBlock {
  private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 1, 16);

  public static final int EFFECT_HEIGHT = 3;

  public SafeZoneBlock() {
    super(Properties.create(Material.GLASS)
        .sound(SoundType.GLASS)
        .notSolid()
        .doesNotBlockMovement()
        .setAllowsSpawn((blockState, blockReader, pos, entityType) -> false));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    SafeZoneEffectAreaBlock.provideEffect(entity);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
    for (int i = 1; i <= EFFECT_HEIGHT; i++) {
      BlockPos up = pos.up(i);
      if (world.getBlockState(up).getBlock() == Blocks.AIR) {
        world.setBlockState(up, ModBlocks.SAFE_ZONE_EFFECT_AREA.getDefaultState(), 3);
      } else {
        break;
      }
    }
  }

  @Override
  public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    super.onBlockHarvested(world, pos, state, player);
    for (int i = 1; i <= EFFECT_HEIGHT; i++) {
      BlockPos up = pos.up(i);
      if (world.getBlockState(up).getBlock() == ModBlocks.SAFE_ZONE_EFFECT_AREA) {
        world.setBlockState(up, Blocks.AIR.getDefaultState(), 3);
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  @OnlyIn(Dist.CLIENT)
  public float getAmbientOcclusionLightValue(BlockState state, IBlockReader world, BlockPos pos) {
    return 1;
  }
}
