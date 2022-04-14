package net.darmo_creations.tloz_mod.entities.capabilities;

import net.darmo_creations.tloz_mod.entities.TrainCollection;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Objects;

/**
 * Wrapper class for {@link TrainCollection} used by {@link TrainCollectionCapabilityManager}.
 */
public class TrainCollectionWrapper implements INBTSerializable<CompoundNBT> {
  private static final String COLLECTION_KEY = "Collection";

  private TrainCollection collection = TrainCollection.SPIRIT;

  /**
   * The {@link TrainCollection} wrapped by this object.
   */
  public TrainCollection getCollection() {
    return this.collection;
  }

  /**
   * Set the {@link TrainCollection} wrapped by this object.
   *
   * @param collection The new value. May not be null.
   */
  public void setCollection(TrainCollection collection) {
    this.collection = Objects.requireNonNull(collection);
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt(COLLECTION_KEY, this.collection.ordinal());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.collection = TrainCollection.values()[nbt.getInt(COLLECTION_KEY) % TrainCollection.values().length];
  }
}
