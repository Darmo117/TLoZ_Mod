package net.darmo_creations.tloz_mod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.Utils;
import net.darmo_creations.tloz_mod.entities.TrainManager;
import net.darmo_creations.tloz_mod.entities.TrainSpeedSetting;
import net.darmo_creations.tloz_mod.items.BombBagItem;
import net.darmo_creations.tloz_mod.items.ModItems;
import net.darmo_creations.tloz_mod.items.QuiverBowItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A custom HUD that displays the amount of rupees, bombs and arrows of the player.
 * Also removes the food and XP bars.
 */
@OnlyIn(Dist.CLIENT)
public class HUD extends AbstractGui {
  public static final ResourceLocation TEXTURE = new ResourceLocation(TLoZ.MODID, "textures/gui/hud.png");

  private static final int HEALTH_BAR_OFFSET = 6;

  private final Minecraft minecraft;

  public HUD(Minecraft minecraft) {
    this.minecraft = minecraft;
  }

  public void render(MatrixStack matrixStack) {
    PlayerEntity player = Minecraft.getInstance().player;
    if (player == null) {
      return;
    }
    Entity riding = player.getRidingEntity();
    if (riding instanceof FurnaceMinecartEntity) {
      this.renderTrainHUD(matrixStack, (FurnaceMinecartEntity) riding);
    } else if (!player.isCreative() && !player.isSpectator()) {
      this.renderDefaultHUD(matrixStack, player);
    }

    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
  }

  private void renderTrainHUD(MatrixStack matrixStack, final FurnaceMinecartEntity engine) {
    TrainSpeedSetting speedSetting = TrainManager.getTrainSpeed(engine);
    // TODO draw custom texture instead of text
    drawString(matrixStack, this.minecraft.fontRenderer, speedSetting.name(), 10, 10, 0xffffff); // TEMP
  }

  private void renderDefaultHUD(MatrixStack matrixStack, PlayerEntity player) {
    ItemStack selectedStack = player.getHeldItemMainhand();
    Item selectedItem = selectedStack.getItem();

    int rupeeBagIndex = Utils.getRupeeBagInventorySlot(player);
    if (rupeeBagIndex != -1) {
      ItemStack rupeeBag = player.inventory.mainInventory.get(rupeeBagIndex);
      this.displayItemAmount(matrixStack, ModItems.BIG_GREEN_RUPEE, this.getAmount(rupeeBag), 0, 2, 2, false);
    }

    if (selectedItem instanceof BombBagItem) {
      this.displayItemAmount(matrixStack, ModItems.BOMB_AMMO, this.getAmount(selectedStack), selectedStack.getMaxDamage(), 2, 28, true);
    } else if (selectedItem instanceof QuiverBowItem) {
      int quiverIndex = Utils.getQuiverInventorySlot(player);
      if (quiverIndex != -1) {
        ItemStack quiver = player.inventory.mainInventory.get(quiverIndex);
        this.displayItemAmount(matrixStack, Items.ARROW, this.getAmount(quiver), quiver.getMaxDamage(), 2, 28, true);
      }
    }
  }

  private void displayItemAmount(MatrixStack matrixStack, Item icon, int amount, int maxAmount, int x, int y, boolean renderBackground) {
    if (renderBackground) {
      this.minecraft.getTextureManager().bindTexture(TEXTURE);
      blit(matrixStack, x, y, 0, 0, 24, 24, 64, 64);
    }
    x += 4;
    y += 4;
    this.minecraft.getItemRenderer().renderItemIntoGUI(new ItemStack(icon), x, y);
    int yOffset = 1 + (16 - this.minecraft.fontRenderer.FONT_HEIGHT) / 2;
    String text = maxAmount > 0 ? String.format("%d/%d", amount, maxAmount) : ("" + amount);
    drawString(matrixStack, this.minecraft.fontRenderer, text, x + 21, y + yOffset, 0xffffff);
  }

  private int getAmount(final ItemStack stack) {
    return stack.getMaxDamage() - stack.getDamage();
  }

  @SuppressWarnings("deprecation")
  public static void offsetHealthBarPre() {
    RenderSystem.pushMatrix();
    RenderSystem.translatef(0, HEALTH_BAR_OFFSET, 0);
  }

  @SuppressWarnings("deprecation")
  public static void offsetHealthBarPost() {
    RenderSystem.popMatrix();
  }
}
