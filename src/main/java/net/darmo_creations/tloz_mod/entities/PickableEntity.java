package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.blocks.PickableBlock;
import net.darmo_creations.tloz_mod.tile_entities.PickableTileEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;

/**
 * An entity that can be picked up by the player.
 *
 * @see PickableBlock
 * @see PickableTileEntity
 */
public abstract class PickableEntity extends Entity {
  private static final String PICKER_KEY = "PickerPlayer";

  private static final DataParameter<String> PICKER = EntityDataManager.createKey(PickableEntity.class, DataSerializers.STRING);

  private PlayerEntity picker;

  public PickableEntity(EntityType<? extends PickableEntity> type, World world) {
    super(type, world);
  }

  public PickableEntity(EntityType<? extends PickableEntity> entityType, World world, double x, double y, double z, PlayerEntity picker) {
    this(entityType, world);
    this.setPosition(x, y, z);
    this.prevPosX = x;
    this.prevPosY = y;
    this.prevPosZ = z;
    this.setPicker(picker);
  }

  @Override
  public boolean canBeCollidedWith() {
    return true;
  }

  // Attacked by entity -> break
  @Override
  public boolean hitByEntity(Entity entity) {
    this.die();
    return true;
  }

  // FIXME never called -> re-implement from tick() method?
  // Collided with another pickable entity -> break
  @Override
  public void applyEntityCollision(Entity entity) {
    System.out.println(entity); // DEBUG
    super.applyEntityCollision(entity);
    if (entity instanceof PickableEntity) {
      ((PickableEntity) entity).die();
    }
  }

  @Override
  public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
    return ActionResultType.SUCCESS;
  }

  @Override
  public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
    if (this.getRidingEntity() == player) {
      this.dropFromPlayer(player);
    } else {
      this.pickUpByPlayer(player);
    }
    return ActionResultType.SUCCESS;
  }

  public void pickUpByPlayer(PlayerEntity player) {
    // Drop currently help entity
    if (player.isBeingRidden()) {
      Entity rider = player.getPassengers().get(0);
      if (rider instanceof PickableEntity) {
        rider.stopRiding();
      }
    }
    // Pickup this entity
    this.startRiding(player, true);
  }

  private void dropFromPlayer(PlayerEntity player) {
    this.stopRiding();
    this.throwThis(player, player.rotationPitch, player.rotationYaw);
    this.setPicker(null);
  }

  private void throwThis(Entity projectile, double x, double y) {
    double speed = 0.45;
    double dx = -Math.sin(y * Math.PI / 180) * Math.cos(x * Math.PI / 180);
    double dy = -Math.sin(x * Math.PI / 180);
    double dz = Math.cos(y * Math.PI / 180) * Math.cos(x * Math.PI / 180);
    Vector3d motionVec = new Vector3d(dx, dy, dz).normalize().scale(speed);
    this.setMotion(motionVec);
    float horizMagnitude = MathHelper.sqrt(horizontalMag(motionVec));
    this.rotationYaw = (float) (MathHelper.atan2(motionVec.x, motionVec.z) * 180 / Math.PI);
    this.rotationPitch = (float) (MathHelper.atan2(motionVec.y, horizMagnitude) * 180 / Math.PI);
    this.prevRotationYaw = this.rotationYaw;
    this.prevRotationPitch = this.rotationPitch;
    Vector3d vector3d = projectile.getMotion();
    this.setMotion(this.getMotion().add(vector3d.x, projectile.isOnGround() ? 0 : vector3d.y, vector3d.z));
  }

  @Override
  public void tick() {
    super.tick();

    if (!this.hasNoGravity()) {
      this.setMotion(this.getMotion().add(0, -0.04, 0));
    }

    this.move(MoverType.SELF, this.getMotion());
    this.setMotion(this.getMotion().scale(0.98));
    if (this.onGround) {
      this.setMotion(this.getMotion().mul(0.7, -0.5, 0.7));
    }

    if (!this.isPassenger() && this.picker != null) {
      this.pickUpByPlayer(this.picker);
    }
  }

  public void die() {
    this.remove();

    if (!this.world.isRemote) {
      List<ItemStack> drops = this.getDrops();
      if (!drops.isEmpty()) {
        drops.forEach(stack -> {
          ItemEntity itemEntity = new ItemEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), stack);
          itemEntity.setNoPickupDelay();
          this.world.addEntity(itemEntity);
        });
      }
    }
  }

  protected abstract List<ItemStack> getDrops();

  @Override
  protected void registerData() {
    this.dataManager.register(PICKER, "");
  }

  @Override
  public void notifyDataManagerChange(DataParameter<?> key) {
    super.notifyDataManagerChange(key);
    if (PICKER.equals(key)) {
      String s = this.dataManager.get(PICKER);
      if (!"".equals(s)) {
        this.picker = this.world.getPlayerByUuid(UUID.fromString(s));
      }
    }
  }

  private void setPicker(PlayerEntity player) {
    this.picker = player;
    this.dataManager.set(PICKER, player != null ? player.getGameProfile().getId().toString() : "");
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    if (this.picker != null) {
      compound.putUniqueId(PICKER_KEY, this.picker.getGameProfile().getId());
    }
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    if (compound.hasUniqueId(PICKER_KEY)) {
      this.picker = this.world.getPlayerByUuid(compound.getUniqueId(PICKER_KEY));
    } else {
      this.picker = null;
    }
  }

  @Override
  protected float getEyeHeight(Pose pose, EntitySize size) {
    return 0.15F;
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
