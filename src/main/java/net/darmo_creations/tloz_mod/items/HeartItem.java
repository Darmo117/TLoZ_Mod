package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.Utils;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * An item that restores 1 heart to the player when picked up.
 */
public class HeartItem extends TLoZItem {
  public HeartItem() {
    super(new Properties().maxStackSize(1).group(TLoZ.CREATIVE_MODE_TAB));
  }

  @SubscribeEvent
  public static void onEntityItemPickup(EntityItemPickupEvent event) {
    ItemEntity itemEntity = event.getItem();
    ItemStack heartsStack = itemEntity.getItem();
    if (heartsStack.getItem() == ModItems.HEART) {
      PlayerEntity player = event.getPlayer();
      player.heal(2 * heartsStack.getCount());
      Utils.playItemPickupSound(player);
      itemEntity.remove();
      event.setCanceled(true);
    }
  }
}
