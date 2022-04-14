package net.darmo_creations.tloz_mod.network;

import net.darmo_creations.tloz_mod.entities.AdditionalDataParameters;
import net.darmo_creations.tloz_mod.entities.TrainSpeedSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class SetTrainSpeedMessage implements IPacket<SetTrainSpeedMessage.Handler> {
  private TrainSpeedSetting speedSetting;
  private int minecartID;

  public SetTrainSpeedMessage(final TrainSpeedSetting speedSetting, final int minecartID) {
    this.speedSetting = Objects.requireNonNull(speedSetting);
    this.minecartID = minecartID;
  }

  /**
   * Create a packet from a byte buffer.
   *
   * @param buf The buffer.
   */
  public SetTrainSpeedMessage(final PacketBuffer buf) {
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

  public static class Handler implements INetHandler {
    public static void handle(SetTrainSpeedMessage msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> {
        ServerPlayerEntity sender = context.getSender();
        if (sender != null) {
          Entity entity = sender.world.getEntityByID(msg.minecartID);
          if (entity instanceof FurnaceMinecartEntity) {
            FurnaceMinecartEntity engine = (FurnaceMinecartEntity) entity;
            // yaw = 0 mod 180 -> x
            // yaw = 0 mod 90 -> z
            int direction = msg.speedSetting.getDirection();
            System.out.println(direction); // DEBUG
            engine.getDataManager().set(AdditionalDataParameters.TRAIN_SPEED_SETTING, msg.speedSetting.ordinal());
            // TODO check signs
            engine.pushX = Math.cos(engine.rotationYaw) * direction;
            engine.pushZ = Math.cos(engine.rotationYaw) * direction;
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
