package net.darmo_creations.tloz_mod.tile_entities.renderers;

import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.tile_entities.SafeZoneEffectAreaTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;

/**
 * Renders a yellow box at the location of a {@link SafeZoneEffectAreaTileEntity}
 * when the player is holding a {@link ModBlocks#SAFE_ZONE} item.
 */
public class SafeZoneEffectAreaTileEntityRenderer extends BoxTileEntityRenderer<SafeZoneEffectAreaTileEntity> {
  public SafeZoneEffectAreaTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
    super(rendererDispatcher);
  }

  @Override
  protected Item getItem() {
    return ModBlocks.SAFE_ZONE.asItem();
  }

  @Override
  protected int getColor() {
    return 0xffff00;
  }
}
