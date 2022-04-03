package net.darmo_creations.tloz_mod.tile_entities;

import net.darmo_creations.tloz_mod.blocks.BombFlowerBlock;
import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Tile entity for the {@link BombFlowerBlock}.
 *
 * @see BombFlowerBlock
 * @see BombEntity
 */
public class BombFlowerTileEntity extends PickableTileEntity {
  public static final int FUSE_DELAY = 140; // 7 seconds

  public BombFlowerTileEntity() {
    super(ModTileEntities.BOMB_FLOWER.get(), FUSE_DELAY);
  }

  /**
   * Pops the bomb from the bomb flower if it has any.
   *
   * @param player       The optional player that picked the bomb.
   * @param fuse         Number of ticks before the bomb will explode.
   * @param invulnerable If true, the bomb entity will not be killable.
   * @return True if the bomb entity could be spawned, false otherwise.
   */
  public boolean popBomb(PlayerEntity player, final int fuse, final boolean invulnerable) {
    if (!this.resetGrowthTimer()) {
      return false;
    }
    BlockPos pos = this.getPos();
    BombEntity bomb = new BombEntity(this.world, pos.getX() + 0.5, pos.getY() + 0.0625, pos.getZ() + 0.5, fuse, true, invulnerable, player);
    //noinspection ConstantConditions
    this.world.addEntity(bomb);
    return true;
  }

  /**
   * Return true if the bomb flower has a ripe bomb.
   * <p>
   * Alias for {@link #hasBlock()}.
   */
  public boolean hasBomb() {
    return this.hasBlock();
  }
}
