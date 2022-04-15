package net.darmo_creations.tloz_mod.network;

import net.darmo_creations.tloz_mod.entities.TrainSpeedSetting;
import net.darmo_creations.tloz_mod.entities.capabilities.TrainSpeedSettingCapabilityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Packet used to synchronize the speed settings of a minecart between client and server.
 */
public class TrainSpeedMessage implements IPacket<TrainSpeedMessage.Handler> {
  private TrainSpeedSetting speedSetting;
  private int minecartID;

  /**
   * Create a message for the given data.
   *
   * @param speedSetting The new speed setting.
   * @param minecartID   ID of the minecart entity to update.
   */
  public TrainSpeedMessage(final TrainSpeedSetting speedSetting, final int minecartID) {
    this.speedSetting = Objects.requireNonNull(speedSetting);
    this.minecartID = minecartID;
  }

  /**
   * Create a packet from a byte buffer.
   *
   * @param buf The buffer.
   */
  public TrainSpeedMessage(final PacketBuffer buf) {
    this.readPacketData(buf);
  }

  @Override
  public void readPacketData(PacketBuffer buf) {
    this.speedSetting = TrainSpeedSetting.values()[buf.readShort() % TrainSpeedSetting.values().length];
    this.minecartID = buf.readInt();
  }

  @Override
  public void writePacketData(PacketBuffer buf) {
    buf.writeShort(this.speedSetting.ordinal());
    buf.writeInt(this.minecartID);
  }

  @Override
  public void processPacket(Handler handler) {
  }

  /**
   * Server-side handler for the {@link TrainSpeedMessage} class.
   */
  public static class Handler implements INetHandler {
    /**
     * Handles the packet received from the client.
     */
    public static void handle(TrainSpeedMessage msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
        if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER) {
          ServerPlayerEntity sender = context.getSender();
          if (sender != null) {
            Entity entity = sender.world.getEntityByID(msg.minecartID);
            if (entity instanceof FurnaceMinecartEntity) {
              FurnaceMinecartEntity engine = (FurnaceMinecartEntity) entity;
              // yaw = 0 mod 180 -> x
              // yaw = 0 mod 90 -> z
              int direction = msg.speedSetting.getDirection();
              System.out.println(direction); // DEBUG
              engine.getCapability(TrainSpeedSettingCapabilityManager.INSTANCE)
                  .ifPresent(trainSpeedSettingWrapper -> trainSpeedSettingWrapper.setSpeedSetting(msg.speedSetting));
              // TODO check signs
              engine.pushX = Math.cos(engine.rotationYaw) * direction;
              engine.pushZ = Math.cos(engine.rotationYaw) * direction;
            }
          }
        } else if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
          //noinspection ConstantConditions
          Entity entity = Minecraft.getInstance().world.getEntityByID(msg.minecartID);
          if (entity instanceof FurnaceMinecartEntity) {
            entity.getCapability(TrainSpeedSettingCapabilityManager.INSTANCE)
                .ifPresent(trainSpeedSettingWrapper -> trainSpeedSettingWrapper.setSpeedSetting(msg.speedSetting));
          }
        }
      });
      context.setPacketHandled(true);
    }

    @Override
    public void onDisconnect(ITextComponent reason) {
    }

    @Override
    public NetworkManager getNetworkManager() {
      return null;
    }
  }
}
