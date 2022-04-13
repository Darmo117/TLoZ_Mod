package net.darmo_creations.tloz_mod.entities;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;

import java.util.Optional;

public class ModDataSerializers {
  public static final IDataSerializer<Optional<Float>> OPTIONAL_FLOAT = new IDataSerializer<Optional<Float>>() {
    @Override
    public void write(PacketBuffer buf, Optional<Float> value) {
      boolean present = value.isPresent();
      buf.writeBoolean(present);
      if (present) {
        buf.writeFloat(value.get());
      }
    }

    @Override
    public Optional<Float> read(PacketBuffer buf) {
      return !buf.readBoolean() ? Optional.empty() : Optional.of(buf.readFloat());
    }

    @Override
    public Optional<Float> copyValue(Optional<Float> value) {
      return value;
    }
  };
}
