package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.entities.trains.TrainCollection;
import net.darmo_creations.tloz_mod.entities.trains.RollingStockType;

public class TrainFreightCarItem extends TrainPartItem {
  public TrainFreightCarItem(final TrainCollection collection) {
    super(RollingStockType.FREIGHT_CAR, collection);
  }
}
