package net.darmo_creations.tloz_mod.tile_entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.tile_entities.BombBreakableBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.client.model.data.EmptyModelData;

/**
 * Renderer for {@link BombBreakableBlockTileEntity}.
 */
public class BombBreakableBlockTileEntityRenderer extends TileEntityRenderer<BombBreakableBlockTileEntity> {
  public BombBreakableBlockTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
    super(rendererDispatcher);
  }

  @Override
  public void render(BombBreakableBlockTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
    BlockState state = tileEntity.getBlock().map(Block::getDefaultState).orElse(Blocks.STONE.getDefaultState());
    Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(state, matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
  }
}
