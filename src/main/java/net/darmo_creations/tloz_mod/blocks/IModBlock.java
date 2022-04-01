package net.darmo_creations.tloz_mod.blocks;

/**
 * This interface should be implemented by all block classes declared in this mod.
 * It defines a method that is usefull for automatic item generation.
 */
public interface IModBlock {
  /**
   * Whether this block should have an auto-generated item.
   * Default: true.
   */
  default boolean hasGeneratedItemBlock() {
    return true;
  }
}
