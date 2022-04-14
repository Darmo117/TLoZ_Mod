package net.darmo_creations.tloz_mod;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

/**
 * List of all key bindings added by this mod.
 */
public class ModKeyBindings {
  public static final KeyBinding TRAIN_INCREASE_SPEED = new KeyBinding("key.train.increase_speed", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_8, "key.categories.train");
  public static final KeyBinding TRAIN_DECREASE_SPEED = new KeyBinding("key.train.decrease_speed", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_2, "key.categories.train");
  public static final KeyBinding TRAIN_WHISTLE = new KeyBinding("key.train.whistle", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_7, "key.categories.train");
}
