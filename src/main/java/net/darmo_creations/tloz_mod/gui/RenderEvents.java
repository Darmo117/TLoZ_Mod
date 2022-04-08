package net.darmo_creations.tloz_mod.gui;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles all render-related events for this mod.
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = TLoZ.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class RenderEvents {
  /**
   * Performs the following operations:
   * <li>Removes the food and XP bars.
   * <li>Offsets the health bar to bring it closer to the hotbar.
   */
  @SubscribeEvent
  public static void onRenderPre(final RenderGameOverlayEvent.Pre event) {
    switch (event.getType()) {
      case FOOD:
      case EXPERIENCE:
        event.setCanceled(true);
        break;
      case HEALTH:
        HUD.offsetHealthBarPre();
        break;
    }
  }

  /**
   * Performs the following operations:
   * <li>Renders the custom HUD.
   * <li>Renders the teleportation overlay.
   * <li>Performs the cleanup after offsetting the health-bar.
   */
  @SubscribeEvent
  public static void onRenderPost(final RenderGameOverlayEvent.Post event) {
    switch (event.getType()) {
      case ALL:
        TLoZ.MAIN_HUD.render(event.getMatrixStack());
        TLoZ.TELEPORTER_EFFECT_GUI.render(event.getMatrixStack());
        break;
      case HEALTH:
        HUD.offsetHealthBarPost();
        break;
    }
  }

  private RenderEvents() {
  }
}
