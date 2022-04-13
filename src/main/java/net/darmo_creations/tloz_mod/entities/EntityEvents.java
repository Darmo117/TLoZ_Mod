package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.OptionalInt;

@Mod.EventBusSubscriber(modid = TLoZ.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class EntityEvents {
  @SubscribeEvent
  public static void onEntityConstructing(EntityEvent.EntityConstructing event) {
    // Inject additional data parameters into players
    Entity entity = event.getEntity();
    if (entity instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entity;
      player.getDataManager().register(AdditionalDataParameters.PLAYER_TELEPORTER_DELAY, OptionalInt.empty());
      player.getDataManager().register(AdditionalDataParameters.PLAYER_TELEPORTER_TARGET_POS, Optional.empty());
      player.getDataManager().register(AdditionalDataParameters.PLAYER_TELEPORTER_YAW, Optional.empty());
      player.getDataManager().register(AdditionalDataParameters.PLAYER_TELEPORTER_PITCH, Optional.empty());
    }
  }

  @SubscribeEvent
  public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    // Prevent food level to decrease.
    event.player.getFoodStats().addStats(1, 1);
  }

  private EntityEvents() {
  }
}
