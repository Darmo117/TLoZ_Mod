package net.darmo_creations.tloz_mod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.darmo_creations.tloz_mod.Utils;
import net.darmo_creations.tloz_mod.items.ModItems;
import net.darmo_creations.tloz_mod.items.TreasureItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Survival inventory overlay that displays the number of each treasure the player has in their treasure bag.
 */
@OnlyIn(Dist.CLIENT)
public class InventoryGUI extends AbstractGui {
  private final Minecraft minecraft;

  public InventoryGUI(Minecraft minecraft) {
    this.minecraft = minecraft;
  }

  public void render(MatrixStack matrixStack, int mouseX, int mouseY) {
    PlayerEntity player = Minecraft.getInstance().player;
    if (player == null || player.isCreative() || player.isSpectator()) {
      return;
    }
    int treasureBagIndex = Utils.getTreasureBagInventorySlot(player);
    if (treasureBagIndex == -1) {
      return;
    }

    ItemStack treasureBag = player.inventory.mainInventory.get(treasureBagIndex);
    Map<TreasureItem, Integer> treasures = ModItems.TREASURE_BAG.getTreasures(treasureBag);
    //noinspection ConstantConditions
    List<Map.Entry<TreasureItem, Integer>> entries = treasures.entrySet().stream()
        .sorted(Comparator.comparing(e -> e.getKey().getRegistryName().getPath()))
        .collect(Collectors.toList());
    for (int i = 0; i < entries.size(); i++) {
      Map.Entry<TreasureItem, Integer> e = entries.get(i);
      // FIXME icons are drawn inside inventory, not besides it
      // TODO draw on multiple columns
      this.displayAmount(matrixStack, e.getKey(), e.getValue(), 2, 2 + i * 24);
    }
    // TODO render tooltips when hovering icons
  }

  private void displayAmount(MatrixStack matrixStack, Item icon, int amount, int x, int y) {
    this.minecraft.getTextureManager().bindTexture(HUD.TEXTURE);
    blit(matrixStack, x, y, 0, 0, 24, 24, 64, 64);
    x += 4;
    y += 4;
    this.minecraft.getItemRenderer().renderItemIntoGUI(new ItemStack(icon), x, y);
    int yOffset = 1 + (16 - this.minecraft.fontRenderer.FONT_HEIGHT) / 2;
    drawString(matrixStack, this.minecraft.fontRenderer, "" + amount, x + 21, y + yOffset, 0xffffff);
  }
}
