package net.darmo_creations.tloz_mod.tile_entities;

import net.darmo_creations.tloz_mod.blocks.JarBlock;
import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.darmo_creations.tloz_mod.entities.JarEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Tile entity for the {@link JarBlock}.
 *
 * @see JarBlock
 * @see BombEntity
 */
public class JarTileEntity extends PickableTileEntity {
  public JarTileEntity() {
    super(ModTileEntities.JAR.get());
  }

  /**
   * Spawns a jar entity at the location of the block.
   *
   * @param picker          Optional player that picked this entity.
   * @param killImmediately Whether to destroy the jar entity immediately.
   * @return True if the entity was successfully spawned.
   */
  public boolean spawnJarEntity(PlayerEntity picker, final boolean killImmediately) {
    BlockPos pos = this.getPos();
    JarEntity jar = new JarEntity(this.world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, killImmediately ? null : picker);
    //noinspection ConstantConditions
    this.world.addEntity(jar);
    if (killImmediately) {
      jar.die();
    }
    return true;
  }
}
