package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * An item that permanently adds 1 heart to the player that picks it up.
 */
public class HeartContainerItem extends SpecialPickableItem {
  public HeartContainerItem() {
    super(new Properties().maxStackSize(1).group(TLoZ.CREATIVE_MODE_TAB));
  }

  @Override
  protected void onPickup(PlayerEntity player, ItemStack itemStack) {
    //noinspection ConstantConditions
    player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(player.getBaseAttributeValue(Attributes.MAX_HEALTH) + 2);
    player.heal(2);
  }
}
