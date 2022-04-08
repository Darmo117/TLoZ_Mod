package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.Utils;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Items that extend this class have a special behavior when picked up.
 * Upon being picked up, the {@link #onPickup(PlayerEntity, ItemStack)} method
 * is called and the item entity is removed.
 */
public abstract class SpecialPickableItem extends TLoZItem {
  public SpecialPickableItem(Properties properties) {
    super(properties);
  }

  /**
   * Called when this item is picked up by a player.
   *
   * @param player    The player that picked up this item.
   * @param itemStack The picked up item stack.
   */
  protected abstract void onPickup(PlayerEntity player, ItemStack itemStack);

  /**
   * Play a sound when this item is picked up.
   *
   * @param player    The player that picked up this item.
   * @param itemStack The picked up item stack.
   */
  @SuppressWarnings("unused")
  protected void playPickupSound(PlayerEntity player, ItemStack itemStack) {
    Utils.playItemPickupSound(player);
  }

  @SubscribeEvent
  public static void onEntityItemPickup(EntityItemPickupEvent event) {
    ItemEntity itemEntity = event.getItem();
    ItemStack stack = itemEntity.getItem();
    Item item = stack.getItem();
    if (item instanceof SpecialPickableItem) {
      PlayerEntity player = event.getPlayer();
      SpecialPickableItem i = (SpecialPickableItem) item;
      i.onPickup(player, stack);
      i.playPickupSound(player, stack);
      itemEntity.remove();
      event.setCanceled(true);
    }
  }
}
