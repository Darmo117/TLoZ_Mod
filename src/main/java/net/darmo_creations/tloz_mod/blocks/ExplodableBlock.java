package net.darmo_creations.tloz_mod.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ExplodableBlock {
  void onBombExplosion(World world, BlockPos pos);
}
