package net.darmo_creations.tloz_mod.tile_entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.tile_entities.SafeZoneEffectAreaTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Hand;

import java.util.Arrays;

/**
 * Renders a yellow box at the location of a {@link SafeZoneEffectAreaTileEntity}
 * when the player is holding a {@link ModBlocks#SAFE_ZONE} item.
 */
public class SafeZoneEffectAreaTileEntityRenderer extends TileEntityRenderer<SafeZoneEffectAreaTileEntity> {
  public static final float BOX_SIZE = 0.5f;

  public SafeZoneEffectAreaTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
    super(rendererDispatcher);
  }

  @Override
  public void render(SafeZoneEffectAreaTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
    ClientPlayerEntity player = Minecraft.getInstance().player;
    if (player != null && Arrays.stream(Hand.values()).anyMatch(hand -> player.getHeldItem(hand).getItem() == ModBlocks.SAFE_ZONE.asItem())) {
      IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.getLines());
      float start = 0.5f - BOX_SIZE / 2;
      float end = 0.5f + BOX_SIZE / 2;
      WorldRenderer.drawBoundingBox(matrixStack, vertexBuilder, start, start, start, end, end, end, 1, 1, 0, 1);
    }
  }
}
