package net.darmo_creations.tloz_mod.entities.capabilities;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.blocks.BlueLightTeleporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Manager for the {@link TeleportData} capability.
 *
 * @see TeleportData
 * @see BlueLightTeleporter
 */
@Mod.EventBusSubscriber(modid = TLoZ.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class TeleportDataCapabilityManager {
  /**
   * Name of the {@link TeleportData} capability.
   */
  public static final ResourceLocation CAPABILITY_NAME = new ResourceLocation(TLoZ.MODID, "teleport_data");
  /**
   * Instance of the {@link TeleportData} capability. Guaranted to never be null at runtime.
   */
  @CapabilityInject(TeleportData.class)
  public static Capability<TeleportData> INSTANCE = null;

  public static void registerCapabilities() {
    CapabilityManager.INSTANCE.register(
        TeleportData.class,
        SimpleCapabilityStorage.create(() -> INSTANCE, Constants.NBT.TAG_COMPOUND),
        TeleportData::new
    );
  }

  /**
   * Attach this capability to all players.
   */
  @SubscribeEvent
  public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof PlayerEntity) {
      event.addCapability(
          CAPABILITY_NAME,
          SimplePersistentCapabilityProvider.from(INSTANCE, TeleportData::new)
      );
    }
  }

  private TeleportDataCapabilityManager() {
  }
}
