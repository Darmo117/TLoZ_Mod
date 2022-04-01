package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.Utils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * Custom bow item that requires a {@link QuiverItem} as an ammo sourcre and does not have durability.
 * <p>
 * Fired arrows cannot be picked up.
 */
public class QuiverBowItem extends TLoZItem {
  private final boolean lightBow;

  /**
   * Create a bow item.
   *
   * @param lightBow If true, when the bow is fully charged a light arrow is fired instead of a normal one.
   */
  public QuiverBowItem(final boolean lightBow) {
    super(new Properties().maxStackSize(1).group(TLoZ.CREATIVE_MODE_TAB));
    this.lightBow = lightBow;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    Optional<ItemStack> quiver = findAmmo(player);
    if (!player.abilities.isCreativeMode && (!quiver.isPresent() || quiver.get().getDamage() == quiver.get().getMaxDamage())) {
      return ActionResult.resultFail(itemstack);
    } else {
      player.setActiveHand(hand);
      return ActionResult.resultConsume(itemstack);
    }
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft) {
    if (entityLiving instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entityLiving;
      boolean isCreative = player.abilities.isCreativeMode;
      Optional<ItemStack> itemstack = findAmmo(player);

      int charge = this.getUseDuration(stack) - timeLeft;

      if (itemstack.isPresent() || isCreative) {
        ItemStack quiver;
        if (isCreative) {
          quiver = new ItemStack(ModItems.BIG_QUIVER);
        } else {
          quiver = itemstack.get();
        }

        if (quiver.getDamage() != quiver.getMaxDamage()) {
          float arrowVelocity = getArrowVelocity(charge);
          if (arrowVelocity >= 0.1) {
            if (!world.isRemote) {
              ArrowItem arrowItem = (ArrowItem) (this.lightBow && arrowVelocity == 1 ? Items.SPECTRAL_ARROW : Items.ARROW);
              AbstractArrowEntity arrowEntity = arrowItem.createArrow(world, new ItemStack(arrowItem), player);
              arrowEntity.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, arrowVelocity * 3, 1);
              if (arrowVelocity == 1) {
                arrowEntity.setIsCritical(true);
              }
              // Cannot pickup arrows
              arrowEntity.pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED;
              world.addEntity(arrowEntity);
            }

            world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT,
                SoundCategory.PLAYERS, 1, 1 / (random.nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);
            quiver.setDamage(quiver.getDamage() + 1);

            player.addStat(Stats.ITEM_USED.get(this));
          }
        }
      }
    }
  }

  private static Optional<ItemStack> findAmmo(PlayerEntity player) {
    int index = Utils.getQuiverInventorySlot(player);
    return index >= 0 ? Optional.of(player.inventory.mainInventory.get(index)) : Optional.empty();
  }

  /**
   * Get the velocity of the arrow entity from the bow’s charge.
   */
  private static float getArrowVelocity(int charge) {
    float f = charge / 20F;
    return Math.min(1, (f * f + f * 2) / 3);
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 72000;
  }

  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BOW;
  }
}
