package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.TLoZ;
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
 * {@link TLoZ.ModEvents#onEntityConstructing(EntityEvent.EntityConstructing)}.
 */
public final class AdditionalDataParameters {
  public static final DataParameter<OptionalInt> PLAYER_TELEPORTER_DELAY = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.OPTIONAL_VARINT);
  public static final DataParameter<Optional<BlockPos>> PLAYER_TELEPORTER_TARGET_POS = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);

  private AdditionalDataParameters() {
  }
}
