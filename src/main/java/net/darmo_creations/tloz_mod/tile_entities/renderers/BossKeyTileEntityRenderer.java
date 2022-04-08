package net.darmo_creations.tloz_mod.tile_entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.tile_entities.BossKeyTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.client.model.data.EmptyModelData;

/**
 * Renderer for {@link BossKeyTileEntity}.
 */
public class BossKeyTileEntityRenderer extends TileEntityRenderer<BossKeyTileEntity> {
  public BossKeyTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
    super(rendererDispatcher);
  }

  @Override
  public void render(BossKeyTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
    Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(ModBlocks.BOSS_KEY.getDefaultState(), matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
  }
}
