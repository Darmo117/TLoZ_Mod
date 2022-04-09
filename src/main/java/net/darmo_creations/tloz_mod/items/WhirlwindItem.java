package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.entities.WhirlwindEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class WhirlwindItem extends TLoZItem {
  public WhirlwindItem() {
    super(new Properties().maxStackSize(1).group(TLoZ.CREATIVE_MODE_TAB));
  }

  // TODO add small cooldown after use
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (!world.isRemote) {
      double yaw = Math.toRadians(player.rotationYaw + 90);
      double dx = Math.cos(yaw);
      double dz = Math.sin(yaw);
      WhirlwindEntity whirlwindEntity = new WhirlwindEntity(world, 20,
          player.getPosX() + dx, player.getPosY() + 0.0625, player.getPosZ() + dz,
          new Vector3d(dx, 0, dz));
      world.addEntity(whirlwindEntity);
      return ActionResult.resultSuccess(stack);
    }
    return ActionResult.resultConsume(stack);
  }
}
