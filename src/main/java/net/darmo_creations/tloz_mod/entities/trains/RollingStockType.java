package net.darmo_creations.tloz_mod.entities.trains;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * List of all rolling stock types.
 */
public enum RollingStockType {
  ENGINE(TrainEngineEntity::new),
  CANNON(RailCannonEntity::new),
  COACH(TrainCoachEntity::new),
  FREIGHT_CAR(FreightCarEntity::new);

  private final MinecartEntityProvider<? extends RollingStockEntity> minecartminecartEntityProvider;

  RollingStockType(final MinecartEntityProvider<? extends RollingStockEntity> minecartminecartEntityProvider) {
    this.minecartminecartEntityProvider = minecartminecartEntityProvider;
  }

  /**
   * Create a minecart entity for this rolling stock type with all additional data parameters set to their correct value.
   *
   * @param world           The current world.
   * @param pos             Position to spawn the minecart at.
   * @param isRailAscending Whether the rail the minecart will spawn on is ascending.
   * @param collection      The train collection for the new minecart.
   * @return A new minecart entity.
   */
  public RollingStockEntity createMinecartEntity(World world, BlockPos pos, boolean isRailAscending, TrainCollection collection) {
    double x = pos.getX() + 0.5;
    double y = pos.getY() + 0.0625 + (isRailAscending ? 0.5 : 0);
    double z = pos.getZ() + 0.5;
    return this.minecartminecartEntityProvider.apply(world, collection, x, y, z);
  }

  /**
   * Custom functional interface for enum constants.
   */
  @FunctionalInterface
  private interface MinecartEntityProvider<T extends RollingStockEntity> {
    T apply(World world, TrainCollection collection, double x, double y, double z);
  }
}
