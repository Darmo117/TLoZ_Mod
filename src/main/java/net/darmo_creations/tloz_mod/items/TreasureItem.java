package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * An item that can be put into a treasure bag.
 *
 * @see TreasureBagItem
 */
public class TreasureItem extends SpecialPickableItem {
  private final int basePrice;

  /**
   * Create a treasure item.
   *
   * @param basePrice Base price in rupees.
   */
  public TreasureItem(final int basePrice) {
    super(new Properties().group(TLoZ.CREATIVE_MODE_TAB));
    this.basePrice = basePrice;
  }

  /**
   * Return the base price in rupees of this item.
   */
  public int getBasePrice() {
    return this.basePrice;
  }

  @Override
  protected void onPickup(PlayerEntity player, ItemStack itemStack) {
    int treasureBagIndex = Utils.getTreasureBagInventorySlot(player);
    if (treasureBagIndex >= 0) {
      ModItems.TREASURE_BAG.addTreasure(itemStack, player.inventory.getStackInSlot(treasureBagIndex));
    }
  }
}
