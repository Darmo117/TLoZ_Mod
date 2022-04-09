package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A bag that contains all treasures the player has picked up.
 *
 * @see TreasureItem
 */
public class TreasureBagItem extends TLoZItem {
  public static final int MAX_AMOUNT = 99;

  public TreasureBagItem() {
    super(new Properties().maxStackSize(1).group(TLoZ.CREATIVE_MODE_TAB));
  }

  @Override
  public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
    Map<TreasureItem, Integer> treasures = this.getTreasures(stack);
    if (treasures.isEmpty()) {
      tooltip.add(new TranslationTextComponent("item.tloz.treasure_bag.tooltip.empty")
          .setStyle(Style.EMPTY.setFormatting(TextFormatting.GRAY).setItalic(true)));
    } else {
      int amount = treasures.values().stream().reduce(0, Integer::sum);
      tooltip.add(new TranslationTextComponent("item.tloz.treasure_bag.tooltip.summary", amount)
          .setStyle(Style.EMPTY.setFormatting(TextFormatting.GRAY)));
      long handle = Minecraft.getInstance().getMainWindow().getHandle();
      if (InputMappings.isKeyDown(handle, 340) // LSHIFT
          || InputMappings.isKeyDown(handle, 344)) { // RSHIFT
        treasures.entrySet().stream()
            .sorted((a, b) -> TreasureItem.getComparator().compare(a.getKey(), b.getKey()))
            .forEach(entry -> {
              //noinspection ConstantConditions
              String key = String.format("item.tloz.%s", entry.getKey().getRegistryName().getPath());
              String name = new TranslationTextComponent(key).getString();
              tooltip.add(new TranslationTextComponent("item.tloz.treasure_bag.tooltip.item", name, entry.getValue())
                  .setStyle(Style.EMPTY.setFormatting(TextFormatting.GRAY)));
            });
      } else {
        tooltip.add(new TranslationTextComponent("item.tloz.treasure_bag.tooltip.shift")
            .setStyle(Style.EMPTY.setFormatting(TextFormatting.GREEN)));
      }
    }
  }

  /**
   * Add a treasure to the given bag.
   *
   * @param treasureItem The bag to add the treasure to.
   * @param bag          The bag.
   */
  public void addTreasure(final ItemStack treasureItem, ItemStack bag) {
    Item item = treasureItem.getItem();
    if (!(item instanceof TreasureItem)) {
      return;
    }
    CompoundNBT contents = bag.getTag();
    if (contents == null) {
      contents = new CompoundNBT();
    }
    //noinspection ConstantConditions
    String treasureName = item.getRegistryName().getPath();
    contents.putInt(treasureName, Math.min(MAX_AMOUNT, contents.getInt(treasureName) + treasureItem.getCount()));
    bag.setTag(contents);
  }

  /**
   * Remove a treasure from the given bag.
   *
   * @param treasureItem The bag to remove the treasure from.
   * @param bag          The bag.
   */
  public void removeTreasure(final ItemStack treasureItem, ItemStack bag) {
    Item item = treasureItem.getItem();
    if (!(item instanceof TreasureItem)) {
      return;
    }
    CompoundNBT contents = bag.getTag();
    if (contents == null) {
      contents = new CompoundNBT();
    }
    //noinspection ConstantConditions
    String treasureName = item.getRegistryName().getPath();
    contents.putInt(treasureName, Math.max(0, contents.getInt(treasureName) - treasureItem.getCount()));
    bag.setTag(contents);
  }

  /**
   * Return the amount of the given treasure.
   *
   * @param treasureItem The treasure to get the quantity of.
   * @param bag          The bag.
   * @return The amount of the given treasure or -1 if the given stack’s item is not a {@link TreasureBagItem}.
   */
  public int getAmountOf(final TreasureItem treasureItem, final ItemStack bag) {
    Item item = treasureItem.getItem();
    if (!(item instanceof TreasureItem)) {
      return -1;
    }
    CompoundNBT contents = bag.getTag();
    if (contents == null) {
      contents = new CompoundNBT();
    }
    //noinspection ConstantConditions
    return contents.getInt(treasureItem.getRegistryName().getPath());
  }

  /**
   * Return all treasures contained in the given bag.
   *
   * @param bag The bag.
   * @return A map associating each treasure item to its amount.
   */
  public Map<TreasureItem, Integer> getTreasures(final ItemStack bag) {
    CompoundNBT tag = bag.getTag();
    if (tag == null) {
      return Collections.emptyMap();
    }
    Map<TreasureItem, Integer> contents = new HashMap<>();
    tag.keySet().forEach(name -> {
      Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(TLoZ.MODID, name));
      if (item instanceof TreasureItem) {
        contents.put((TreasureItem) item, tag.getInt(name));
      }
    });
    return contents;
  }
}
