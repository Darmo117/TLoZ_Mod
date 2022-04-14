package net.darmo_creations.tloz_mod.entities;

import net.minecraft.entity.item.minecart.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.OptionalInt;

/**
 * List of all train parts.
 */
public enum TrainPart {
  ENGINE(FurnaceMinecartEntity::new),
  CANNON(CommandBlockMinecartEntity::new),
  COACH(MinecartEntity::new),
  FREIGHT_CAR(ChestMinecartEntity::new);

  private final MinecartEntityProvider<? extends AbstractMinecartEntity> minecartminecartEntityProvider;

  TrainPart(final MinecartEntityProvider<? extends AbstractMinecartEntity> minecartminecartEntityProvider) {
    this.minecartminecartEntityProvider = minecartminecartEntityProvider;
  }

  /**
   * Create a minecart entity for this train part with all additional data parameters set to their correct value.
   *
   * @param world           The current world.
   * @param pos             Position to spawn the minecart at.
   * @param isRailAscending Whether the rail the minecart will spawn on is ascending.
   * @param collection      The train collection for the new minecart.
   * @return A new minecart entity.
   */
  public AbstractMinecartEntity createMinecartEntity(World world, BlockPos pos, boolean isRailAscending, TrainCollection collection) {
    double x = pos.getX() + 0.5;
    double y = pos.getY() + 0.0625 + (isRailAscending ? 0.5 : 0);
    double z = pos.getZ() + 0.5;
    AbstractMinecartEntity minecart = this.minecartminecartEntityProvider.apply(world, x, y, z);
    minecart.getDataManager().set(AdditionalDataParameters.TRAIN_COLLECTION, OptionalInt.of(collection.ordinal()));
    return minecart;
  }

  /**
   * Custom functional interface for enum constants.
   */
  @FunctionalInterface
  private interface MinecartEntityProvider<T extends AbstractMinecartEntity> {
    T apply(World world, double x, double y, double z);
  }
}
