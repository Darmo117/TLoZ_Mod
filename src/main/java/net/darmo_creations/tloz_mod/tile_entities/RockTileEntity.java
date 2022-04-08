package net.darmo_creations.tloz_mod.tile_entities;

import net.darmo_creations.tloz_mod.blocks.RockBlock;
import net.darmo_creations.tloz_mod.entities.RockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Tile entity for the {@link RockBlock}.
 *
 * @see RockBlock
 * @see RockEntity
 */
public class RockTileEntity extends PickableTileEntity {
  public RockTileEntity() {
    super(ModTileEntities.ROCK.get());
  }

  /**
   * Spawns a rock entity at the location of the block.
   *
   * @param picker         Optional player that picked this entity.
   * @param breakInstantly Whether to destroy the rock entity immediately.
   * @return True if the entity was successfully spawned.
   */
  public boolean spawnRockEntity(PlayerEntity picker, final boolean breakInstantly) {
    if (!this.resetGrowthTimer()) { // Prevent entity spawning twice when broken with a bomb
      return false;
    }
    BlockPos pos = this.getPos();
    RockEntity rock = new RockEntity(this.world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, breakInstantly, breakInstantly ? null : picker);
    //noinspection ConstantConditions
    this.world.addEntity(rock);
    return true;
  }
}
