package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.entities.capabilities.TeleportData;
import net.darmo_creations.tloz_mod.entities.capabilities.TeleportDataCapabilityManager;
import net.darmo_creations.tloz_mod.network.ModNetworkManager;
import net.darmo_creations.tloz_mod.network.TeleportDataMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.EntityTeleportEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

@Mod.EventBusSubscriber(modid = TLoZ.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class EntityEvents {
  /**
   * Clone custom player capabilities upon death.
   */
  @SubscribeEvent
  public static void onPlayerClone(PlayerEvent.Clone event) {
    if (event.isWasDeath() && !event.getEntity().world.isRemote) {
      PlayerEntity originalPlayer = event.getOriginal();
      PlayerEntity newPlayer = (PlayerEntity) event.getEntity();
      TeleportData teleportData = originalPlayer.getCapability(TeleportDataCapabilityManager.INSTANCE).orElseGet(TeleportData::new);
      newPlayer.getCapability(TeleportDataCapabilityManager.INSTANCE).ifPresent(cap -> cap.deserializeNBT(teleportData.serializeNBT()));
      ModNetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) newPlayer), new TeleportDataMessage(teleportData));
    }
  }

//  @SubscribeEvent
//  public static void onEntityConstructing(EntityEvent.EntityConstructing event) {
//    // Inject additional data parameters into players and minecarts
//    Entity entity = event.getEntity();
//    if (entity instanceof AbstractMinecartEntity) {
//      register(entity, AdditionalDataParameters.TRAIN_COLLECTION, OptionalInt.empty());
//      if (entity instanceof FurnaceMinecartEntity) {
//        register(entity, AdditionalDataParameters.TRAIN_SPEED_SETTING, TrainSpeedSetting.IDLE.ordinal());
//      }
//    }
//  }
//
//  private static <T> void register(Entity entity, DataParameter<T> dataParameter, T defaultValue) {
//    try {
//    entity.getDataManager().register(dataParameter, defaultValue);
//    } catch (IllegalArgumentException e) {
//      // Parameter is already registered, skip
//    }
//  }

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
