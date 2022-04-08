package net.darmo_creations.tloz_mod.particles;

import net.darmo_creations.tloz_mod.Utils;
import net.minecraft.particles.BasicParticleType;

import java.util.List;

/**
 * Declares additional particles.
 */
public final class ModParticles {
  public static final BasicParticleType BLUE_TELEPORTER = (BasicParticleType) new BasicParticleType(false).setRegistryName("blue_teleporter");

  public static final List<BasicParticleType> PARTICLE_TYPES;

  static {
    PARTICLE_TYPES = Utils.gatherEntries(ModParticles.class, BasicParticleType.class);
  }

  private ModParticles() {
  }
}
