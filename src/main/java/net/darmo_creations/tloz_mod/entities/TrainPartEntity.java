package net.darmo_creations.tloz_mod.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class TrainPartEntity extends AbstractMinecartEntity {
  private static final String COLLECTION_KEY = "Collection";

  private static final DataParameter<Integer> COLLECTION = EntityDataManager.createKey(TrainPartEntity.class, DataSerializers.VARINT);

  private Collection collection;

  public TrainPartEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public TrainPartEntity(EntityType<?> type, World world, Collection collection, double x, double y, double z) {
    super(type, world, x, y, z);
    this.setCollection(collection);
  }

  private void setCollection(Collection collection) {
    this.collection = collection;
    this.dataManager.set(COLLECTION, collection.ordinal());
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(COLLECTION, Collection.SPIRIT.ordinal());
  }

  @Override
  public void notifyDataManagerChange(DataParameter<?> key) {
    super.notifyDataManagerChange(key);
    if (COLLECTION.equals(key)) {
      this.collection = Collection.values()[this.dataManager.get(COLLECTION)];
    }
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    super.writeAdditional(compound);
    compound.putInt(COLLECTION_KEY, this.collection.ordinal());
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    this.collection = Collection.values()[compound.getInt(COLLECTION_KEY)];
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  public enum Collection {
    SPIRIT,
    WOODEN,
    STEEL,
    SKULL,
    STAGECOACH,
    DRAGON,
    SWEET,
    GOLDEN
  }
}
