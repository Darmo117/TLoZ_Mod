package net.darmo_creations.tloz_mod.tile_entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.tile_entities.SpikesEffectAreaTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;

import java.util.Arrays;

/**
 * Renders a gray box at the location of a {@link SpikesEffectAreaTileEntity}
 * when the player is holding a {@link ModBlocks#SPIKES} item.
 */
public class SpikesEffectAreaTileEntityRenderer extends BoxTileEntityRenderer<SpikesEffectAreaTileEntity> {
  public SpikesEffectAreaTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
    super(rendererDispatcher);
  }

  @Override
  protected Item getItem() {
    return ModBlocks.SPIKES.asItem();
  }

  @Override
  protected int getColor() {
    return 0x808080;
  }
}
