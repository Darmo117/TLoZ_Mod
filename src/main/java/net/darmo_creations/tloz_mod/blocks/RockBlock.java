package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.RockEntity;
import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.darmo_creations.tloz_mod.tile_entities.RockTileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class RockBlock extends PickableBlock<RockTileEntity> {
  public RockBlock() {
    super(Properties.create(Material.ROCK, DyeColor.GRAY)
            .sound(SoundType.ANCIENT_DEBRIS)
            .hardnessAndResistance(-1)
            .setBlocksVision((blockState, blockReader, pos) -> false),
        RockTileEntity.class);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.or(
        makeCuboidShape(3, 1, 3, 13, 11, 13),
        makeCuboidShape(4, 0, 4, 12, 1, 12),
        makeCuboidShape(5, 2, 13, 11, 10, 14),
        makeCuboidShape(13, 3, 4, 14, 10, 12),
        makeCuboidShape(2, 2, 5, 3, 10, 12),
        makeCuboidShape(4, 3, 2, 11, 10, 3),
        makeCuboidShape(4, 11, 5, 12, 12, 11)
    );
  }

  @Override
  protected InteractionResult onInteraction(RockTileEntity tileEntity, World world, BlockPos pos, InteractionContext interactionContext) {
    switch (interactionContext.interactionType) {
      case PLAYER_INTERACT:
        return tileEntity.spawnRockEntity(interactionContext.player, false) ? InteractionResult.BREAK_BLOCK : InteractionResult.FAIL;
      case ENTITY_COLLISION:
        if (interactionContext.entity instanceof BombEntity || interactionContext.entity instanceof RockEntity) {
          tileEntity.spawnRockEntity(null, true);
          return InteractionResult.BREAK_BLOCK;
        }
        break;
      case BOMB_EXPLOSION:
        tileEntity.spawnRockEntity(null, true);
        return InteractionResult.BREAK_BLOCK;
    }
    return InteractionResult.FAIL;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }
}
