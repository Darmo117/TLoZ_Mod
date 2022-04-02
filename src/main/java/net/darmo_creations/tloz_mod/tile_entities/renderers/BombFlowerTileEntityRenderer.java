package net.darmo_creations.tloz_mod.tile_entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.tile_entities.BombFlowerTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.client.model.data.EmptyModelData;

/**
 * Renderer for {@link BombFlowerTileEntity}.
 */
public class BombFlowerTileEntityRenderer extends TileEntityRenderer<BombFlowerTileEntity> {
  public BombFlowerTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
    super(rendererDispatcher);
  }

  @Override
  public void render(BombFlowerTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
    float growthStage = tileEntity.getGrowthStage();
    if (tileEntity.hasBomb() || growthStage > 0) {
      matrixStack.push();
      // Growing animation
      if (0 < growthStage && growthStage < 1) {
        float offset = 0.5f - growthStage / 2;
        matrixStack.translate(offset, 0, offset);
        matrixStack.scale(growthStage, growthStage, growthStage);
      }
      Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(ModBlocks.FLOWER_BOMB.getDefaultState(), matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
      matrixStack.pop();
    }
  }
}
