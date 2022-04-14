package net.darmo_creations.tloz_mod.entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.entities.AdditionalDataParameters;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;

import java.util.OptionalInt;

public class FurnaceMinecartEntityRenderer extends MinecartRenderer<FurnaceMinecartEntity> {
  public FurnaceMinecartEntityRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void render(FurnaceMinecartEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
    OptionalInt optTrainCollection = entity.getDataManager().get(AdditionalDataParameters.TRAIN_COLLECTION);
    if (optTrainCollection.isPresent()) {
      // TODO
//      System.out.println("part: " + optTrainPart);
      super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight); // TEMP while waiting for actual render implementation
    } else {
      super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }
  }
}
