package net.darmo_creations.tloz_mod.entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

/**
 * Renderer for {@link BombEntity}.
 */
public class BombEntityRenderer extends EntityRenderer<BombEntity> {
  public BombEntityRenderer(EntityRendererManager renderManager) {
    super(renderManager);
    this.shadowSize = 0.5f;
  }

  @Override
  public void render(BombEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
    matrixStack.push();
    // Swelling animation
    if ((float) entity.getFuse() - partialTicks + 1 < 20) {
      float f = 1 - (entity.getFuse() - partialTicks + 1) / 10f;
      f = MathHelper.clamp(f, 0, 1);
      float scale = (float) (1 + Math.pow(f, 4) * 0.3);
      matrixStack.scale(scale, scale, scale);
    }

    double yOffset = entity.isPassenger() ? 0.5 : 0;
    matrixStack.translate(-0.5, yOffset - 0.0625, -0.5);
    Block block = entity.isPlant() ? ModBlocks.FLOWER_BOMB : ModBlocks.BOMB;
    boolean doFullBright = entity.getFuse() / 5 % 2 == 0;
    if (doFullBright) {
      // TODO make flash red
    }
    TNTMinecartRenderer.renderTntFlash(block.getDefaultState(), matrixStack, buffer, packedLight, doFullBright);
    matrixStack.pop();
    super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
  }

  @Override
  public ResourceLocation getEntityTexture(BombEntity entity) {
    return null;
  }
}
