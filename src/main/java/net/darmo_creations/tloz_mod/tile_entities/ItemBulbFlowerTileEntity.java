package net.darmo_creations.tloz_mod.tile_entities;

import net.darmo_creations.tloz_mod.blocks.ItemBulbFlowerBlock;
import net.darmo_creations.tloz_mod.entities.ItemBulbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Tile entity for the {@link ItemBulbFlowerBlock}.
 *
 * @see ItemBulbFlowerBlock
 * @see ItemBulbEntity
 */
public class ItemBulbFlowerTileEntity extends PickableTileEntity {
  public static final int GROWTH_DELAY = 140; // 7 seconds

  public ItemBulbFlowerTileEntity() {
    super(ModTileEntities.ITEM_BULB_FLOWER.get(), GROWTH_DELAY);
  }

  /**
   * Pops the bulb from the bulb flower if it has any.
   *
   * @param picker         The optional player that picked the bulb.
   * @param breakInstantly Whether to destroy the bulb entity immediately.
   * @return True if the bulb entity could be spawned, false otherwise.
   */
  public boolean popBulb(PlayerEntity picker, final boolean breakInstantly) {
    if (!this.resetGrowthTimer()) {
      return false;
    }
    BlockPos pos = this.getPos();
    ItemBulbEntity bulb = new ItemBulbEntity(this.world, pos.getX() + 0.5, pos.getY() + 0.0625, pos.getZ() + 0.5,
        breakInstantly, breakInstantly ? null : picker);
    //noinspection ConstantConditions
    this.world.addEntity(bulb);
    return true;
  }

  /**
   * Return true if the bulb flower has a ripe bulb.
   * <p>
   * Alias for {@link #hasBlock()}.
   */
  public boolean hasBulb() {
    return this.hasBlock();
  }
}
