package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.entities.WhirlwindEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class WhirlwindItem extends TLoZItem {
  public WhirlwindItem() {
    super(new Properties().maxDamage(WhirlwindEntity.WHIRLWIND_DEFAULT_DURATION).group(TLoZ.CREATIVE_MODE_TAB));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (stack.getDamage() != 0) {
      return new ActionResult<>(ActionResultType.FAIL, stack);
    }
    if (!world.isRemote) {
      double yaw = Math.toRadians(player.rotationYaw + 90);
      double dx = Math.cos(yaw);
      double dz = Math.sin(yaw);
      WhirlwindEntity whirlwindEntity = new WhirlwindEntity(world, WhirlwindEntity.WHIRLWIND_DEFAULT_DURATION,
          player.getPosX() + dx, player.getPosY(), player.getPosZ() + dz, new Vector3d(dx, 0, dz));
      world.addEntity(whirlwindEntity);
      stack.setDamage(WhirlwindEntity.WHIRLWIND_DEFAULT_DURATION);
      // TODO sound
    }
    return new ActionResult<>(ActionResultType.SUCCESS, stack);
  }

  @Override
  public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
    int cooldown = stack.getDamage();
    if (cooldown > 0) {
      stack.setDamage(cooldown - 1);
    }
  }
}
