package net.darmo_creations.tloz_mod.tile_entities.renderers;

import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.tile_entities.KillTriggerTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;

/**
 * Renders a red box at the location of a {@link KillTriggerTileEntity}
 * when the player is holding a {@link ModBlocks#KILL_TRIGGER} item.
 */
public class KillTriggerTileEntityRenderer extends BoxTileEntityRenderer<KillTriggerTileEntity> {
  public KillTriggerTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
    super(rendererDispatcher);
  }

  @Override
  protected Item getItem() {
    return ModBlocks.KILL_TRIGGER.asItem();
  }

  @Override
  protected int getColor() {
    return 0xff0000;
  }
}
