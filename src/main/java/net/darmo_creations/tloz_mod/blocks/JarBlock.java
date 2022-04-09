package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.JarEntity;
import net.darmo_creations.tloz_mod.entities.PickableEntity;
import net.darmo_creations.tloz_mod.entities.WhirlwindEntity;
import net.darmo_creations.tloz_mod.tile_entities.JarTileEntity;
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

public class JarBlock extends PickableBlock<JarTileEntity, JarEntity> {
  public JarBlock() {
    super(Properties.create(Material.MISCELLANEOUS, DyeColor.LIGHT_BLUE)
            .sound(SoundType.GLASS)
            .setBlocksVision((blockState, blockReader, pos) -> false),
        JarTileEntity.class, JarEntity.class);
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

  @Override
  protected InteractionResult onInteraction(JarTileEntity tileEntity, World world, BlockPos pos, InteractionContext interactionContext) {
    switch (interactionContext.interactionType) {
      case PLAYER_INTERACT:
        return tileEntity.spawnJarEntity(interactionContext.player, false) ? InteractionResult.BREAK_BLOCK : InteractionResult.FAIL;
      case ENTITY_COLLISION:
        if (interactionContext.entity instanceof PickableEntity) {
          return tileEntity.spawnJarEntity(null, true) ? InteractionResult.BREAK_BLOCK : InteractionResult.FAIL;
        } else if (interactionContext.entity instanceof WhirlwindEntity) {
          return tileEntity.spawnJarEntity(null, false) ? InteractionResult.BREAK_BLOCK : InteractionResult.FAIL;
        }
        break;
      case PLAYER_HIT:
      case PROJECTILE_COLLISION:
      case BOMB_EXPLOSION:
        return tileEntity.spawnJarEntity(null, true) ? InteractionResult.BREAK_BLOCK : InteractionResult.FAIL;
    }
    return InteractionResult.FAIL;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }
}
