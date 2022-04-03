package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.blocks.JarBlock;
import net.darmo_creations.tloz_mod.items.ModItems;
import net.darmo_creations.tloz_mod.tile_entities.JarTileEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

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

  public JarEntity(World world, double x, double y, double z, PlayerEntity picker) {
    super(ModEntities.JAR.get(), world, x, y, z, picker);
  }

  // TODO break on block collision

  @Override
  public void die() {
    super.die();
    if (this.world.isRemote) {
      this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS,
          4, (1 + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, false);
    }
  }

  @Override
  protected List<ItemStack> getDrops() {
    // TODO randomize loot depending on player’s available weapons and ammo amount
    return Collections.singletonList(new ItemStack(ModItems.HEART));
  }
}
