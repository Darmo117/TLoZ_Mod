package net.darmo_creations.tloz_mod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ModSoundEvents {
  public static final SoundEvent TELEPORT = new SoundEvent(new ResourceLocation(TLoZ.MODID, "teleporter.teleport")).setRegistryName("teleporter.teleport");
  public static final SoundEvent TELEPORTER_HUM = new SoundEvent(new ResourceLocation(TLoZ.MODID, "teleporter.hum")).setRegistryName("teleporter.hum");
}
