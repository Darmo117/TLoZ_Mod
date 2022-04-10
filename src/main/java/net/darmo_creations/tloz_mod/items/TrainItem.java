package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.entities.TrainPartEntity;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class TrainItem<T extends TrainPartEntity> extends TLoZItem {
  private final Class<T> trainEntityClass;
  private final TrainPartEntity.Collection collection;

  public TrainItem(final Class<T> trainEntityClass, TrainPartEntity.Collection collection) {
    super(new Properties().maxStackSize(1).group(TLoZ.CREATIVE_MODE_TAB));
    this.trainEntityClass = trainEntityClass;
    this.collection = collection;
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
        double yOffset = 0;
        if (railshape.isAscending()) {
          yOffset = 0.5;
        }
        T trainPartEntity;
        try {
          Constructor<T> constructor = this.trainEntityClass.getConstructor(World.class, TrainPartEntity.Collection.class, double.class, double.class, double.class);
          trainPartEntity = constructor.newInstance(world, this.collection, blockpos.getX() + 0.5, blockpos.getY() + 0.0625 + yOffset, blockpos.getZ() + 0.5);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
          throw new RuntimeException(e);
        }
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
