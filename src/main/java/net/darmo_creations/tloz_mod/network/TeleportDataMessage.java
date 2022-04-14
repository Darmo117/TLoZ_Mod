package net.darmo_creations.tloz_mod.network;

import net.darmo_creations.tloz_mod.entities.capabilities.TeleportData;
import net.darmo_creations.tloz_mod.entities.capabilities.TeleportDataCapabilityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Packet used to synchronize {@link TeleportData} from server to client.
 */
public class TeleportDataMessage implements IPacket<TeleportDataMessage.Handler> {
  private TeleportData data;

  /**
   * Create a message for the given data.
   *
   * @param data Data to send to the client.
   */
  public TeleportDataMessage(final TeleportData data) {
    this.data = data;
  }

  /**
   * Create a packet from a byte buffer.
   *
   * @param buf The buffer.
   */
  public TeleportDataMessage(final PacketBuffer buf) {
    this.readPacketData(buf);
  }

  @Override
  public void readPacketData(PacketBuffer buf) {
    this.data = new TeleportData();
    //noinspection ConstantConditions
    this.data.deserializeNBT(buf.readCompoundTag());
  }

  @Override
  public void writePacketData(PacketBuffer buf) {
    buf.writeCompoundTag(this.data.serializeNBT());
  }

  @Override
  public void processPacket(Handler handler) {
  }

  /**
   * Client-side handler for the {@link TeleportDataMessage} class.
   */
  public static class Handler implements INetHandler {
    /**
     * Handles the packet received from the server.
     */
    public static void handle(TeleportDataMessage msg, Supplier<NetworkEvent.Context> ctx) {
      NetworkEvent.Context context = ctx.get();
      //noinspection ConstantConditions
      context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
          Minecraft.getInstance().player.getCapability(TeleportDataCapabilityManager.INSTANCE)
              .ifPresent(teleportData -> teleportData.deserializeNBT(msg.data.serializeNBT()))
      ));
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
