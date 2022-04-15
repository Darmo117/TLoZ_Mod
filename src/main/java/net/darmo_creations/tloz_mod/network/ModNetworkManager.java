package net.darmo_creations.tloz_mod.network;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModNetworkManager {
  private static final String PROTOCOL_VERSION = "1";

  private static int id = 0;

  public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
      new ResourceLocation(TLoZ.MODID, "main"),
      () -> PROTOCOL_VERSION,
      PROTOCOL_VERSION::equals,
      PROTOCOL_VERSION::equals
  );

  /**
   * Register a new message type to this mod’s network channel.
   *
   * @param messageType     Message’s class.
   * @param encoder         Reference to the encoding method.
   * @param decoder         Reference to the decoding method.
   * @param messageConsumer Reference to the message handling method.
   */
  public static <MSG> void registerMessage(
      Class<MSG> messageType,
      BiConsumer<MSG, PacketBuffer> encoder,
      Function<PacketBuffer, MSG> decoder,
      BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer
  ) {
    INSTANCE.registerMessage(
        id++,
        messageType,
        encoder,
        decoder,
        messageConsumer
    );
  }
}
