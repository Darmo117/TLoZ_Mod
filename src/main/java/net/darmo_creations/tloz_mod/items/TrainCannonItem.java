package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.entities.trains.TrainCollection;
import net.darmo_creations.tloz_mod.entities.trains.RollingStockType;

public class TrainCannonItem extends TrainPartItem {
  public TrainCannonItem(final TrainCollection collection) {
    super(RollingStockType.CANNON, collection);
  }
}
