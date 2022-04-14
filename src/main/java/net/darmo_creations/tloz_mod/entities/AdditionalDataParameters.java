package net.darmo_creations.tloz_mod.entities;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityEvent;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Defines additional data parameters for various vanilla entities that are injected in
 * {@link EntityEvents#onEntityConstructing(EntityEvent.EntityConstructing)}.
 */
public final class AdditionalDataParameters {
  // Player data
  public static final DataParameter<OptionalInt> PLAYER_TELEPORTER_DELAY = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.OPTIONAL_VARINT);
  public static final DataParameter<Optional<BlockPos>> PLAYER_TELEPORTER_TARGET_POS = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
  public static final DataParameter<Optional<Float>> PLAYER_TELEPORTER_YAW = EntityDataManager.createKey(PlayerEntity.class, ModDataSerializers.OPTIONAL_FLOAT);
  public static final DataParameter<Optional<Float>> PLAYER_TELEPORTER_PITCH = EntityDataManager.createKey(PlayerEntity.class, ModDataSerializers.OPTIONAL_FLOAT);

  // Minecart data
  public static final DataParameter<OptionalInt> TRAIN_COLLECTION = EntityDataManager.createKey(AbstractMinecartEntity.class, DataSerializers.OPTIONAL_VARINT);
  public static final DataParameter<Integer> TRAIN_SPEED_SETTING = EntityDataManager.createKey(FurnaceMinecartEntity.class, DataSerializers.VARINT);

  private AdditionalDataParameters() {
  }
}
