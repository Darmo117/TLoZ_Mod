package net.darmo_creations.tloz_mod.items;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

/**
 * A bag that can hold up to 9999 rupees.
 */
public class RupeeBagItem extends SimpleBagItem {
  public RupeeBagItem() {
    super(9999);
  }

  @Override
  protected String getUnlocalizedTooltipName() {
    return "rupee_bag";
  }

  @Override
  protected Style getStyleForProportion(float proportion, Style baseStyle) {
    if (proportion == 0) {
      return baseStyle.setFormatting(TextFormatting.RED);
    } else {
      return baseStyle.setFormatting(TextFormatting.GRAY);
    }
  }
}
