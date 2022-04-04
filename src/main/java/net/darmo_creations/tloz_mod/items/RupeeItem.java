package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Item representing a single rupee.
 */
public class RupeeItem extends SpecialPickableItem<RupeeItem> {
  private final int value;

  public RupeeItem(final int value) {
    super(new Properties().group(TLoZ.CREATIVE_MODE_TAB));
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

  @Override
  protected void onPickup(PlayerEntity player, ItemStack itemStack, RupeeItem item) {
    int rupeeBagIndex = Utils.getBombBagInventorySlot(player);
    if (rupeeBagIndex >= 0) {
      ItemStack rupeeBag = player.inventory.getStackInSlot(rupeeBagIndex);
      if (rupeeBag.isDamaged()) {
        rupeeBag.setDamage(rupeeBag.getDamage() - itemStack.getCount() * item.getValue());
      }
    }
  }
}
