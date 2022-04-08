package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.tile_entities.BossKeyTileEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// TODO may be electrified
public class BossKeyBlock extends PickableBlock<BossKeyTileEntity> {
  public BossKeyBlock() {
    super(Properties.create(Material.IRON)
            .sound(SoundType.METAL)
            .setBlocksVision((blockState, blockReader, pos) -> false),
        BossKeyTileEntity.class);
  }

  @Override
  protected InteractionResult onInteraction(BossKeyTileEntity tileEntity, World world, BlockPos pos, InteractionContext interactionContext) {
    if (interactionContext.interactionType == InteractionType.PLAYER_INTERACT) {
      return tileEntity.spawnKeyEntity(interactionContext.player) ? InteractionResult.BREAK_BLOCK : InteractionResult.FAIL;
    }
    return InteractionResult.FAIL;
  }
}
