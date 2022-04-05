package net.darmo_creations.tloz_mod.tile_entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;

import java.util.Arrays;

/**
 * Renders a red box at the location of a tile entity when the player holds a specific item.
 */
public abstract class BoxTileEntityRenderer<T extends TileEntity> extends TileEntityRenderer<T> {
  public BoxTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
    super(rendererDispatcher);
  }

  @Override
  public void render(T tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
    ClientPlayerEntity player = Minecraft.getInstance().player;
    if (player != null && Arrays.stream(Hand.values()).anyMatch(hand -> player.getHeldItem(hand).getItem() == this.getItem())) {
      IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.getLines());
      float boxSize = this.getBoxSize();
      int color = this.getColor();
      float red = ((color >> 16) & 0xff) / 255f;
      float green = ((color >> 8) & 0xff) / 255f;
      float blue = (color & 0xff) / 255f;
      float alpha = 1 - ((color >> 24) & 0xff) / 255f;
      float start = 0.5f - boxSize / 2;
      float end = 0.5f + boxSize / 2;
      WorldRenderer.drawBoundingBox(
          matrixStack, vertexBuilder,
          start, start, start,
          end, end, end,
          red, green, blue, alpha
      );
    }
  }

  protected float getBoxSize() {
    return 0.5f;
  }

  protected abstract Item getItem();

  protected abstract int getColor();
}
