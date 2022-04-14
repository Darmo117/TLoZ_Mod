package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Mod.EventBusSubscriber(modid = TLoZ.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class EntityEvents {
  @SubscribeEvent
  public static void onEntityConstructing(EntityEvent.EntityConstructing event) {
    // Inject additional data parameters into players and minecarts
    Entity entity = event.getEntity();
    if (entity instanceof PlayerEntity) {
      register(entity, AdditionalDataParameters.PLAYER_TELEPORTER_DELAY, OptionalInt.empty());
      register(entity, AdditionalDataParameters.PLAYER_TELEPORTER_TARGET_POS, Optional.empty());
      register(entity, AdditionalDataParameters.PLAYER_TELEPORTER_YAW, Optional.empty());
      register(entity, AdditionalDataParameters.PLAYER_TELEPORTER_PITCH, Optional.empty());
    } else if (entity instanceof AbstractMinecartEntity) {
      register(entity, AdditionalDataParameters.TRAIN_COLLECTION, OptionalInt.empty());
      if (entity instanceof FurnaceMinecartEntity) {
        // FIXME crashes on world if entity is already in world
        register(entity, AdditionalDataParameters.TRAIN_SPEED_SETTING, TrainSpeedSetting.IDLE.ordinal());
      }
    }
  }

  private static <T> void register(Entity entity, DataParameter<T> dataParameter, T defaultValue) {
//    try {
    entity.getDataManager().register(dataParameter, defaultValue);
//    } catch (IllegalArgumentException e) {
//      // Parameter is already registered, skip
//    }
  }

  public static final double SIZE = 12;

  @SubscribeEvent
  public static void onEntityTeleport(EntityTeleportEvent event) {
    Entity entity = event.getEntity();
    if (entity instanceof AbstractMinecartEntity) {
      Vector3d prevPos = event.getPrev();
      Vector3d pos = event.getTarget();
      Vector3d diff = pos.subtract(prevPos);
      AxisAlignedBB boundingBox = new AxisAlignedBB(
          prevPos.x - SIZE / 2, prevPos.y - SIZE / 2, prevPos.z - SIZE / 2,
          prevPos.x + SIZE / 2, prevPos.y + SIZE / 2, prevPos.z + SIZE / 2
      );
      List<Entity> minecarts = entity.world.getEntitiesInAABBexcluding(entity, boundingBox, e -> e instanceof AbstractMinecartEntity);
      minecarts.forEach(m -> m.setPositionAndUpdate(m.getPosX() + diff.x, m.getPosY() + diff.y, m.getPosZ() + diff.z));
    }
  }

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    // Prevent food level from decreasing.
    event.player.getFoodStats().addStats(1, 1);
  }

  private EntityEvents() {
  }
}
