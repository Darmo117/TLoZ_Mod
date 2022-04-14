package net.darmo_creations.tloz_mod.entities.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

/**
 * Generic implementation of a capability provider.
 *
 * @param <C> Type of stored capability.
 * @param <S> Type of NBT tags expected for serialization.
 */
public class SimplePersistentCapabilityProvider<C, S extends INBT> implements ICapabilityProvider, INBTSerializable<S> {
  private final Capability<C> capability;
  private final LazyOptional<C> implementation;
  private final Direction direction;

  protected SimplePersistentCapabilityProvider(final Capability<C> capability, final LazyOptional<C> implementation, final Direction direction) {
    this.capability = capability;
    this.implementation = implementation;
    this.direction = direction;
  }

  /**
   * Create a provider instance for the given capability.
   *
   * @param cap  The capability instance.
   * @param impl Supplier of capability data instances.
   * @return A provider instance.
   */
  public static <C> SimplePersistentCapabilityProvider<C, INBT> from(final Capability<C> cap, final NonNullSupplier<C> impl) {
    return from(cap, null, impl);
  }

  /**
   * Create a provider instance for the given capability and block face.
   *
   * @param cap       The capability instance.
   * @param direction The block face which should provide the capability.
   * @param impl      Supplier of capability data instances.
   * @return A provider instance.
   */
  public static <C> SimplePersistentCapabilityProvider<C, INBT> from(final Capability<C> cap, final Direction direction, final NonNullSupplier<C> impl) {
    return new SimplePersistentCapabilityProvider<>(cap, LazyOptional.of(impl), direction);
  }

  @Override
  public <T> LazyOptional<T> getCapability(final Capability<T> cap, final Direction side) {
    if (cap == this.capability) {
      return this.implementation.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public S serializeNBT() {
    //noinspection unchecked
    return (S) this.capability.writeNBT(this.getInstance(), this.direction);
  }

  @Override
  public void deserializeNBT(final S nbt) {
    this.capability.readNBT(this.getInstance(), this.direction, nbt);
  }

  private C getInstance() {
    return this.implementation.orElseThrow(() -> new IllegalStateException("Unable to obtain capability instance"));
  }
}
