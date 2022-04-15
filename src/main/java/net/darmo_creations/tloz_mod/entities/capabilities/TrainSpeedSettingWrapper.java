package net.darmo_creations.tloz_mod.entities.capabilities;

import net.darmo_creations.tloz_mod.entities.TrainSpeedSetting;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Objects;

/**
 * Wrapper class for {@link TrainSpeedSetting} used by {@link TrainSpeedSettingCapabilityManager}.
 */
public class TrainSpeedSettingWrapper implements INBTSerializable<CompoundNBT> {
  private static final String SPEED_SETTING_KEY = "SpeedSetting";

  private TrainSpeedSetting speedSetting = TrainSpeedSetting.IDLE;

  /**
   * The {@link TrainSpeedSetting} wrapped by this object.
   */
  public TrainSpeedSetting getSpeedSetting() {
    return this.speedSetting;
  }

  /**
   * Set the {@link TrainSpeedSetting} wrapped by this object.
   *
   * @param speedSetting The new value. May not be null.
   */
  public void setSpeedSetting(TrainSpeedSetting speedSetting) {
    this.speedSetting = Objects.requireNonNull(speedSetting);
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt(SPEED_SETTING_KEY, this.speedSetting.ordinal());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.speedSetting = TrainSpeedSetting.values()[nbt.getInt(SPEED_SETTING_KEY) % TrainSpeedSetting.values().length];
  }
}
