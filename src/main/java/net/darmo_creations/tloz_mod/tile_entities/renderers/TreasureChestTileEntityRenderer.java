package net.darmo_creations.tloz_mod.tile_entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.blocks.TreasureChestBlock;
import net.darmo_creations.tloz_mod.tile_entities.TreasureChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Optional;

/**
 * Displays the item currently contained within a {@link TreasureChestTileEntity}.
 */
public class TreasureChestTileEntityRenderer extends TileEntityRenderer<TreasureChestTileEntity> {
  public TreasureChestTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
    super(rendererDispatcher);
  }

  @Override
  public void render(TreasureChestTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
    BlockState blockState = tileEntity.getBlockState();
    PlayerEntity player = Minecraft.getInstance().player;
    //noinspection ConstantConditions
    if (blockState.get(TreasureChestBlock.MAIN) && player.isCreative() && player.isCrouching()) {
      Optional<ItemStack> loot = tileEntity.getLoot();
      if (loot.isPresent()) {
        matrixStack.push();
        Vector3d offset = TreasureChestBlock.getItemSpawnOffset(blockState, ((TreasureChestBlock) blockState.getBlock()).isDouble());
        matrixStack.translate(offset.x, offset.y, offset.z);
        Minecraft.getInstance().getItemRenderer().renderItem(loot.get(), ItemCameraTransforms.TransformType.GROUND, combinedLight, OverlayTexture.NO_OVERLAY, matrixStack, buffer);
        matrixStack.pop();
      }
    }
  }
}
