package net.darmo_creations.tloz_mod.items;

import net.minecraft.item.Item;

/**
 * Base class for this mod’s items.
 * <p>
 * All items inheriting this class are immune to fire.
 */
public class TLoZItem extends Item {
  public TLoZItem(Properties properties) {
    super(properties.isImmuneToFire());
  }
}
