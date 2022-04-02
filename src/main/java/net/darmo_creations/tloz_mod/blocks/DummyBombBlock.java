package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Dummy block used to render {@link BombFlowerBlock} and {@link BombEntity}.
 */
public class DummyBombBlock extends Block implements IModBlock {
  public DummyBombBlock() {
    super(AbstractBlock.Properties.create(Material.MISCELLANEOUS));
  }

  @Override
  public boolean hasGeneratedItemBlock() {
    return false;
  }
}
