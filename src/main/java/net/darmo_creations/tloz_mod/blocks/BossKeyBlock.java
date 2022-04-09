package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.BossKeyEntity;
import net.darmo_creations.tloz_mod.entities.WhirlwindEntity;
import net.darmo_creations.tloz_mod.tile_entities.BossKeyTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

// TODO may be electrified
public class BossKeyBlock extends PickableBlock<BossKeyTileEntity, BossKeyEntity> {
  public BossKeyBlock() {
    super(Properties.create(Material.IRON)
            .sound(SoundType.METAL)
            .setBlocksVision((blockState, blockReader, pos) -> false),
        BossKeyTileEntity.class, BossKeyEntity.class);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return makeCuboidShape(1, 0, 1, 15, 15, 15);
  }

  @Override
  protected InteractionResult onInteraction(BossKeyTileEntity tileEntity, World world, BlockPos pos, InteractionContext interactionContext) {
    if (interactionContext.interactionType == InteractionType.PLAYER_INTERACT) {
      return tileEntity.spawnKeyEntity(interactionContext.player) ? InteractionResult.BREAK_BLOCK : InteractionResult.FAIL;
    } else if (interactionContext.entity instanceof WhirlwindEntity) {
      return tileEntity.spawnKeyEntity(null) ? InteractionResult.BREAK_BLOCK : InteractionResult.FAIL;
    }
    return InteractionResult.FAIL;
  }
}
