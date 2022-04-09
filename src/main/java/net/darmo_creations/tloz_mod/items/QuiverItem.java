package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.Utils;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * An item used as an ammo source for {@link QuiverBowItem}s.
 */
public class QuiverItem extends SimpleBagItem {
  /**
   * Create a quiver.
   *
   * @param capacity Number of arrows this quiver can hold.
   */
  public QuiverItem(final int capacity) {
    super(capacity);
  }

  @Override
  protected String getUnlocalizedTooltipName() {
    return "quiver";
  }

  @Override
  protected Style getStyleForProportion(float proportion, Style baseStyle) {
    if (proportion == 1) {
      return baseStyle.setFormatting(TextFormatting.GREEN);
    } else if (proportion == 0) {
      return baseStyle.setFormatting(TextFormatting.RED);
    } else if (proportion <= 0.5) {
      return baseStyle.setFormatting(TextFormatting.GOLD);
    } else {
      return baseStyle.setFormatting(TextFormatting.YELLOW);
    }
  }

  /**
   * Refill player’s quiver when they pickup an arrow item.
   */
  @SubscribeEvent
  public static void onEntityItemPickup(EntityItemPickupEvent event) {
    ItemEntity itemEntity = event.getItem();
    ItemStack arrowsStack = itemEntity.getItem();
    if (arrowsStack.getItem() == Items.ARROW) {
      PlayerEntity player = event.getPlayer();
      int quiverIndex = Utils.getQuiverInventorySlot(player);
      if (quiverIndex >= 0) {
        ItemStack quiver = player.inventory.getStackInSlot(quiverIndex);
        ((QuiverItem) quiver.getItem()).add(quiver, arrowsStack.getCount());
      }
      Utils.playItemPickupSound(player);
      itemEntity.remove();
      event.setCanceled(true);
    }
  }
}
