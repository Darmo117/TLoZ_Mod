package net.darmo_creations.tloz_mod.tile_entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.particles.ModParticles;
import net.darmo_creations.tloz_mod.tile_entities.BlueLightTeleporterTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.World;

public class BlueLightTeleporterTileEntityRenderer extends TileEntityRenderer<BlueLightTeleporterTileEntity> {
  public BlueLightTeleporterTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcher) {
    super(rendererDispatcher);
  }

  @Override
  public void render(BlueLightTeleporterTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
    World world = tileEntity.getWorld();
    for (int i = 0; i < 20; i++) {
      //noinspection ConstantConditions
      double theta = world.rand.nextDouble() * 2 * Math.PI;
      double x = tileEntity.getPos().getX() + 0.5 + Math.cos(theta) * 0.5;
      double y = tileEntity.getPos().getY();
      double z = tileEntity.getPos().getZ() + 0.5 + Math.sin(theta) * 0.5;
      world.addParticle(ModParticles.BLUE_TELEPORTER, x, y, z, 0, 1, 0);
    }
  }
}
