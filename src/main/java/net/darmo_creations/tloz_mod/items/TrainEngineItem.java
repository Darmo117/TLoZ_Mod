package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.entities.TrainCollection;
import net.darmo_creations.tloz_mod.entities.TrainPart;

public class TrainEngineItem extends TrainPartItem {
  public TrainEngineItem(final TrainCollection collection) {
    super(TrainPart.ENGINE, collection);
  }
}