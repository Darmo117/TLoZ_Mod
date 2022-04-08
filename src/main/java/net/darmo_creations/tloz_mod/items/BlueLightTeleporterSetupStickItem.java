package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.blocks.BlueLightTeleporter;
import net.darmo_creations.tloz_mod.tile_entities.BlueLightTeleporterTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * An item used to setup {@link BlueLightTeleporter}s’ destination.
 */
public class BlueLightTeleporterSetupStickItem extends TLoZItem {
  public BlueLightTeleporterSetupStickItem() {
    super(new Properties().maxStackSize(1).group(TLoZ.CREATIVE_MODE_TAB));
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    if (player == null) {
      return ActionResultType.FAIL;
    }
    ItemStack stack = context.getItem();
    BlockPos clickedPos = context.getPos();
    TileEntity te = context.getWorld().getTileEntity(clickedPos);
    if (te instanceof BlueLightTeleporterTileEntity) {
      stack.setTag(NBTUtil.writeBlockPos(clickedPos));
      player.sendStatusMessage(new TranslationTextComponent(
          "item.tloz.blue_teleporter_setup_stick.teleporter_selected", clickedPos
      ), true);
      return ActionResultType.SUCCESS;
    } else if (stack.getTag() != null) {
      BlockPos teleporterPos = NBTUtil.readBlockPos(stack.getTag());
      TileEntity t = context.getWorld().getTileEntity(teleporterPos);
      if (t instanceof BlueLightTeleporterTileEntity) {
        ((BlueLightTeleporterTileEntity) t).setTargetPos(clickedPos);
        player.sendStatusMessage(new TranslationTextComponent(
            "item.tloz.blue_teleporter_setup_stick.destination_selected", clickedPos
        ), true);
        return ActionResultType.SUCCESS;
      }
    }
    return ActionResultType.FAIL;
  }
}
