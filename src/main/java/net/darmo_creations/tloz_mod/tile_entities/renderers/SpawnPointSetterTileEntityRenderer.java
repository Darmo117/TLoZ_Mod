package net.darmo_creations.tloz_mod.tile_entities.renderers;

import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.tile_entities.SpawnpointSetterTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;

/**
 * Renders a red box at the location of a {@link SpawnpointSetterTileEntity}
 * when the player is holding a {@link ModBlocks#SPAWNPOINT_SETTER} item.
 */
public class SpawnPointSetterTileEntityRenderer extends BoxTileEntityRenderer<SpawnpointSetterTileEntity> {
  public SpawnPointSetterTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
    super(rendererDispatcher);
  }

  @Override
  protected Item getItem() {
    return ModBlocks.SPAWNPOINT_SETTER.asItem();
  }

  @Override
  protected int getColor() {
    return 0xff0000;
  }
}
