package net.darmo_creations.tloz_mod.entities;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

import java.util.OptionalInt;

/**
 * @deprecated Replace by Forge capabilities.
 */
@Deprecated
public final class AdditionalDataParameters {
  // Minecart data
  public static final DataParameter<OptionalInt> TRAIN_COLLECTION = EntityDataManager.createKey(AbstractMinecartEntity.class, DataSerializers.OPTIONAL_VARINT);
  public static final DataParameter<Integer> TRAIN_SPEED_SETTING = EntityDataManager.createKey(FurnaceMinecartEntity.class, DataSerializers.VARINT);

  private AdditionalDataParameters() {
  }
}
