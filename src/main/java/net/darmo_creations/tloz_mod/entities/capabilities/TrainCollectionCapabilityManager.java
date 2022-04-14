package net.darmo_creations.tloz_mod.entities.capabilities;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.entities.TrainCollection;
import net.darmo_creations.tloz_mod.items.TrainPartItem;
import net.darmo_creations.tloz_mod.mixins.FurnaceMinecartEntityMixin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Manager for the {@link TrainCollectionWrapper} capability.
 *
 * @see TrainCollectionWrapper
 * @see TrainCollection
 * @see TrainPartItem
 * @see FurnaceMinecartEntityMixin
 */
@Mod.EventBusSubscriber(modid = TLoZ.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class TrainCollectionCapabilityManager {
  /**
   * Name of the {@link TrainCollectionWrapper} capability.
   */
  public static final ResourceLocation CAPABILITY_NAME = new ResourceLocation(TLoZ.MODID, "train_collection");
  /**
   * Instance of the {@link TrainCollectionWrapper} capability. Guaranted to never be null at runtime.
   */
  @CapabilityInject(TrainCollectionWrapper.class)
  public static Capability<TrainCollectionWrapper> INSTANCE = null;

  public static void registerCapabilities() {
    CapabilityManager.INSTANCE.register(
        TrainCollectionWrapper.class,
        SimpleCapabilityStorage.create(() -> INSTANCE, Constants.NBT.TAG_COMPOUND),
        TrainCollectionWrapper::new
    );
  }

  /**
   * Attach this capability to all minecarts.
   */
  @SubscribeEvent
  public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
    if (event.getObject() instanceof AbstractMinecartEntity) {
      event.addCapability(
          CAPABILITY_NAME,
          SimplePersistentCapabilityProvider.from(INSTANCE, TrainCollectionWrapper::new)
      );
    }
  }

  private TrainCollectionCapabilityManager() {
  }
}
