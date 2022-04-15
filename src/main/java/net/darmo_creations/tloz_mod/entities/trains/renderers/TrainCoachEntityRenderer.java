package net.darmo_creations.tloz_mod.entities.trains.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.entities.trains.TrainCoachEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MinecartRenderer;

public class TrainCoachEntityRenderer extends MinecartRenderer<TrainCoachEntity> {
  public TrainCoachEntityRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void render(TrainCoachEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
    // TEMP while no actual render implementation
    super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
  }
}
