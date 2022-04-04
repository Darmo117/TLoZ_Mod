package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.ItemBulbEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Dummy block used to render {@link ItemBulbFlowerBlock} and {@link ItemBulbEntity}.
 */
public class DummyItemBulbBlock extends Block implements IModBlock {
  public DummyItemBulbBlock() {
    super(Properties.create(Material.MISCELLANEOUS));
  }

  @Override
  public boolean hasGeneratedItemBlock() {
    return false;
  }
}
