package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.entities.TrainCollection;
import net.darmo_creations.tloz_mod.entities.TrainPart;

public class TrainCoachItem extends TrainPartItem {
  public TrainCoachItem(final TrainCollection collection) {
    super(TrainPart.COACH, collection);
  }
}
