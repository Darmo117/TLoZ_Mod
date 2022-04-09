package net.darmo_creations.tloz_mod.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class WhirlwindEntity extends Entity {
  private static final String MAX_AGE_KEY = "MaxAge";
  private static final String AGE_KEY = "Age";

  private static final DataParameter<Integer> MAX_AGE = EntityDataManager.createKey(WhirlwindEntity.class, DataSerializers.VARINT);
  private static final DataParameter<Integer> AGE = EntityDataManager.createKey(WhirlwindEntity.class, DataSerializers.VARINT);

  private int maxAge;
  private int age;

  public WhirlwindEntity(EntityType<?> entityType, World world) {
    super(entityType, world);
  }

  public WhirlwindEntity(World world, final int maxAge, double x, double y, double z, final Vector3d direction) {
    super(ModEntities.WHIRLWIND.get(), world);
    this.setPosition(x, y, z);
    this.prevPosX = x;
    this.prevPosY = y;
    this.prevPosZ = z;
    this.setMotion(direction.scale(0.5));
    this.setMaxAge(maxAge);
    this.setAge(0);
  }

  @Override
  public void tick() {
    super.tick();
    if (this.age >= this.maxAge) {
      this.remove();
      return;
    }
    List<Entity> entities = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox(), entity -> !entity.isPassenger());
    if (!entities.isEmpty()) {
      // Pick up and move encountered entities
      entities.forEach(entity -> {
        if (entity instanceof PickableEntity) {
          entity.startRiding(this, true);
        } // TODO move and stun custom monsters
      });
    }
    Vector3d prevMotion = this.getMotion();
    this.move(MoverType.SELF, this.getMotion());
//    if (!prevMotion.equals(this.getMotion())) {
//      this.remove();
//      return;
//    }
    this.setAge(this.age + 1);
  }

  public void setMaxAge(int maxAge) {
    this.maxAge = maxAge;
    this.dataManager.set(MAX_AGE, maxAge);
  }

  public void setAge(int age) {
    this.age = age;
    this.dataManager.set(AGE, age);
  }

  @Override
  public void notifyDataManagerChange(DataParameter<?> key) {
    super.notifyDataManagerChange(key);
    if (MAX_AGE.equals(key)) {
      this.maxAge = this.dataManager.get(MAX_AGE);
    } else if (AGE.equals(key)) {
      this.age = this.dataManager.get(AGE);
    }
  }

  @Override
  protected void registerData() {
    this.dataManager.register(MAX_AGE, 20);
    this.dataManager.register(AGE, 0);
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    compound.putInt(MAX_AGE_KEY, this.maxAge);
    compound.putInt(AGE_KEY, this.age);
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    this.setMaxAge(compound.getInt(MAX_AGE_KEY));
    this.setAge(compound.getInt(AGE_KEY));
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
