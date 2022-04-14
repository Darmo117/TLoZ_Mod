package net.darmo_creations.tloz_mod.entities;

import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

/**
 * @deprecated Replace by Forge capabilities.
 */
@Deprecated
public final class AdditionalDataParameters {
  // TODO migrate to capabilities
  // Minecart data
  public static final DataParameter<Integer> TRAIN_SPEED_SETTING = EntityDataManager.createKey(FurnaceMinecartEntity.class, DataSerializers.VARINT);

  private AdditionalDataParameters() {
  }
}
