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
 * Upon being picked up, the {@link #onPickup(PlayerEntity, ItemStack, T)} method
 * is called and the item entity is removed.
 */
public abstract class SpecialPickableItem<T extends SpecialPickableItem<T>> extends TLoZItem {
  public SpecialPickableItem(Properties properties) {
    super(properties);
  }

  /**
   * Called when this item is picked up by a player.
   *
   * @param player    The player that picked up this item.
   * @param itemStack The picked up item stack.
   * @param item      The item that was picked up.
   */
  protected abstract void onPickup(PlayerEntity player, ItemStack itemStack, T item);

  /**
   * Wrapper method for {@link #onPickup(PlayerEntity, ItemStack, SpecialPickableItem)} to avoid generic types issues.
   */
  private void onPickup(PlayerEntity player, ItemStack itemStack, Item item) {
    //noinspection unchecked
    this.onPickup(player, itemStack, (T) item);
  }

  @SubscribeEvent
  public static void onEntityItemPickup(EntityItemPickupEvent event) {
    ItemEntity itemEntity = event.getItem();
    ItemStack stack = itemEntity.getItem();
    Item item = stack.getItem();
    if (item instanceof SpecialPickableItem) {
      PlayerEntity player = event.getPlayer();
      SpecialPickableItem<?> i = (SpecialPickableItem<?>) item;
      i.onPickup(player, stack, i);
      Utils.playItemPickupSound(player);
      itemEntity.remove();
      event.setCanceled(true);
    }
  }
}
