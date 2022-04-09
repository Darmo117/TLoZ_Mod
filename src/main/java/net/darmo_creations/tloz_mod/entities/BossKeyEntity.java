package net.darmo_creations.tloz_mod.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class BossKeyEntity extends PickableEntity {
  public BossKeyEntity(EntityType<? extends PickableEntity> type, World world) {
    super(type, world);
  }

  public BossKeyEntity(World world, double x, double y, double z, PlayerEntity picker) {
    super(ModEntities.BOSS_KEY.get(), world, x, y, z, false, picker);
  }

  @Override
  public void die() {
    // Cannot die upon collision or bomb explosion
  }
}
