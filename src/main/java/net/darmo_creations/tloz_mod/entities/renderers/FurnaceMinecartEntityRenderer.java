package net.darmo_creations.tloz_mod.entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.entities.TrainCollection;
import net.darmo_creations.tloz_mod.entities.capabilities.TrainCollectionCapabilityManager;
import net.darmo_creations.tloz_mod.entities.capabilities.TrainCollectionWrapper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraftforge.common.util.LazyOptional;

public class FurnaceMinecartEntityRenderer extends MinecartRenderer<FurnaceMinecartEntity> {
  public FurnaceMinecartEntityRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  public void render(FurnaceMinecartEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
    LazyOptional<TrainCollectionWrapper> optTrainCollection = entity.getCapability(TrainCollectionCapabilityManager.INSTANCE);
    if (optTrainCollection.isPresent()) {
      // TODO
      TrainCollection collection = optTrainCollection.orElseGet(TrainCollectionWrapper::new).getCollection();
//      System.out.println("collection: " + collection); // DEBUG
      super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight); // TEMP while no actual render implementation
    } else {
      super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }
  }
}
