package net.darmo_creations.tloz_mod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.util.List;

/**
 * Declares custom sound events.
 */
public final class ModSoundEvents {
  public static final SoundEvent TELEPORT = new SoundEvent(new ResourceLocation(TLoZ.MODID, "teleporter.teleport")).setRegistryName("teleporter.teleport");
  public static final SoundEvent TELEPORTER_HUM = new SoundEvent(new ResourceLocation(TLoZ.MODID, "teleporter.hum")).setRegistryName("teleporter.hum");

  /**
   * List of all available sound events.
   */
  public static final List<SoundEvent> SOUND_EVENTS;

  static {
    SOUND_EVENTS = Utils.gatherEntries(ModSoundEvents.class, SoundEvent.class);
  }

  private ModSoundEvents() {
  }
}
