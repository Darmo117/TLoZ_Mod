package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.Utils;
import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
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
 * An item that holds a certain amount of bombs. Bombs can be drawn by using the item.
 */
public class BombBagItem extends TLoZItem {
  public static final int FUSE_DELAY = 120; // 6 seconds

  /**
   * Create a bomb bag.
   *
   * @param capacity Number of bombs this bag can hold.
   */
  public BombBagItem(final int capacity) {
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
    tooltip.add(new TranslationTextComponent("item.tloz.bomb_bag.tooltip.count",
        maxDamage - damage, maxDamage).setStyle(style));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
    ItemStack item = player.getHeldItem(hand);
    int damage = item.getDamage();
    if (damage < item.getMaxDamage()) {
      if (!world.isRemote) {
        BombEntity bomb = new BombEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), FUSE_DELAY, false, false);
        world.addEntity(bomb);
        item.setDamage(damage + 1);
      }
      return new ActionResult<>(ActionResultType.SUCCESS, item);
    }
    return new ActionResult<>(ActionResultType.FAIL, item);
  }

  /**
   * Refill player’s bomb bag when they pickup a bomb ammo item.
   */
  @SubscribeEvent
  public static void onEntityItemPickup(EntityItemPickupEvent event) {
    ItemEntity itemEntity = event.getItem();
    ItemStack bombsStack = itemEntity.getItem();
    if (bombsStack.getItem() == ModItems.BOMB_AMMO) {
      PlayerEntity player = event.getPlayer();
      int bombBagIndex = Utils.getBombBagInventorySlot(player);
      if (bombBagIndex >= 0) {
        ItemStack bombBag = player.inventory.getStackInSlot(bombBagIndex);
        if (bombBag.isDamaged()) {
          bombBag.setDamage(bombBag.getDamage() - bombsStack.getCount());
        }
      }
      Utils.playItemPickupSound(player);
      itemEntity.remove();
      event.setCanceled(true);
    }
  }
}
