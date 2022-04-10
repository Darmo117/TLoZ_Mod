package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.entities.LocomotiveEntity;
import net.darmo_creations.tloz_mod.entities.TrainPartEntity;

public class LocomotiveItem extends TrainItem<LocomotiveEntity> {
  public LocomotiveItem(final TrainPartEntity.Collection collection) {
    super(LocomotiveEntity.class, collection);
  }
}
