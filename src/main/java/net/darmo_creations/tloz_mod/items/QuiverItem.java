package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.Utils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

/**
 * An item used as an ammo source for {@link QuiverBowItem}s.
 */
public class QuiverItem extends TLoZItem {
  /**
   * Create a quiver.
   *
   * @param capacity Number of arrows this quiver can hold.
   */
  public QuiverItem(final int capacity) {
    super(new Properties()
        .maxDamage(capacity)
        .group(TLoZ.CREATIVE_MODE_TAB));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
    int maxDamage = stack.getMaxDamage();
    int damage = stack.getDamage();
    Style style = Style.EMPTY;
    float proportion = (maxDamage - damage) / (float) maxDamage;
    if (proportion == 1) {
      style = style.setFormatting(TextFormatting.GREEN);
    } else if (proportion == 0) {
      style = style.setFormatting(TextFormatting.RED);
    } else if (proportion <= 0.5) {
      style = style.setFormatting(TextFormatting.GOLD);
    } else {
      style = style.setFormatting(TextFormatting.YELLOW);
    }
    tooltip.add(new TranslationTextComponent("item.tloz.quiver.tooltip.count",
        maxDamage - damage, maxDamage).setStyle(style));
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
        if (quiver.isDamaged()) {
          quiver.setDamage(quiver.getDamage() - arrowsStack.getCount());
        }
      }
      Utils.playItemPickupSound(player);
      itemEntity.remove();
      event.setCanceled(true);
    }
  }
}
