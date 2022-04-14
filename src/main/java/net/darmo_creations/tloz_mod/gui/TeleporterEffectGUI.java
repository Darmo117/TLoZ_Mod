package net.darmo_creations.tloz_mod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.blocks.BlueLightTeleporter;
import net.darmo_creations.tloz_mod.entities.capabilities.TeleportData;
import net.darmo_creations.tloz_mod.entities.capabilities.TeleportDataCapabilityManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

/**
 * Renders a blue fade-in overlay whenever a player is using a {@link BlueLightTeleporter}.
 */
@OnlyIn(Dist.CLIENT)
public class TeleporterEffectGUI extends AbstractGui {
  private final Minecraft minecraft;

  public TeleporterEffectGUI(Minecraft minecraft) {
    this.minecraft = minecraft;
  }

  public void render(MatrixStack matrixStack) {
    PlayerEntity player = Minecraft.getInstance().player;
    if (player == null) {
      return;
    }
    Optional<Integer> delay = player.getCapability(TeleportDataCapabilityManager.INSTANCE).orElseGet(TeleportData::new).getDelay();
    if (delay.isPresent()) {
      int progression = (int) (255 * (1 - (float) delay.get() / BlueLightTeleporter.DELAY));
      int color = (progression << 24) | 0x0080ff;
      fill(matrixStack, 0, 0, this.minecraft.getMainWindow().getScaledWidth(), this.minecraft.getMainWindow().getScaledHeight(), color);
    }
  }
}
