package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.BigRockEntity;
import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.darmo_creations.tloz_mod.tile_entities.BigRockTileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BigRockBlock extends PickableBlock<BigRockTileEntity> {
  public static final VoxelShape SHAPE = makeCuboidShape(1, 0, 1, 15, 15, 15);

  public BigRockBlock() {
    super(Properties.create(Material.ROCK, DyeColor.GRAY)
            .sound(SoundType.ANCIENT_DEBRIS)
            .hardnessAndResistance(-1)
            .setBlocksVision((blockState, blockReader, pos) -> false),
        BigRockTileEntity.class);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @Override
  protected InteractionResult onInteraction(BigRockTileEntity tileEntity, World world, BlockPos pos, InteractionContext interactionContext) {
    switch (interactionContext.interactionType) {
      case PLAYER_INTERACT:
        return tileEntity.spawnRockEntity(interactionContext.player, false) ? InteractionResult.BREAK_BLOCK : InteractionResult.FAIL;
      case ENTITY_COLLISION:
        if (interactionContext.entity instanceof BombEntity || interactionContext.entity instanceof BigRockEntity) {
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
