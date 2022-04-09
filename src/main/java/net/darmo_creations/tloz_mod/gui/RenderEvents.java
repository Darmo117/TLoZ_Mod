package net.darmo_creations.tloz_mod.gui;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiContainerEvent;
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
  public static void onGameOverlayRenderPre(final RenderGameOverlayEvent.Pre event) {
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
  public static void onGameOverlayRenderPost(final RenderGameOverlayEvent.Post event) {
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

  @SubscribeEvent
  public static void onContainerDrawForeground(GuiContainerEvent.DrawForeground event) {
    if (event.getGuiContainer() instanceof InventoryScreen) {
      TLoZ.INVENTORY_GUI.render(event.getMatrixStack(), (InventoryScreen) event.getGuiContainer(), event.getMouseX(), event.getMouseY());
    }
  }

  private RenderEvents() {
  }
}
