package net.darmo_creations.tloz_mod.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class TestMinecartEntity extends AbstractMinecartEntity {
  public TestMinecartEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  @SuppressWarnings("unused")
  public TestMinecartEntity(World world, double x, double y, double z) {
    super(ModEntities.TEST_MINECART.get(), world, x, y, z);
  }

  @Override
  public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
    if (player.isSecondaryUseActive() || this.isBeingRidden()) {
      return ActionResultType.PASS;
    } else if (!this.world.isRemote) {
      return player.startRiding(this) ? ActionResultType.CONSUME : ActionResultType.PASS;
    } else {
      return ActionResultType.SUCCESS;
    }
  }

  @Override
  public Type getMinecartType() {
    return Type.RIDEABLE;
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
