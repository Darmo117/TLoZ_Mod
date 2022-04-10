package net.darmo_creations.tloz_mod.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class LocomotiveEntity extends TrainPartEntity {
  public LocomotiveEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  @SuppressWarnings("unused")
  public LocomotiveEntity(World world, Collection collection, double x, double y, double z) {
    super(world, collection, x, y, z);
  }

  @Override
  public double getMountedYOffset() {
    return 1;
  }

  @Override
  public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
    if (player.isSecondaryUseActive() || this.isBeingRidden()) {
      return ActionResultType.PASS;
    } else if (!this.world.isRemote) {
      return player.startRiding(this) ? ActionResultType.CONSUME : ActionResultType.PASS;
    } else {
      return ActionResultType.SUCCESS;
    }
  }

  @Override
  public void tick() {
    super.tick();
    // TODO set speed and direction depending on some special items in the rider’s inventory
    // FIXME movement lag
  }

  @Override
  public Type getMinecartType() {
    return Type.RIDEABLE;
  }
}
