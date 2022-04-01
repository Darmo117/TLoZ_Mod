package net.darmo_creations.tloz_mod;

import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Creative mode tab for this mod.
 */
public class CreativeTab extends ItemGroup {
  public CreativeTab() {
    super(TLoZ.MODID);
  }

  @Override
  public ItemStack createIcon() {
    return new ItemStack(ModBlocks.SHOCK_SWITCH);
  }
}
