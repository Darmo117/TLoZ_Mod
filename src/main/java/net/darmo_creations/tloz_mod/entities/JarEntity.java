package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.blocks.JarBlock;
import net.darmo_creations.tloz_mod.tile_entities.JarTileEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Entity for the jar block.
 *
 * @see JarBlock
 * @see JarTileEntity
 */
public class JarEntity extends PickableEntity {
  public JarEntity(EntityType<? extends JarEntity> type, World world) {
    super(type, world);
  }

  public JarEntity(World world, double x, double y, double z) {
    super(ModEntities.JAR.get(), world, x, y, z);
  }

  // TODO break on block collision

  @Override
  public void die() {
    super.die();
    if (this.world.isRemote) {
      this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS,
          4, (1 + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, false);
    } else {
      // TODO drop loot
    }
  }
}
