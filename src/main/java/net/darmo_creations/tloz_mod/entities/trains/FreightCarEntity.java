package net.darmo_creations.tloz_mod.entities.trains;

import net.darmo_creations.tloz_mod.entities.ModEntities;
import net.darmo_creations.tloz_mod.items.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FreightCarEntity extends RollingStockEntity {
  public FreightCarEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public FreightCarEntity(World world, TrainCollection collection, double x, double y, double z) {
    super(ModEntities.FREIGHT_CAR.get(), world, collection, x, y, z);
  }

  @Override
  public Type getMinecartType() {
    return Type.CHEST;
  }

  @Override
  public ItemStack getCartItem() {
    // TODO
    switch (this.getCollection()) {
      case SPIRIT:
        return new ItemStack(ModItems.SPIRIT_FREIGHT_CAR);
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
