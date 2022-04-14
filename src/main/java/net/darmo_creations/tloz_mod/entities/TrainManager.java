package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.ModKeyBindings;
import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.network.ModNetworkManager;
import net.darmo_creations.tloz_mod.network.SetTrainSpeedMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
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
      Entity ridingEntity = player.getRidingEntity();
      if (ridingEntity instanceof FurnaceMinecartEntity) {
        FurnaceMinecartEntity engine = (FurnaceMinecartEntity) ridingEntity;
        int key = event.getKey();
        TrainSpeedSetting currentSetting = getTrainSpeed(engine);
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
   * Return the speed setting of the given minecart.
   */
  public static TrainSpeedSetting getTrainSpeed(final FurnaceMinecartEntity engine) {
    int i = engine.getDataManager().get(AdditionalDataParameters.TRAIN_SPEED_SETTING);
    return TrainSpeedSetting.values()[i % TrainSpeedSetting.values().length];
  }

  /**
   * Set the speed of the given engine on the server.
   *
   * @param speedSetting The new speed setting.
   * @param engine       The minecart entity.
   */
  public static void setTrainSpeed(final TrainSpeedSetting speedSetting, FurnaceMinecartEntity engine) {
    ModNetworkManager.INSTANCE.sendToServer(new SetTrainSpeedMessage(speedSetting, engine.getEntityId()));
  }
}