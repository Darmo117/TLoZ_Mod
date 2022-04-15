package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.entities.trains.TrainCollection;
import net.darmo_creations.tloz_mod.entities.trains.RollingStockType;

public class TrainEngineItem extends TrainPartItem {
  public TrainEngineItem(final TrainCollection collection) {
    super(RollingStockType.ENGINE, collection);
  }
}
