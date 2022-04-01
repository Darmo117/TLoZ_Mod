package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.blocks.BombFlowerBlock;
import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.blocks.ShockSwitchBlock;
import net.darmo_creations.tloz_mod.items.BombBagItem;
import net.darmo_creations.tloz_mod.tile_entities.BombFlowerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;

/**
 * A bomb is an entity that explodes after a certain amount of time is elapsed or is hit.
 * <p>
 * Conditions for a bomb to explode:
 * <li>Fuse time is ≤ 0
 * <li>Attacked by an entity
 * <li>Hit by a projectile
 * <li>Collides with another bomb
 *
 * @see BombFlowerBlock
 * @see BombFlowerTileEntity
 * @see BombBagItem
 */
public class BombEntity extends Entity {
  /**
   * List of blocks that can be destroyed.
   */
  private static final List<Block> DESTROYABLE_BLOCKS = new ArrayList<>();
  /**
   * List of blocks that are affected in some way but not destroyed.
   */
  private static final List<Block> AFFECTED_BLOCKS = new ArrayList<>();
  /**
   * List of entity types that can be hurt.
   */
  private static final List<Class<? extends Entity>> HURTABLE_ENTITIES = new ArrayList<>();

  static {
    DESTROYABLE_BLOCKS.add(ModBlocks.JAR);
    DESTROYABLE_BLOCKS.add(ModBlocks.BOMB_BREAKABLE_BLOCK);
    DESTROYABLE_BLOCKS.add(ModBlocks.CRUMBLY_BLOCK);
    DESTROYABLE_BLOCKS.add(Blocks.GRASS);
    DESTROYABLE_BLOCKS.add(Blocks.TALL_GRASS);
    DESTROYABLE_BLOCKS.add(Blocks.FERN);
    DESTROYABLE_BLOCKS.add(Blocks.LARGE_FERN);
    DESTROYABLE_BLOCKS.add(Blocks.DEAD_BUSH);

    AFFECTED_BLOCKS.add(ModBlocks.SHOCK_SWITCH);
    AFFECTED_BLOCKS.add(ModBlocks.BOMB_FLOWER);

    HURTABLE_ENTITIES.add(BombEntity.class);
    HURTABLE_ENTITIES.add(PlayerEntity.class);
    HURTABLE_ENTITIES.add(BatEntity.class);
  }

  private static final String FUSE_KEY = "Fuse";
  private static final String IS_PLANT_KEY = "IsPlant";
  private static final String JUST_SPAWNED_KEY = "JustSpawned";

  private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(BombEntity.class, DataSerializers.VARINT);
  private static final DataParameter<Boolean> IS_PLANT = EntityDataManager.createKey(BombEntity.class, DataSerializers.BOOLEAN);

  public static final float EXPLOSION_SIZE = 2;
  /**
   * Number of hearts to remove from entities’ health.
   */
  public static final int EXPLOSION_DAMAGE = 2;

  private int fuse;
  private boolean isPlant;
  private boolean justSpawned;

  public BombEntity(EntityType<? extends BombEntity> type, World world) {
    super(type, world);
  }

  public BombEntity(World world, double x, double y, double z, int fuse, boolean isPlant) {
    this(ModEntities.BOMB.get(), world);
    this.setPosition(x, y, z);
    this.setFuse(fuse);
    this.setPlant(isPlant);
    this.prevPosX = x;
    this.prevPosY = y;
    this.prevPosZ = z;
    world.playSound(null, x, y, z, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1, 1);
    this.justSpawned = true;
  }

  @Override
  public boolean canBeCollidedWith() {
    return true;
  }

  // Attacked by entity -> explode
  @Override
  public boolean hitByEntity(Entity entity) {
    this.explode();
    return true;
  }

  // FIXME never called
  @Override
  public void applyEntityCollision(Entity entity) {
    System.out.println(entity);
    super.applyEntityCollision(entity);
    if (!(entity instanceof PlayerEntity) && HURTABLE_ENTITIES.contains(entity.getClass())) {
      this.explode();
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
    // Drop currently help bomb
    if (player.isBeingRidden()) {
      Entity rider = player.getPassengers().get(0);
      if (rider instanceof BombEntity) {
        rider.stopRiding();
      }
    }
    // Pickup this bomb
    this.startRiding(player, true);
  }

  private void dropFromPlayer(PlayerEntity player) {
    this.stopRiding();
    this.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw);
  }

  private void setDirectionAndMovement(Entity projectile, double x, double y) {
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
  protected void registerData() {
    this.dataManager.register(FUSE, 80);
    this.dataManager.register(IS_PLANT, false);
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

    --this.fuse;
    if (this.fuse <= 0) {
      this.explode();
    } else {
      this.func_233566_aG_();
      if (this.world.isRemote) {
        this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 1.25, this.getPosZ(), 0, 0, 0);
      }
    }

    this.justSpawned = false;
  }

  public void explode() {
    if (this.justSpawned) {
      return;
    }
    this.remove();

    if (this.world.isRemote) {
      this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS,
          4, (1 + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, false);
      this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getPosX(), this.getPosY(), this.getPosZ(), 1, 0, 0);
    } else {
      final AxisAlignedBB effectMaxArea = new AxisAlignedBB(
          this.getPosX() - EXPLOSION_SIZE, this.getPosY() - EXPLOSION_SIZE, this.getPosZ() - EXPLOSION_SIZE,
          this.getPosX() + EXPLOSION_SIZE, this.getPosY() + EXPLOSION_SIZE, this.getPosZ() + EXPLOSION_SIZE
      );
      this.destroyAndUpdateBlocks(effectMaxArea);
      this.hurtEntities(effectMaxArea);
    }
  }

  private void destroyAndUpdateBlocks(AxisAlignedBB effectMaxArea) {
    if (this.world.getFluidState(this.getPosition()).isEmpty()) {
      BlockPos.getAllInBox(effectMaxArea)
          .filter(pos -> {
            BlockState blockState = this.world.getBlockState(pos);
            double distance = Math.sqrt(
                Math.pow(pos.getX() + 0.5 - this.getPosX(), 2)
                    + Math.pow(pos.getY() + 0.5 - this.getPosY(), 2)
                    + Math.pow(pos.getZ() + 0.5 - this.getPosZ(), 2)
            );
            return distance <= EXPLOSION_SIZE
                && (DESTROYABLE_BLOCKS.contains(blockState.getBlock())
                || AFFECTED_BLOCKS.contains(blockState.getBlock()));
          })
          .forEach(pos -> {
            BlockState blockState = this.world.getBlockState(pos);
            Block block = blockState.getBlock();
            if (block == ModBlocks.BOMB_FLOWER) {
              TileEntity te = this.world.getTileEntity(pos);
              if (te instanceof BombFlowerTileEntity && ((BombFlowerTileEntity) te).hasBomb()) {
                ((BombFlowerTileEntity) te).popBomb(null, this.world, 3); // Small delay to mimic in-game behavior
              }
            } else if (block == ModBlocks.SHOCK_SWITCH) {
              ((ShockSwitchBlock) block).toggleState(blockState, this.world, pos);
            } else {
              Block.spawnDrops(blockState, this.world, pos, this.world.getTileEntity(pos));
              this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            }
          });
    }
  }

  private void hurtEntities(AxisAlignedBB effectMaxArea) {
    this.world.getEntitiesWithinAABBExcludingEntity(this, effectMaxArea).stream()
        .filter(entity -> {
          double distance = Math.sqrt(
              Math.pow(entity.getPosX() - this.getPosX(), 2)
                  + Math.pow(entity.getPosY() - this.getPosY(), 2)
                  + Math.pow(entity.getPosZ() - this.getPosZ(), 2)
          );
          return distance <= EXPLOSION_SIZE && entity.isAlive() && HURTABLE_ENTITIES.stream().anyMatch(c -> c.isAssignableFrom(entity.getClass()));
        })
        .forEach(entity -> {
          if (entity instanceof BombEntity) {
            ((BombEntity) entity).explode();
          } else {
            entity.attackEntityFrom(DamageSource.causeExplosionDamage((LivingEntity) null), EXPLOSION_DAMAGE);
          }
        });
  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {
    compound.putShort(FUSE_KEY, (short) this.getFuse());
    compound.putBoolean(IS_PLANT_KEY, this.isPlant());
    compound.putBoolean(JUST_SPAWNED_KEY, this.justSpawned);
  }

  @Override
  protected void readAdditional(CompoundNBT compound) {
    this.setFuse(compound.getShort(FUSE_KEY));
    this.setPlant(compound.getBoolean(IS_PLANT_KEY));
    this.setPlant(compound.getBoolean(JUST_SPAWNED_KEY));
  }

  @Override
  protected float getEyeHeight(Pose pose, EntitySize size) {
    return 0.15F;
  }

  public void setFuse(int fuse) {
    this.dataManager.set(FUSE, fuse);
    this.fuse = fuse;
  }

  public void setPlant(boolean plant) {
    this.dataManager.set(IS_PLANT, plant);
    this.isPlant = plant;
  }

  @Override
  public void notifyDataManagerChange(DataParameter<?> key) {
    if (FUSE.equals(key)) {
      this.fuse = this.dataManager.get(FUSE);
    } else if (IS_PLANT.equals(key)) {
      this.isPlant = this.dataManager.get(IS_PLANT);
    }
  }

  public int getFuse() {
    return this.fuse;
  }

  public boolean isPlant() {
    return this.isPlant;
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
