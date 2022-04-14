package net.darmo_creations.tloz_mod.entities.capabilities;

import net.darmo_creations.tloz_mod.blocks.BlueLightTeleporter;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Objects;
import java.util.Optional;

/**
 * An object that holds data used by {@link BlueLightTeleporter}s through capabilities.
 *
 * @see TeleportDataCapabilityManager
 */
public class TeleportData implements INBTSerializable<CompoundNBT> {
  private static final String DELAY_KEY = "Delay";
  private static final String TARGET_POS_KEY = "TargetPosition";
  private static final String TARGET_YAW_KEY = "TargetYaw";
  private static final String TARGET_PITCH_KEY = "TargetPitch";

  private Integer delay;
  private BlockPos targetPosition;
  private Float targetYaw;
  private Float targetPitch;

  /**
   * The remaining delay before player teleportation should occur.
   */
  public Optional<Integer> getDelay() {
    return Optional.ofNullable(this.delay);
  }

  /**
   * Set the remaining delay before player teleportation should occur.
   *
   * @param delay The new delay. Must be ≥ 0.
   */
  public void setDelay(int delay) {
    if (delay < 0) {
      throw new IllegalArgumentException("delay should be >= 0");
    }
    this.delay = delay;
  }

  /**
   * The position the player should be teleported to once delay reaches 0.
   */
  public Optional<BlockPos> getTargetPosition() {
    return Optional.ofNullable(this.targetPosition);
  }

  /**
   * Set the position the player should be teleported to once delay reaches 0.
   *
   * @param targetPosition The new target position. May not be null.
   */
  public void setTargetPosition(BlockPos targetPosition) {
    this.targetPosition = Objects.requireNonNull(targetPosition);
  }

  /**
   * The yaw which the player should have after being teleported.
   */
  public Optional<Float> getTargetYaw() {
    return Optional.ofNullable(this.targetYaw);
  }

  /**
   * Set the yaw which the player should have after being teleported.
   *
   * @param targetYaw The new target yaw angle. May be null.
   */
  public void setTargetYaw(Float targetYaw) {
    this.targetYaw = targetYaw;
  }

  /**
   * The pitch which the player should have after being teleported.
   */
  public Optional<Float> getTargetPitch() {
    return Optional.ofNullable(this.targetPitch);
  }

  /**
   * Set the pitch which the player should have after being teleported.
   *
   * @param targetPitch The new target pitch angle. May be null.
   */
  public void setTargetPitch(Float targetPitch) {
    this.targetPitch = targetPitch;
  }

  /**
   * Set all data to null.
   */
  public void reset() {
    this.delay = null;
    this.targetPosition = null;
    this.targetYaw = null;
    this.targetPitch = null;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    this.getDelay().ifPresent(delay -> nbt.putInt(DELAY_KEY, delay));
    this.getTargetPosition().ifPresent(blockPos -> nbt.put(TARGET_POS_KEY, NBTUtil.writeBlockPos(blockPos)));
    this.getTargetYaw().ifPresent(yaw -> nbt.putFloat(TARGET_YAW_KEY, yaw));
    this.getTargetPitch().ifPresent(pitch -> nbt.putFloat(TARGET_PITCH_KEY, pitch));
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.delay = nbt.contains(DELAY_KEY) ? nbt.getInt(DELAY_KEY) : null;
    this.targetPosition = nbt.contains(TARGET_POS_KEY) ? NBTUtil.readBlockPos(nbt.getCompound(TARGET_POS_KEY)) : null;
    this.targetYaw = nbt.contains(TARGET_YAW_KEY) ? nbt.getFloat(TARGET_YAW_KEY) : null;
    this.targetPitch = nbt.contains(TARGET_PITCH_KEY) ? nbt.getFloat(TARGET_PITCH_KEY) : null;
  }
}
