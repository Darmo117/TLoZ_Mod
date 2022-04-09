package net.darmo_creations.tloz_mod.entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.entities.WhirlwindEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class WhirlwindEntityRenderer extends EntityRenderer<WhirlwindEntity> {
  public WhirlwindEntityRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void render(WhirlwindEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
    super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    // TODO spawn particles
  }

  @Override
  public ResourceLocation getEntityTexture(WhirlwindEntity entity) {
    return null;
  }
}
