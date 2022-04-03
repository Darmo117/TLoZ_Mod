package net.darmo_creations.tloz_mod.tile_entities;

import net.darmo_creations.tloz_mod.blocks.JarBlock;
import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.darmo_creations.tloz_mod.entities.JarEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

  public boolean spawnJarEntity(PlayerEntity picker, World world, final boolean killImmediately) {
    BlockPos pos = this.getPos();
    JarEntity jar = new JarEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, picker);
    world.addEntity(jar);
    if (killImmediately) {
      jar.die();
    }
    return true;
  }
}
