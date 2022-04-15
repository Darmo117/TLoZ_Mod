package net.darmo_creations.tloz_mod.entities.trains;

/**
 * Lists all available train variants.
 */
public enum TrainCollection {
  SPIRIT,
  WOODEN,
  STEEL,
  SKULL,
  STAGECOACH,
  DRAGON,
  SWEET,
  GOLDEN;

  /**
   * Return the ID of this collection.
   */
  public int getID() {
    return this.ordinal();
  }

  /**
   * Return the collection for the given ID.
   *
   * @param id Collection’s ID
   * @return The collection.
   */
  public static TrainCollection fromID(final int id) {
    return values()[id % values().length];
  }
}
