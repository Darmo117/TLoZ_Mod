package net.darmo_creations.tloz_mod.entities.trains;

import net.darmo_creations.tloz_mod.entities.ModEntities;
import net.darmo_creations.tloz_mod.items.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RailCannonEntity extends RollingStockEntity {
  public RailCannonEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  public RailCannonEntity(World world, TrainCollection collection, double x, double y, double z) {
    super(ModEntities.RAIL_CANNON.get(), world, collection, x, y, z);
  }

  @Override
  public Type getMinecartType() {
    return Type.TNT;
  }

  @Override
  public ItemStack getCartItem() {
    // TODO
    switch (this.getCollection()) {
      case SPIRIT:
        return new ItemStack(ModItems.SPIRIT_CANNON);
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
