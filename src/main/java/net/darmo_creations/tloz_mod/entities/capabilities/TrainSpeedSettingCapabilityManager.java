package net.darmo_creations.tloz_mod.entities.capabilities;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.entities.TrainSpeedSetting;
import net.darmo_creations.tloz_mod.mixins.FurnaceMinecartEntityMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Manager for the {@link TrainSpeedSettingWrapper} capability.
 *
 * @see TrainSpeedSettingWrapper
 * @see TrainSpeedSetting
 * @see FurnaceMinecartEntityMixin
 */
@Mod.EventBusSubscriber(modid = TLoZ.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class TrainSpeedSettingCapabilityManager {
  /**
   * Name of the {@link TrainSpeedSettingWrapper} capability.
   */
  public static final ResourceLocation CAPABILITY_NAME = new ResourceLocation(TLoZ.MODID, "train_speed_setting");
  /**
   * Instance of the {@link TrainSpeedSettingWrapper} capability. Guaranted to never be null at runtime.
   */
  @CapabilityInject(TrainSpeedSettingWrapper.class)
  public static Capability<TrainSpeedSettingWrapper> INSTANCE = null;

  public static void registerCapabilities() {
    CapabilityManager.INSTANCE.register(
        TrainSpeedSettingWrapper.class,
        SimpleCapabilityStorage.create(() -> INSTANCE, Constants.NBT.TAG_COMPOUND),
        TrainSpeedSettingWrapper::new
    );
  }

  /**
   * Attach this capability to all furnace minecarts.
   */
  @SubscribeEvent
  public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof FurnaceMinecartEntity) {
      event.addCapability(
          CAPABILITY_NAME,
          SimplePersistentCapabilityProvider.from(INSTANCE, TrainSpeedSettingWrapper::new)
      );
    }
  }

  private TrainSpeedSettingCapabilityManager() {
  }
}
