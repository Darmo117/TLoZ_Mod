package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

/**
 * A bag that can hold up to 9999 rupees.
 */
public class RupeeBagItem extends TLoZItem {
  public RupeeBagItem() {
    super(new Properties().maxDamage(9999).group(TLoZ.CREATIVE_MODE_TAB));
  }

  @Override
  public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
    int maxDamage = stack.getMaxDamage();
    int damage = stack.getDamage();
    Style style = Style.EMPTY;
    float proportion = (maxDamage - damage) / (float) maxDamage;
    if (proportion == 0) {
      style = style.setFormatting(TextFormatting.RED);
    } else {
      style = style.setFormatting(TextFormatting.GRAY);
    }
    tooltip.add(new TranslationTextComponent("item.tloz.rupee_bag.tooltip.count",
        maxDamage - damage, maxDamage).setStyle(style));
  }
}
