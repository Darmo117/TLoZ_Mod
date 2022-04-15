package net.darmo_creations.tloz_mod.entities.trains;

import net.darmo_creations.tloz_mod.ModKeyBindings;
import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.network.ModNetworkManager;
import net.darmo_creations.tloz_mod.network.TrainSpeedMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = TLoZ.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TrainManager {
  @SubscribeEvent
  public static void onKeyInput(InputEvent.KeyInputEvent event) {
    // Only react to key presses
    if (event.getAction() != GLFW.GLFW_PRESS) {
      return;
    }
    PlayerEntity player = Minecraft.getInstance().player;
    if (player != null) {
      Entity riddenEntity = player.getRidingEntity();
      if (riddenEntity instanceof TrainEngineEntity) {
        TrainEngineEntity engine = (TrainEngineEntity) riddenEntity;
        int key = event.getKey();
        TrainSpeedSetting currentSetting = engine.getSpeedSetting();
        TrainSpeedSetting newSpeed = currentSetting;
        if (key == ModKeyBindings.TRAIN_INCREASE_SPEED.getKey().getKeyCode()) {
          newSpeed = currentSetting.getNext();
        } else if (key == ModKeyBindings.TRAIN_DECREASE_SPEED.getKey().getKeyCode()) {
          newSpeed = currentSetting.getPrevious();
        } else if (key == ModKeyBindings.TRAIN_WHISTLE.getKey().getKeyCode()) {
          whistle(player);
        }
        if (newSpeed != currentSetting) {
          setTrainSpeed(newSpeed, engine);
        }
      }
    }
  }

  public static void whistle(PlayerEntity player) {
    // TODO play sound on server
  }

  /**
   * Set the speed of the given engine locally the sends an update packet to the server.
   *
   * @param speedSetting The new speed setting.
   * @param engine       The minecart entity.
   */
  public static void setTrainSpeed(final TrainSpeedSetting speedSetting, TrainEngineEntity engine) {
    ModNetworkManager.INSTANCE.sendToServer(new TrainSpeedMessage(speedSetting, engine.getEntityId()));
  }
}
