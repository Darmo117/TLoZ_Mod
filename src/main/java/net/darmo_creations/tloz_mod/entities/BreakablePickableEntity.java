package net.darmo_creations.tloz_mod.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

/**
 * A pickable entity that breaks upon hitting another block.
 */
public abstract class BreakablePickableEntity extends PickableEntity {
  private static final String BREAK_INSTANTLY_KEY = "BreakInstantly";

  private static final DataParameter<Boolean> BREAK_INSTANTLY = EntityDataManager.createKey(BreakablePickableEntity.class, DataSerializers.BOOLEAN);

  private boolean breakInstantly;

  public BreakablePickableEntity(EntityType<? extends BreakablePickableEntity> type, World world) {
    super(type, world);
  }

  public BreakablePickableEntity(EntityType<? extends BreakablePickableEntity> type, World world, double x, double y, double z,
                                 boolean breakInstantly, PlayerEntity picker) {
    super(type, world, x, y, z, true, picker);
    this.setBreakInstantly(breakInstantly);
  }

  @Override
  public void tick() {
    if (this.breakInstantly) {
      this.die();
      return;
    }
    super.tick();
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(BREAK_INSTANTLY, false);
  }

  private void setBreakInstantly(boolean breakInstantly) {
    this.breakInstantly = breakInstantly;
    this.dataManager.set(BREAK_INSTANTLY, breakInstantly);
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    super.writeAdditional(compound);
    compound.putBoolean(BREAK_INSTANTLY_KEY, this.breakInstantly);
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    this.setBreakInstantly(compound.getBoolean(BREAK_INSTANTLY_KEY));
  }
}
