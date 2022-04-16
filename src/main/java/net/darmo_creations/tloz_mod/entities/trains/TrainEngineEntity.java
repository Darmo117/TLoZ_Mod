package net.darmo_creations.tloz_mod.entities.trains;

import net.darmo_creations.tloz_mod.Utils;
import net.darmo_creations.tloz_mod.entities.ModEntities;
import net.darmo_creations.tloz_mod.items.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class TrainEngineEntity extends RollingStockEntity {
  private static final String SPEED_SETTING_KEY = "SpeedSetting";

  public static final DataParameter<Integer> SPEED_SETTING = EntityDataManager.createKey(TrainEngineEntity.class, DataSerializers.VARINT);

  /**
   * Drag due to rail and air friction.
   */
  public static final double FRICTION_DRAG = 0.99;
  /**
   * Drag to apply when braking, i.e. when the actual speed is above the desired speed.
   */
  public static final double BRAKE_DRAG = 0.97;
  /**
   * Quantity to multiply to the desired direction to obtain the base push force.
   */
  public static final double BASE_ABS_PUSH_FORCE = 0.01;
  /**
   * Amount of speed to multiply to the desired magnitude the desired absolute speed.
   */
  public static final double BASE_ABS_SPEED = 0.4;
  /**
   * Speed below which the engine should stop.
   */
  public static final double STOP_THRESHOLD = 1e-3;

  private TrainSpeedSetting speedSetting;

  public TrainEngineEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public TrainEngineEntity(World world, TrainCollection collection, double x, double y, double z) {
    super(ModEntities.TRAIN_ENGINE.get(), world, collection, x, y, z);
    this.setSpeedSetting(TrainSpeedSetting.IDLE);
  }

  @Override
  public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
    if (player.isSecondaryUseActive() || this.isBeingRidden()) {
      return ActionResultType.PASS;
    } else if (!this.world.isRemote) {
      return player.startRiding(this) ? ActionResultType.CONSUME : ActionResultType.PASS;
    } else {
      return ActionResultType.SUCCESS;
    }
  }

  public TrainSpeedSetting getSpeedSetting() {
    return this.speedSetting;
  }

  public void setSpeedSetting(TrainSpeedSetting speedSetting) {
    this.speedSetting = Objects.requireNonNull(speedSetting);
    this.dataManager.set(SPEED_SETTING, speedSetting.getID());
  }

  // TODO set speed to IDLE when minecart hits block
  @Override
  public void tick() {
    super.tick();
    List<Entity> passengers = this.getPassengers();
    boolean isPlayerRiding = !passengers.isEmpty() && passengers.get(0) instanceof PlayerEntity;
    if (!this.world.isRemote()) {
      if (!isPlayerRiding) {
        this.setSpeedSetting(TrainSpeedSetting.IDLE);
      }
    }

    if (isPlayerRiding && this.rand.nextInt(4) == 0) {
      // TODO adjust particles position to entity model
      this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getPosX(), this.getPosY() + 0.8, this.getPosZ(), 0, 0, 0);
    }
  }

  @Override
  protected void applyDrag() {
    double motionX = this.getMotion().getX() * FRICTION_DRAG;
    double motionZ = this.getMotion().getZ() * FRICTION_DRAG;
    double force = BASE_ABS_PUSH_FORCE * this.speedSetting.getDirection();
    double yaw = Math.toRadians(this.rotationYaw);
    motionX += Math.cos(yaw) * force;
    motionZ += Math.sin(yaw) * force;
    double targetAbsoluteSpeed = BASE_ABS_SPEED * this.speedSetting.getMagnitude();
    // We’re going above the desired speed, brake
    if (Math.abs(motionX) > targetAbsoluteSpeed) {
      motionX *= BRAKE_DRAG;
    }
    if (Math.abs(motionZ) > targetAbsoluteSpeed) {
      motionZ *= BRAKE_DRAG;
    }
    // We’re going too slow, stop
    if (Math.abs(motionX) < STOP_THRESHOLD) {
      motionX = 0;
    }
    if (Math.abs(motionZ) < STOP_THRESHOLD) {
      motionZ = 0;
    }
    this.setMotion(motionX, 0, motionZ);
    Utils.print("motion:", this.getMotion()); // DEBUG
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(SPEED_SETTING, TrainSpeedSetting.IDLE.getID());
  }

  @Override
  public void notifyDataManagerChange(DataParameter<?> key) {
    super.notifyDataManagerChange(key);
    if (key == SPEED_SETTING) {
      this.speedSetting = TrainSpeedSetting.fromID(this.dataManager.get(SPEED_SETTING));
    }
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    super.writeAdditional(compound);
    compound.putInt(SPEED_SETTING_KEY, this.speedSetting.getID());
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    this.setSpeedSetting(TrainSpeedSetting.fromID(compound.getInt(SPEED_SETTING_KEY)));
  }

  @Override
  public double getMountedYOffset() {
    return 0.5;
  }

  @Override
  public Type getMinecartType() {
    return Type.RIDEABLE;
  }

  // TEMP remove once renderer is implemented
  @Override
  public BlockState getDefaultDisplayTile() {
    return Blocks.RED_CARPET.getDefaultState();
  }

  @Override
  public ItemStack getCartItem() {
    // TODO
    switch (this.getCollection()) {
      case SPIRIT:
        return new ItemStack(ModItems.SPIRIT_ENGINE);
      case WOODEN:
        break;
      case STEEL:
        break;
      case SKULL:
        break;
      case STAGECOACH:
        break;
      case DRAGON:
        break;
      case SWEET:
        break;
      case GOLDEN:
        break;
    }
    return null;
  }
}
