package net.darmo_creations.tloz_mod.network;

import net.darmo_creations.tloz_mod.entities.TrainCollection;
import net.darmo_creations.tloz_mod.entities.capabilities.TrainCollectionCapabilityManager;
import net.darmo_creations.tloz_mod.entities.capabilities.TrainCollectionWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Packet used to synchronize {@link TrainCollectionWrapper} from server to client.
 */
public class TrainCollectionMessage implements IPacket<TrainCollectionMessage.Handler> {
  private TrainCollection collection;
  private int minecartID;

  /**
   * Create a message for the given data.
   *
   * @param collection Data to send to the client.
   * @param minecartID ID of the minecart entity to update.
   */
  public TrainCollectionMessage(final TrainCollection collection, int minecartID) {
    this.collection = collection;
    this.minecartID = minecartID;
  }

  /**
   * Create a packet from a byte buffer.
   *
   * @param buf The buffer.
   */
  public TrainCollectionMessage(final PacketBuffer buf) {
    this.readPacketData(buf);
  }

  @Override
  public void readPacketData(PacketBuffer buf) {
    this.collection = TrainCollection.values()[buf.readInt() % TrainCollection.values().length];
    this.minecartID = buf.readInt();
  }

  @Override
  public void writePacketData(PacketBuffer buf) {
    buf.writeInt(this.collection.ordinal());
    buf.writeInt(this.minecartID);
  }

  @Override
  public void processPacket(Handler handler) {
  }

  /**
   * Client-side handler for the {@link TrainCollectionMessage} class.
   */
  public static class Handler implements INetHandler {
    /**
     * Handles the packet received from the server.
     */
    public static void handleClient(TrainCollectionMessage msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
              //noinspection ConstantConditions
              Entity entity = Minecraft.getInstance().world.getEntityByID(msg.minecartID);
              if (entity instanceof AbstractMinecartEntity) {
                entity.getCapability(TrainCollectionCapabilityManager.INSTANCE)
                    .ifPresent(trainCollectionWrapper -> trainCollectionWrapper.setCollection(msg.collection));
              }
            }
        ));
      }
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
