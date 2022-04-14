package net.darmo_creations.tloz_mod.entities.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

/**
 * Generic implementation of a capability storage.
 *
 * @param <S> Type of NBT tags expected for serialization.
 * @param <C> Type of stored capability.
 */
public class SimpleCapabilityStorage<S extends INBT, C extends INBTSerializable<S>> implements Capability.IStorage<C> {
  private final Lazy<Capability<C>> capability;
  private final int expectedTagType;

  protected SimpleCapabilityStorage(final int expectedTagType, final Lazy<Capability<C>> capability) {
    this.expectedTagType = expectedTagType;
    this.capability = capability;
  }

  /**
   * Create a storage instance for the given capability.
   *
   * @param capability      A capability instance supplier.
   * @param expectedTagType ID of the expected NBT tags for serialization.
   * @return The storage instance.
   */
  public static <S extends INBT, C extends INBTSerializable<S>> SimpleCapabilityStorage<S, C> create(final Supplier<Capability<C>> capability, final int expectedTagType) {
    return new SimpleCapabilityStorage<>(expectedTagType, Lazy.of(capability));
  }

  @Override
  public INBT writeNBT(final Capability<C> capability, final C instance, final Direction side) {
    if (capability != this.capability.get()) {
      return null;
    }
    return instance.serializeNBT();
  }

  @Override
  @SuppressWarnings("unchecked")
  public void readNBT(final Capability<C> capability, final C instance, final Direction side, final INBT nbt) {
    if (capability != this.capability.get()) {
      return;
    }
    if (this.expectedTagType != -1 && nbt.getId() != this.expectedTagType) {
      throw new IllegalStateException(String.format("The NBT type %s is not suitable for the capability %s", nbt.getClass().getSimpleName(), capability));
    }
    instance.deserializeNBT((S) nbt);
  }
}
