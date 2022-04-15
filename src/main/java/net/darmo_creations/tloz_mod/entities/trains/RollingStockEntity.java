package net.darmo_creations.tloz_mod.entities.trains;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Objects;

public abstract class RollingStockEntity extends AbstractMinecartEntity {
  private static final String COLLECTION_KEY = "Collection";

  public static final DataParameter<Integer> COLLECTION = EntityDataManager.createKey(RollingStockEntity.class, DataSerializers.VARINT);

  private TrainCollection collection;

  public RollingStockEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public RollingStockEntity(EntityType<?> type, World world, TrainCollection collection, double x, double y, double z) {
    super(type, world, x, y, z);
    this.collection = Objects.requireNonNull(collection);
  }

  public TrainCollection getCollection() {
    return this.collection;
  }

  public void setCollection(TrainCollection collection) {
    this.collection = Objects.requireNonNull(collection);
    this.dataManager.set(COLLECTION, collection.ordinal());
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(COLLECTION, TrainCollection.SPIRIT.getID());
  }

  @Override
  public void notifyDataManagerChange(DataParameter<?> key) {
    super.notifyDataManagerChange(key);
    if (key == COLLECTION) {
      this.collection = TrainCollection.fromID(this.dataManager.get(COLLECTION));
    }
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    super.writeAdditional(compound);
    compound.putInt(COLLECTION_KEY, this.collection.getID());
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    this.setCollection(TrainCollection.fromID(compound.getInt(COLLECTION_KEY)));
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
