package net.darmo_creations.tloz_mod.entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.entities.WhirlwindEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;

public class WhirlwindEntityRenderer extends EntityRenderer<WhirlwindEntity> {
  public WhirlwindEntityRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void render(WhirlwindEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
    super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    for (int i = 0; i < 5; i++) {
      double x = entity.getPosX() + entity.world.rand.nextDouble();
      double y = entity.getPosY() + entity.world.rand.nextDouble() * 2;
      double z = entity.getPosZ() + entity.world.rand.nextDouble();
      entity.world.addParticle(ParticleTypes.CLOUD, x, y, z, 0, 0, 0);
    }
  }

  @Override
  public ResourceLocation getEntityTexture(WhirlwindEntity entity) {
    return null;
  }
}
