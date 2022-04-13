package net.darmo_creations.tloz_mod;

import net.minecraft.nbt.CompoundNBT;

/**
 * Enables serialization and deserialization of objects into and from NBT tags.
 */
public interface NBTSerializable {
  /**
   * Serialize this object into an NBT tag.
   *
   * @return The serialized data.
   */
  CompoundNBT writeToNBT();

  /**
   * Update this object using data in the given tag.
   *
   * @param tag The data to deserialize.
   */
  void readFromNBT(CompoundNBT tag);
}
