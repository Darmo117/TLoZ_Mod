package net.darmo_creations.tloz_mod.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A block that can be destroyed by bombs. Subject to gravity and breaks upon touching the ground.
 */
public class CrumblyBlock extends FallingBlock {
  public CrumblyBlock() {
    super(Properties.create(Material.ROCK).sound(SoundType.ANCIENT_DEBRIS));
  }

  @Override
  protected void onStartFalling(FallingBlockEntity fallingEntity) {
    fallingEntity.setHurtEntities(true);
  }

  @Override
  public void onEndFalling(World world, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingBlock) {
    world.destroyBlock(pos, false);
  }
}
