package net.darmo_creations.tloz_mod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.Utils;
import net.darmo_creations.tloz_mod.items.ModItems;
import net.darmo_creations.tloz_mod.items.TreasureItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Survival inventory overlay that displays the number of each treasure the player has in their treasure bag.
 */
@OnlyIn(Dist.CLIENT)
public class InventoryGUI extends AbstractGui {
  private static final int COLUMN_HEIGHT = 5;
  private static final int ICON_SIZE = 24;

  private final Minecraft minecraft;

  public InventoryGUI(Minecraft minecraft) {
    this.minecraft = minecraft;
  }

  public void render(MatrixStack matrixStack, final InventoryScreen inventoryScreen, final int mouseX, final int mouseY) {
    PlayerEntity player = Minecraft.getInstance().player;
    if (player == null || player.isCreative() || player.isSpectator() || inventoryScreen.getRecipeGui().isVisible()) {
      return;
    }
    int treasureBagIndex = Utils.getTreasureBagInventorySlot(player);
    if (treasureBagIndex == -1) {
      return;
    }

    TreasureItem hoveredIcon = null;
    ItemStack treasureBag = player.inventory.mainInventory.get(treasureBagIndex);
    Map<TreasureItem, Integer> treasures = ModItems.TREASURE_BAG.getTreasures(treasureBag);
    List<Map.Entry<TreasureItem, Integer>> entries = treasures.entrySet().stream()
        .sorted((a, b) -> TreasureItem.getComparator().compare(a.getKey(), b.getKey()))
        .collect(Collectors.toList());
    for (int i = 0; i < entries.size(); i++) {
      Map.Entry<TreasureItem, Integer> e = entries.get(i);
      int x = -120 + 40 * (i / COLUMN_HEIGHT);
      int y = 26 * (i % COLUMN_HEIGHT);
      this.displayAmount(matrixStack, e.getKey(), e.getValue(), x, y);
      if (this.isPointInRegion(inventoryScreen, x, y, ICON_SIZE, ICON_SIZE, mouseX, mouseY)) {
        hoveredIcon = e.getKey();
      }
    }

    if (hoveredIcon != null) {
      this.renderHoveredTooltip(matrixStack, inventoryScreen, hoveredIcon,
          mouseX - inventoryScreen.getGuiLeft(), mouseY - inventoryScreen.getGuiTop());
    }
  }

  private boolean isPointInRegion(ContainerScreen<?> screen, int x, int y, int width, int height, double mouseX, double mouseY) {
    mouseX = mouseX - screen.getGuiLeft();
    mouseY = mouseY - screen.getGuiTop();
    return mouseX >= x - 1 && mouseX < x + width + 1 && mouseY >= y - 1 && mouseY < y + height + 1;
  }

  private void displayAmount(MatrixStack matrixStack, Item icon, int amount, int x, int y) {
    this.minecraft.getTextureManager().bindTexture(HUD.TEXTURE);
    blit(matrixStack, x, y, 0, 0, ICON_SIZE, ICON_SIZE, 64, 64);
    x += 4;
    y += 4;
    this.minecraft.getItemRenderer().renderItemIntoGUI(new ItemStack(icon), x, y);
    int yOffset = 1 + (16 - this.minecraft.fontRenderer.FONT_HEIGHT) / 2;
    drawString(matrixStack, this.minecraft.fontRenderer, "" + amount, x + 21, y + yOffset, 0xffffff);
  }

  private void renderHoveredTooltip(MatrixStack matrixStack, ContainerScreen<?> screen, Item icon, int x, int y) {
    //noinspection ConstantConditions
    if (this.minecraft.player.inventory.getItemStack().isEmpty()) {
      this.renderTooltip(matrixStack, screen, new ItemStack(icon), x, y);
    }
  }

  private void renderTooltip(MatrixStack matrixStack, ContainerScreen<?> screen, ItemStack itemStack, int mouseX, int mouseY) {
    FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
    GuiUtils.preItemToolTip(itemStack);
    this.renderWrappedToolTip(matrixStack, screen, this.getTooltipFromItem(itemStack), mouseX, mouseY, font == null ? this.minecraft.fontRenderer : font);
    GuiUtils.postItemToolTip();
  }

  private void renderWrappedToolTip(MatrixStack matrixStack, ContainerScreen<?> screen, List<? extends ITextProperties> tooltips,
                                    int mouseX, int mouseY, FontRenderer font) {
    int adjustedWidth = this.minecraft.getMainWindow().getScaledWidth() - screen.getGuiLeft();
    int adjustedHeight = this.minecraft.getMainWindow().getScaledHeight() - screen.getGuiTop();
    GuiUtils.drawHoveringText(matrixStack, tooltips, mouseX, mouseY, adjustedWidth, adjustedHeight, -1, font);
  }

  private List<ITextComponent> getTooltipFromItem(ItemStack itemStack) {
    ITooltipFlag.TooltipFlags tooltipFlag = this.minecraft.gameSettings.advancedItemTooltips
        ? ITooltipFlag.TooltipFlags.ADVANCED
        : ITooltipFlag.TooltipFlags.NORMAL;
    return itemStack.getTooltip(this.minecraft.player, tooltipFlag);
  }
}
