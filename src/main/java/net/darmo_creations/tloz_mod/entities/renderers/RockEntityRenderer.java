package net.darmo_creations.tloz_mod.entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.entities.RockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;

/**
 * Renderer for {@link RockEntity}.
 */
public class RockEntityRenderer extends EntityRenderer<RockEntity> {
  public RockEntityRenderer(EntityRendererManager renderManager) {
    super(renderManager);
    this.shadowSize = 0.5f;
  }

  @Override
  public void render(RockEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
    matrixStack.push();
    double yOffset = entity.isPassenger() ? 0.5 : 0;
    matrixStack.translate(-0.5, yOffset, -0.5);
    Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(ModBlocks.ROCK.getDefaultState(), matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
    matrixStack.pop();
    super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
  }

  @Override
  public ResourceLocation getEntityTexture(RockEntity entity) {
    return null;
  }
}
