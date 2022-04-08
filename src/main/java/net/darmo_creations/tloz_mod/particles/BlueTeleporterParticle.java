package net.darmo_creations.tloz_mod.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlueTeleporterParticle extends SpriteTexturedParticle {
  protected BlueTeleporterParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
    super(world, x, y, z, motionX, motionY, motionZ);
    this.particleScale = 0.1F * (this.rand.nextFloat() * 0.2F + 0.5F);
    float f = this.rand.nextFloat() * 0.6F + 0.4F;
    this.particleRed = 0;
    this.particleGreen = f * 0.5f;
    this.particleBlue = f;
    this.maxAge = (int) (Math.random() * 10);
  }

  @Override
  public IParticleRenderType getRenderType() {
    return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
  }

  @Override
  public int getBrightnessForRender(float partialTick) {
    int i = super.getBrightnessForRender(partialTick);
    float f = (float) this.age / (float) this.maxAge;
    f = f * f;
    f = f * f;
    int j = i & 255;
    int k = i >> 16 & 255;
    k = k + (int) (f * 15.0F * 16.0F);
    if (k > 240) {
      k = 240;
    }
    return j | k << 16;
  }

  @Override
  public void tick() {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    if (this.age++ >= this.maxAge) {
      this.setExpired();
    } else {
      this.posY += this.motionY * 0.125;
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static class Factory implements IParticleFactory<BasicParticleType> {
    private final IAnimatedSprite spriteSet;

    public Factory(IAnimatedSprite spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle makeParticle(BasicParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      BlueTeleporterParticle particle = new BlueTeleporterParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
      particle.selectSpriteRandomly(this.spriteSet);
      return particle;
    }
  }
}
