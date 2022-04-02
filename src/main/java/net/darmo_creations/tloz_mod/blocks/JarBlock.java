package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class JarBlock extends Block implements IModBlock {
  public JarBlock() {
    super(Properties.create(Material.MISCELLANEOUS, DyeColor.LIGHT_BLUE)
        .sound(SoundType.GLASS)
        .notSolid()
        .zeroHardnessAndResistance()
        .setAllowsSpawn((blockState, blockReader, pos, entityType) -> false)
        .setOpaque((blockState, blockReader, pos) -> false)
        .setSuffocates((blockState, blockReader, pos) -> false)
        .setBlocksVision((blockState, blockReader, pos) -> false));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.or(
        makeCuboidShape(3, 15, 3, 13, 16, 13),
        makeCuboidShape(4, 14, 4, 12, 15, 12),
        makeCuboidShape(3, 0, 3, 13, 14, 13),
        makeCuboidShape(2, 1, 2, 14, 13, 14),
        makeCuboidShape(1, 2, 1, 15, 12, 15)
    );
  }

  // Collision with bomb entity -> explode bomb
  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (entity instanceof BombEntity && entity.getMotion().length() > BombEntity.EXPLOSION_SPEED_THRESHOLD) {
      ((BombEntity) entity).explode();
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    // TODO change dropped items depending on player’s available weapons
    return Collections.emptyList();
  }
}
