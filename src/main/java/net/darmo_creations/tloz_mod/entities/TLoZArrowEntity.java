package net.darmo_creations.tloz_mod.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class TLoZArrowEntity extends AbstractArrowEntity {
  private static final String LIGHT_ARROW_KEY = "LightArrow";

  private static final DataParameter<Boolean> LIGHT_ARROW = EntityDataManager.createKey(TLoZArrowEntity.class, DataSerializers.BOOLEAN);

  private boolean lightArrow;

  public TLoZArrowEntity(EntityType<? extends TLoZArrowEntity> type, World world) {
    super(type, world);
  }

  public TLoZArrowEntity(World world, LivingEntity shooter, boolean lightArrow) {
    super(ModEntities.ARROW.get(), shooter, world);
    this.setLightArrow(lightArrow);
  }

  public boolean isLightArrow() {
    return this.lightArrow;
  }

  @Override
  public void tick() {
    super.tick();
    if (this.inGround) {
      this.remove();
    }
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(LIGHT_ARROW, false);
  }

  private void setLightArrow(boolean lightArrow) {
    this.lightArrow = lightArrow;
    this.dataManager.set(LIGHT_ARROW, lightArrow);
  }

  @Override
  public void writeAdditional(CompoundNBT compound) {
    super.writeAdditional(compound);
    compound.putBoolean(LIGHT_ARROW_KEY, this.lightArrow);
  }

  @Override
  public void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    this.setLightArrow(compound.getBoolean(LIGHT_ARROW_KEY));
  }

  @Override
  protected ItemStack getArrowStack() {
    return new ItemStack(Items.ARROW);
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
