package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Item that refills the bomb bag of the player that picks it up.
 */
public class BombItem extends SpecialPickableItem {
  public BombItem() {
    super(new Properties().group(TLoZ.CREATIVE_MODE_TAB));
  }

  @Override
  protected void onPickup(PlayerEntity player, ItemStack itemStack) {
    int bombBagIndex = Utils.getBombBagInventorySlot(player);
    if (bombBagIndex >= 0) {
      ItemStack bombBag = player.inventory.getStackInSlot(bombBagIndex);
      ((BombBagItem) bombBag.getItem()).add(bombBag, itemStack.getCount());
    }
  }
}
