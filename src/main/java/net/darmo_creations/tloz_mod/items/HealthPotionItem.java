package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DrinkHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/**
 * A drinkable item that restore a certain amount of health to the player that drinks it.
 */
public class HealthPotionItem extends TLoZItem {
  private final int healAmount;

  /**
   * Create a healing potion item.
   *
   * @param healAmount The number of hearts to restore upon player consuming this item.
   */
  public HealthPotionItem(final int healAmount) {
    super(new Properties().maxStackSize(1).group(TLoZ.CREATIVE_MODE_TAB));
    this.healAmount = healAmount;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
    PlayerEntity player = entityLiving instanceof PlayerEntity ? (PlayerEntity) entityLiving : null;
    if (player instanceof ServerPlayerEntity) {
      CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
    }

    if (player != null) {
      if (!world.isRemote) {
        player.heal(this.healAmount * 2);
      }
      player.addStat(Stats.ITEM_USED.get(this));
      stack.shrink(1);
    }

    return stack;
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 10;
  }

  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.DRINK;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity playerIn, Hand hand) {
    return DrinkHelper.startDrinking(world, playerIn, hand);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
    if (this.healAmount < 100) {
      tooltip.add(new TranslationTextComponent("item.tloz.potion.tooltip.amount", this.healAmount));
    } else {
      tooltip.add(new TranslationTextComponent("item.tloz.potion.tooltip.all"));
    }
  }
}
