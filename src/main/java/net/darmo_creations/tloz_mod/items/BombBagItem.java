package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * An item that holds a certain amount of bombs. Bombs can be drawn by using the item.
 */
public class BombBagItem extends SimpleContainerItem {
  public static final int FUSE_DELAY = 120; // 6 seconds

  /**
   * Create a bomb bag.
   *
   * @param capacity Number of bombs this bag can hold.
   */
  public BombBagItem(final int capacity) {
    super(capacity);
  }

  @Override
  protected String getUnlocalizedTooltipName() {
    return "bomb_bag";
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

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (!this.isEmpty(stack)) {
      if (!world.isRemote) {
        BombEntity bomb = new BombEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), FUSE_DELAY, false, false, player);
        world.addEntity(bomb);
        this.remove(stack, 1);
      }
      return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }
    return new ActionResult<>(ActionResultType.FAIL, stack);
  }
}
