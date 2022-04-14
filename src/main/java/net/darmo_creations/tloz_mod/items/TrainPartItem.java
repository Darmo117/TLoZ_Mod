package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.entities.TrainCollection;
import net.darmo_creations.tloz_mod.entities.TrainPart;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

/**
 * An item that spawns a minecart entity corresponding to the given train part and collection.
 */
public abstract class TrainPartItem extends TLoZItem {
  private final TrainPart trainPart;
  private final TrainCollection collection;

  public TrainPartItem(final TrainPart trainPart, final TrainCollection collection) {
    super(new Properties().maxStackSize(1).group(TLoZ.CREATIVE_MODE_TAB));
    this.trainPart = trainPart;
    this.collection = collection;
  }

  @Override
  public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
    //noinspection ConstantConditions
    tooltip.add(new TranslationTextComponent(String.format("item.tloz.%s.description", this.getRegistryName().getPath()))
        .setStyle(Style.EMPTY.setFormatting(TextFormatting.GRAY)));
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    World world = context.getWorld();
    BlockPos blockpos = context.getPos();
    BlockState blockstate = world.getBlockState(blockpos);
    if (!blockstate.isIn(BlockTags.RAILS)) {
      return ActionResultType.FAIL;
    } else {
      ItemStack itemstack = context.getItem();
      if (!world.isRemote) {
        RailShape railshape = blockstate.getBlock() instanceof AbstractRailBlock ? ((AbstractRailBlock) blockstate.getBlock()).getRailDirection(blockstate, world, blockpos, null) : RailShape.NORTH_SOUTH;
        AbstractMinecartEntity trainPartEntity = this.trainPart.createMinecartEntity(world, blockpos, railshape.isAscending(), this.collection);
        if (itemstack.hasDisplayName()) {
          trainPartEntity.setCustomName(itemstack.getDisplayName());
        }
        world.addEntity(trainPartEntity);
      }
      itemstack.shrink(1);

      return ActionResultType.SUCCESS;
    }
  }
}
