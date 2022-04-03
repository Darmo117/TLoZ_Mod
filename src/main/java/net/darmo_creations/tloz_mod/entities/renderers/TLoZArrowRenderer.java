package net.darmo_creations.tloz_mod.entities.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.darmo_creations.tloz_mod.entities.TLoZArrowEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Renderer for {@link BombEntity}.
 */
public class TLoZArrowRenderer extends EntityRenderer<TLoZArrowEntity> {
  public static final ResourceLocation ARROW_TEXTURE = new ResourceLocation("textures/entity/projectiles/arrow.png");
  public static final ResourceLocation LIGHT_ARROW_TEXTURE = new ResourceLocation("textures/entity/projectiles/spectral_arrow.png");

  public TLoZArrowRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  /**
   * Copied from {@link ArrowRenderer}.
   */
  @Override
  public void render(TLoZArrowEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
    // TODO add aura to light arrows
    matrixStack.push();
    matrixStack.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) - 90.0F));
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)));
    float f9 = (float) entity.arrowShake - partialTicks;
    if (f9 > 0.0F) {
      float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
      matrixStack.rotate(Vector3f.ZP.rotationDegrees(f10));
    }

    matrixStack.rotate(Vector3f.XP.rotationDegrees(45.0F));
    matrixStack.scale(0.05625F, 0.05625F, 0.05625F);
    matrixStack.translate(-4.0D, 0.0D, 0.0D);
    IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEntityCutout(this.getEntityTexture(entity)));
    MatrixStack.Entry matrixstack$entry = matrixStack.getLast();
    Matrix4f matrix4f = matrixstack$entry.getMatrix();
    Matrix3f matrix3f = matrixstack$entry.getNormal();
    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLight);
    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLight);
    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLight);
    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLight);
    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLight);
    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLight);
    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLight);
    this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLight);

    for (int j = 0; j < 4; ++j) {
      matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
      this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLight);
      this.drawVertex(matrix4f, matrix3f, ivertexbuilder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLight);
      this.drawVertex(matrix4f, matrix3f, ivertexbuilder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLight);
      this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLight);
    }

    matrixStack.pop();
    super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
  }

  public void drawVertex(Matrix4f matrix, Matrix3f normals, IVertexBuilder vertexBuilder, int offsetX, int offsetY, int offsetZ, float textureX, float textureY, int normalX, int p_229039_10_, int p_229039_11_, int packedLightIn) {
    vertexBuilder.pos(matrix, (float) offsetX, (float) offsetY, (float) offsetZ).color(255, 255, 255, 255).tex(textureX, textureY).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLightIn).normal(normals, (float) normalX, (float) p_229039_11_, (float) p_229039_10_).endVertex();
  }

  @Override
  public ResourceLocation getEntityTexture(TLoZArrowEntity entity) {
    return entity.isLightArrow() ? LIGHT_ARROW_TEXTURE : ARROW_TEXTURE;
  }
}
