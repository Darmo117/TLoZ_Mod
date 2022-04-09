package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.blocks.PickableBlock;
import net.darmo_creations.tloz_mod.tile_entities.PickableTileEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * An entity that can be picked up and thrown by the player.
 *
 * @see PickableBlock
 * @see PickableTileEntity
 */
public abstract class PickableEntity extends Entity {
  /**
   * List of entity types that can be hurt by this entity.
   */
  protected static final List<Class<? extends Entity>> HURTABLE_ENTITIES = new ArrayList<>();

  static {
    HURTABLE_ENTITIES.add(PickableEntity.class);
    HURTABLE_ENTITIES.add(BatEntity.class); // TODO custom bat entity?
  }

  private static final String PICKER_KEY = "PickerPlayer";
  private static final String BREAK_ON_COLLISION_KEY = "BreakOnCollision";

  private static final DataParameter<String> PICKER = EntityDataManager.createKey(PickableEntity.class, DataSerializers.STRING);
  private static final DataParameter<Boolean> BREAK_ON_COLLISION = EntityDataManager.createKey(PickableEntity.class, DataSerializers.BOOLEAN);

  protected PlayerEntity picker;
  protected boolean breakOnBlockCollision;

  public PickableEntity(EntityType<? extends PickableEntity> type, World world) {
    super(type, world);
  }

  /**
   * Create a pickable entity.
   *
   * @param breakOnBlockCollision Whether this entity should break/die upon hitting any block.
   * @param picker                Player who should pick this entity. May be null.
   */
  public PickableEntity(EntityType<? extends PickableEntity> entityType, World world, double x, double y, double z,
                        boolean breakOnBlockCollision, PlayerEntity picker) {
    this(entityType, world);
    this.setPosition(x, y, z);
    this.prevPosX = x;
    this.prevPosY = y;
    this.prevPosZ = z;
    this.setPicker(picker);
    this.setBreakOnBlockCollision(breakOnBlockCollision);
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

  @Override
  public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
    return ActionResultType.SUCCESS;
  }

  @Override
  public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
    if (this.getRidingEntity() == player) {
      this.dropFromPlayer(player, true);
    } else {
      this.pickUpByPlayer(player);
    }
    return ActionResultType.SUCCESS;
  }

  /**
   * Make the given player pick up the entity.
   *
   * @param player The player that should pick up this entity.
   */
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

  /**
   * Drop this entity from the player’s hands.
   *
   * @param player The player that should drop this entity.
   * @param yeet   Whether to throw this entity in front of player.
   */
  private void dropFromPlayer(PlayerEntity player, final boolean yeet) {
    this.stopRiding();
    if (yeet) {
      this.throwThis(player, player.rotationPitch, player.rotationYaw);
    }
    this.setPicker(null);
  }

  /**
   * Throw this entity in front of player.
   *
   * @param player The player throwing this entity.
   * @param pitch  Player’s pitch angle.
   * @param yaw    Player’s yaw angle.
   */
  private void throwThis(Entity player, double pitch, double yaw) {
    double speed = 0.45;
    double dx = -Math.sin(yaw * Math.PI / 180) * Math.cos(pitch * Math.PI / 180);
    double dy = -Math.sin(pitch * Math.PI / 180);
    double dz = Math.cos(yaw * Math.PI / 180) * Math.cos(pitch * Math.PI / 180);
    Vector3d motionVec = new Vector3d(dx, dy, dz).normalize().scale(speed);
    this.setMotion(motionVec);
    float horizMagnitude = MathHelper.sqrt(horizontalMag(motionVec));
    this.rotationYaw = (float) (MathHelper.atan2(motionVec.x, motionVec.z) * 180 / Math.PI);
    this.rotationPitch = (float) (MathHelper.atan2(motionVec.y, horizMagnitude) * 180 / Math.PI);
    this.prevRotationYaw = this.rotationYaw;
    this.prevRotationPitch = this.rotationPitch;
    Vector3d vector3d = player.getMotion();
    this.setMotion(this.getMotion().add(vector3d.x, player.isOnGround() ? 0 : vector3d.y, vector3d.z));
  }

  @Override
  public void tick() {
    super.tick();
    System.out.println(this.getPosition()); // DEBUG
    boolean hitBlock = false;
    boolean hitEntity = false;

    if (!this.hasNoGravity()) {
      this.setMotion(this.getMotion().add(0, -0.04, 0));
    }

    this.move(MoverType.SELF, this.getMotion());
    this.setMotion(this.getMotion().scale(0.98));
    if (this.onGround) {
      this.setMotion(this.getMotion().mul(0.7, -0.5, 0.7));
      hitBlock = this.prevPosX != this.getPosX() || this.prevPosY != this.getPosY() || this.prevPosZ != this.getPosZ();
    }
    // TODO detect if hitting side of blocks

    List<Entity> entities = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox(), this::canCollideWith);
    if (!entities.isEmpty()) {
      entities.forEach(entity -> {
        if (entity instanceof PickableEntity) {
          ((PickableEntity) entity).die();
        } else {
          entity.attackEntityFrom(DamageSource.GENERIC, this.getCollisionDamageAmount(entity));
        }
      });
      hitEntity = true;
    }

    if (hitEntity || this.breakOnBlockCollision && hitBlock) {
      this.die();
    } else if (!this.isPassenger() && this.picker != null) {
      this.pickUpByPlayer(this.picker);
    }
  }

  /**
   * Return whether this entity can collide with the given one.
   *
   * @param entity The entity this one’s colliding with.
   * @return True if this entity can collide with the provided entity, false otherwise.
   */
  protected boolean canCollideWith(Entity entity) {
    return HURTABLE_ENTITIES.stream().anyMatch(c -> c.isAssignableFrom(entity.getClass()));
  }

  /**
   * Return the amount of damage to deal to the given entity upon collision.
   *
   * @param entity The entity to deal damage to. Guaranted to not be a {@link PickableEntity}.
   */
  protected float getCollisionDamageAmount(Entity entity) {
    return 0;
  }

  /**
   * Kill this entity and drop its loot if any.
   */
  public void die() {
    if (!this.isAlive()) { // Prevent duplicate drops when thrown on bomb flower or similar
      return;
    }
    this.remove();

    // Play on both client and server as entity may not yet be spawned client-side when dying
    this.playBreakSoundAndAnimation();
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

  protected void playBreakSoundAndAnimation() {
  }

  /**
   * Return a list of items that should be dropped when this entity dies.
   */
  protected List<ItemStack> getDrops() {
    return Collections.emptyList();
  }

  @Override
  protected void registerData() {
    this.dataManager.register(PICKER, "");
    this.dataManager.register(BREAK_ON_COLLISION, false);
  }

  @Override
  public void notifyDataManagerChange(DataParameter<?> key) {
    super.notifyDataManagerChange(key);
    if (PICKER.equals(key)) {
      String s = this.dataManager.get(PICKER);
      if (!"".equals(s)) {
        this.picker = this.world.getPlayerByUuid(UUID.fromString(s));
      }
    } else if (BREAK_ON_COLLISION.equals(key)) {
      this.breakOnBlockCollision = this.dataManager.get(BREAK_ON_COLLISION);
    }
  }

  private void setPicker(PlayerEntity player) {
    this.picker = player;
    this.dataManager.set(PICKER, player != null ? player.getGameProfile().getId().toString() : "");
  }

  private void setBreakOnBlockCollision(boolean breakOnBlockCollision) {
    this.breakOnBlockCollision = breakOnBlockCollision;
    this.dataManager.set(BREAK_ON_COLLISION, breakOnBlockCollision);
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    if (this.picker != null) {
      compound.putUniqueId(PICKER_KEY, this.picker.getGameProfile().getId());
    }
    compound.putBoolean(BREAK_ON_COLLISION_KEY, this.breakOnBlockCollision);
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    if (compound.hasUniqueId(PICKER_KEY)) {
      this.picker = this.world.getPlayerByUuid(compound.getUniqueId(PICKER_KEY));
    } else {
      this.picker = null;
    }
    this.setBreakOnBlockCollision(compound.getBoolean(BREAK_ON_COLLISION_KEY));
  }

  @Override
  protected float getEyeHeight(Pose pose, EntitySize size) {
    return 0.15F;
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  // Drop any pickable entity upon being hurt
  @SubscribeEvent
  public static void onLivingHurt(LivingHurtEvent event) {
    if (event.getEntityLiving() instanceof PlayerEntity) {
      // FIXME sync to client
      PlayerEntity player = (PlayerEntity) event.getEntityLiving();
      player.getPassengers().stream()
          .filter(e -> e instanceof PickableEntity)
          .map(e -> (PickableEntity) e)
          .forEach(e -> e.dropFromPlayer(player, false));
    }
  }
}
