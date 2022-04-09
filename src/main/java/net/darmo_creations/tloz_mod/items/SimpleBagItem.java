package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/**
 * Represents a bag that can hold up to a certain amount of things.
 */
public abstract class SimpleBagItem extends TLoZItem {
  /**
   * Create a bag with the given capacity.
   *
   * @param capacity The bag’s capacity.
   */
  public SimpleBagItem(final int capacity) {
    super(new Properties().maxDamage(capacity).group(TLoZ.CREATIVE_MODE_TAB));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
    int capacity = this.getCapacity(stack);
    int amount = this.getAmount(stack);
    Style style = this.getStyleForProportion((float) amount / capacity, Style.EMPTY);
    tooltip.add(new TranslationTextComponent(String.format("item.tloz.%s.tooltip.count", this.getUnlocalizedTooltipName()),
        amount, capacity).setStyle(style));
  }

  /**
   * Return the unlocalized name to use for the tooltip key.
   */
  protected abstract String getUnlocalizedTooltipName();

  /**
   * Apply a style for the given fullness.
   *
   * @param proportion Proportion of bag that is full.
   * @param baseStyle  A style to modify depending on the proportion.
   * @return A new style.
   */
  @SuppressWarnings("SameParameterValue")
  protected abstract Style getStyleForProportion(float proportion, Style baseStyle);

  /**
   * Add the given amount to the given stack.
   *
   * @param stack  The stack to modify.
   * @param amount The amount to add.
   */
  public void add(final ItemStack stack, final int amount) {
    if (stack.getItem() == this) {
      stack.setDamage(stack.getDamage() - amount);
    }
  }

  /**
   * Remove the given amount to the given stack.
   *
   * @param stack  The stack to modify.
   * @param amount The amount to remove.
   */
  public void remove(final ItemStack stack, final int amount) {
    if (stack.getItem() == this) {
      stack.setDamage(stack.getDamage() + stack.getCount() * amount);
    }
  }

  /**
   * Indicate whether the bag represented by the given stack is empty.
   *
   * @param stack The stack to check.
   * @return True if the bag is empty, false otherwise or if the stack’s item is not a {@link SimpleBagItem}..
   */
  public boolean isEmpty(final ItemStack stack) {
    return this.getAmount(stack) <= 0;
  }

  /**
   * Return the quantity of stuff present in the given bag.
   *
   * @param stack The stack to check.
   * @return The quantity or -1 if the stack’s item is not a {@link SimpleBagItem}.
   */
  public int getAmount(final ItemStack stack) {
    if (stack.getItem() != this) {
      return -1;
    }
    return stack.getMaxDamage() - stack.getDamage();
  }

  /**
   * Return the capacity of the given bag, i.e. the maximum amount of stuff it can hold.
   *
   * @param stack The stack to check.
   * @return The capacity or -1 if the stack’s item is not a {@link SimpleBagItem}.
   */
  public int getCapacity(final ItemStack stack) {
    if (stack.getItem() != this) {
      return -1;
    }
    return stack.getMaxDamage();
  }
}
