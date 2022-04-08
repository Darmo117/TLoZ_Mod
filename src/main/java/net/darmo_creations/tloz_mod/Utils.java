package net.darmo_creations.tloz_mod;

import net.darmo_creations.tloz_mod.items.BombBagItem;
import net.darmo_creations.tloz_mod.items.QuiverItem;
import net.darmo_creations.tloz_mod.items.RupeeBagItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Defines various utility functions.
 */
public final class Utils {
  /**
   * Return the index of the slot in a player’s inventory that contains a bomb bag.
   *
   * @param player The player whose inventory is to be scanned.
   * @return The slot index or -1 if no bomb bag could be found.
   */
  public static int getBombBagInventorySlot(final PlayerEntity player) {
    return getInventorySlot(player, BombBagItem.class);
  }

  /**
   * Return the index of the slot in a player’s inventory that contains a quiver.
   *
   * @param player The player whose inventory is to be scanned.
   * @return The slot index or -1 if no quiver could be found.
   */
  public static int getQuiverInventorySlot(final PlayerEntity player) {
    return getInventorySlot(player, QuiverItem.class);
  }

  /**
   * Return the index of the slot in a player’s inventory that contains a rupee bag.
   *
   * @param player The player whose inventory is to be scanned.
   * @return The slot index or -1 if no rupee bag could be found.
   */
  public static int getRupeeBagInventorySlot(final PlayerEntity player) {
    return getInventorySlot(player, RupeeBagItem.class);
  }

  /**
   * Return the slot index of the first item that has the given class.
   *
   * @param player    The player whose inventory is to be scanned.
   * @param itemClass Item’s class.
   * @return The slot index or -1 if no item matched.
   */
  private static int getInventorySlot(PlayerEntity player, Class<? extends Item> itemClass) {
    NonNullList<ItemStack> mainInventory = player.inventory.mainInventory;
    for (int i = 0; i < mainInventory.size(); i++) {
      ItemStack itemStack = mainInventory.get(i);
      if (itemStack.getItem().getClass() == itemClass) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Play the item pickup sound to the specified player’s position.
   *
   * @param player The player to use the position of.
   */
  public static void playItemPickupSound(final PlayerEntity player) {
    player.world.playSound(null, player.getPosX(), player.getPosY() + 0.5, player.getPosZ(),
        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
        ((player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.7F + 1) * 2);
  }

  /**
   * Gathers into a list the values of all static fields from the given class
   * whose types are assignable to the other given class.
   *
   * @param entriesClass Class to gather
   * @param entryClass   Class that fields should be assignable to.
   * @param <T>          Type of each entry.
   * @return A list containing the values of all gathered fields.
   */
  public static <T> List<T> gatherEntries(final Class<?> entriesClass, final Class<T> entryClass) {
    List<T> entries = new LinkedList<>();
    Arrays.stream(entriesClass.getDeclaredFields())
        .filter(field -> entryClass.isAssignableFrom(field.getType()))
        .map(field -> {
          T item;
          try {
            //noinspection unchecked
            item = (T) field.get(null);
          } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
          }
          return item;
        })
        .forEach(entries::add);
    return entries;
  }

  private Utils() {
  }
}
