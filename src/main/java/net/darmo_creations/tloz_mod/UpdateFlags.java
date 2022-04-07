package net.darmo_creations.tloz_mod;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class defines constant that correspond to flags used by the
 * {@link World#setBlockState(BlockPos, BlockState, int)} and
 * {@link World#notifyBlockUpdate(BlockPos, BlockState, BlockState, int)} methods.
 */
@SuppressWarnings("unused")
public final class UpdateFlags {
  public static final int UPDATE_BLOCK = 1;
  public static final int SEND_TO_CLIENT = 2;
  public static final int PREVENT_RERENDER = 4;
  public static final int RERENDER_ON_MAIN_THREAD = 8;
  public static final int PREVENT_NEIGHBOR_REACTIONS = 16;
  public static final int PREVENT_NEIGHBOR_DROPS = 32;
  public static final int BLOCK_MOVED = 64;

  private UpdateFlags() {
  }
}
