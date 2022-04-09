package net.darmo_creations.tloz_mod.tile_entities;

import net.darmo_creations.tloz_mod.entities.BossKeyEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class BossKeyTileEntity extends PickableTileEntity {
  public BossKeyTileEntity() {
    super(ModTileEntities.BOSS_KEY.get());
  }

  /**
   * Spawns a boss key entity at the location of the block.
   *
   * @param picker Optional player that picked this entity.
   * @return True if the entity was successfully spawned.
   */
  public boolean spawnKeyEntity(PlayerEntity picker) {
    BlockPos pos = this.getPos();
    BossKeyEntity key = new BossKeyEntity(this.world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, picker);
    //noinspection ConstantConditions
    this.world.addEntity(key);
    return true;
  }
}
