package net.darmo_creations.tloz_mod.entities.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerEntityTeleportPositionCapability<U extends INBT> implements ICapabilitySerializable<U> {
  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    return null;
  }

  @Override
  public U serializeNBT() {
    return null;
  }

  @Override
  public void deserializeNBT(U nbt) {

  }
}
