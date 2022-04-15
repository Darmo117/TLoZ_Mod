package net.darmo_creations.tloz_mod.entities.trains;

import net.darmo_creations.tloz_mod.entities.ModEntities;
import net.darmo_creations.tloz_mod.items.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TrainCoachEntity extends RollingStockEntity {
  public TrainCoachEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public TrainCoachEntity(World world, TrainCollection collection, double x, double y, double z) {
    super(ModEntities.TRAIN_COACH.get(), world, collection, x, y, z);
  }

  @Override
  public Type getMinecartType() {
    return Type.RIDEABLE;
  }

  @Override
  public ItemStack getCartItem() {
    // TODO
    switch (this.getCollection()) {
      case SPIRIT:
        return new ItemStack(ModItems.SPIRIT_COACH);
      case WOODEN:
        break;
      case STEEL:
        break;
      case SKULL:
        break;
      case STAGECOACH:
        break;
      case DRAGON:
        break;
      case SWEET:
        break;
      case GOLDEN:
        break;
    }
    return null;
  }
}
