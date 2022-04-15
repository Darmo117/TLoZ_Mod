package net.darmo_creations.tloz_mod.entities.trains;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class TrainEngineEntity extends RollingStockEntity {
  private static final String SPEED_SETTING_KEY = "SpeedSetting";

  public static final DataParameter<Integer> SPEED_SETTING = EntityDataManager.createKey(TrainEngineEntity.class, DataSerializers.VARINT);

  public static final double DRAG = 0.85;

  private TrainSpeedSetting speedSetting;
  /**
   * Vector representing the orientation of the push vector. Always positive.
   */
  private Vector3d pushOrientation;
  /**
   * Value representing the direction (+1, 0, or -1) of the push force.
   */
  private double pushDirection;

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
    this.pushDirection = speedSetting.getDirection();
    double yaw = Math.toRadians(this.rotationYaw);
    this.pushOrientation = new Vector3d(Math.cos(yaw), 0, Math.sin(yaw));
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
  protected void moveAlongTrack(BlockPos pos, BlockState state) {
    super.moveAlongTrack(pos, state);
    Vector3d motion = this.getMotion();
    double motionMagnitudeSq = horizontalMag(motion);
    double pushMagnitudeSq = horizontalMag(this.pushOrientation);
    if (pushMagnitudeSq > 1e-4 && motionMagnitudeSq > 0.001) {
      double motionMagnitude = MathHelper.sqrt(motionMagnitudeSq);
      double pushMagnitude = MathHelper.sqrt(pushMagnitudeSq);
      // Update orientation to account for rail corners
      this.pushOrientation = motion.scale(pushMagnitude / motionMagnitude);
    }
  }

  // TODO add inertia to direction changes
  @Override
  protected void applyDrag() {
    double pushMagnitudeSq = horizontalMag(this.pushOrientation);
    if (pushMagnitudeSq > 1e-7) {
      double pushMagnitude = MathHelper.sqrt(pushMagnitudeSq);
      double speedCoefficient = this.speedSetting.getMagnitude() / 20.0;
      this.pushOrientation = this.pushOrientation.scale(speedCoefficient / pushMagnitude);
      // Apply push direction to vector
      Vector3d force = new Vector3d(Math.abs(this.pushOrientation.x) * this.pushDirection, 0, Math.abs(this.pushOrientation.z) * this.pushDirection);
      this.setMotion(this.getMotion().mul(DRAG, 0, DRAG).add(force));
    } else {
      this.setMotion(this.getMotion().mul(DRAG, 0, DRAG));
    }
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
