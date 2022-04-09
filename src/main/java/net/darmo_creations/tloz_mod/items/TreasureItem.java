package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.Utils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;

/**
 * An item that can be put into a treasure bag.
 *
 * @see TreasureBagItem
 */
public class TreasureItem extends SpecialPickableItem {
  private final int basePrice;

  /**
   * Create a treasure item.
   *
   * @param basePrice Base price in rupees.
   */
  public TreasureItem(final int basePrice) {
    super(new Properties().group(TLoZ.CREATIVE_MODE_TAB));
    this.basePrice = basePrice;
  }

  @Override
  public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
    //noinspection ConstantConditions
    tooltip.add(new TranslationTextComponent(String.format("item.tloz.%s.description", this.getRegistryName().getPath()))
        .setStyle(Style.EMPTY.setFormatting(TextFormatting.GRAY)));
    tooltip.add(new TranslationTextComponent("item.tloz.treasure.tooltip.price", this.getBasePrice())
        .setStyle(Style.EMPTY.setFormatting(TextFormatting.GREEN)));
  }

  /**
   * Return the base price in rupees of this item.
   */
  public int getBasePrice() {
    return this.basePrice;
  }

  @Override
  protected void onPickup(PlayerEntity player, ItemStack itemStack) {
    int treasureBagIndex = Utils.getTreasureBagInventorySlot(player);
    if (treasureBagIndex >= 0) {
      ModItems.TREASURE_BAG.addTreasure(itemStack, player.inventory.getStackInSlot(treasureBagIndex));
    }
  }

  /**
   * Return a comparator that sorts treasures according to their base price and unlocalized name.
   */
  public static Comparator<TreasureItem> getComparator() {
    return (item1, item2) -> {
      int price1 = item1.getBasePrice();
      int price2 = item2.getBasePrice();
      int comp = Integer.compare(price1, price2);
      if (comp != 0) {
        return comp;
      }
      //noinspection ConstantConditions
      String name1 = item1.getRegistryName().getPath();
      //noinspection ConstantConditions
      String name2 = item2.getRegistryName().getPath();
      return name1.compareTo(name2);
    };
  }
}
