package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * An item that restores 1 heart to the player that picks it up.
 */
public class HeartItem extends SpecialPickableItem<HeartItem> {
  public HeartItem() {
    super(new Properties().maxStackSize(1).group(TLoZ.CREATIVE_MODE_TAB));
  }

  @Override
  protected void onPickup(PlayerEntity player, ItemStack itemStack, HeartItem item) {
    player.heal(2 * itemStack.getCount());
  }
}
