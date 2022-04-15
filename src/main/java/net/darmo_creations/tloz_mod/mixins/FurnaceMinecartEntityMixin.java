package net.darmo_creations.tloz_mod.mixins;

import net.darmo_creations.tloz_mod.Utils;
import net.darmo_creations.tloz_mod.entities.TrainSpeedSetting;
import net.darmo_creations.tloz_mod.entities.capabilities.TrainSpeedSettingCapabilityManager;
import net.darmo_creations.tloz_mod.entities.capabilities.TrainSpeedSettingWrapper;
import net.darmo_creations.tloz_mod.network.ModNetworkManager;
import net.darmo_creations.tloz_mod.network.TrainSpeedMessage;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.FurnaceMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@SuppressWarnings("UnusedMixin") // Intellij does not detect JSON file
@Mixin(value = FurnaceMinecartEntity.class, priority = 500)
public abstract class FurnaceMinecartEntityMixin extends AbstractMinecartEntity {
  @Shadow
  private int fuel;
  @Shadow
  public double pushX;
  @Shadow
  public double pushZ;

  public FurnaceMinecartEntityMixin(EntityType<?> entityType, World world) {
    super(entityType, world);
  }

  /**
   * @author Darmo
   * @reason Remove need for fuel, make ridable.
   */
  @Override
  @Overwrite
  public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
    if (player.isSecondaryUseActive() || this.isBeingRidden()) {
      return ActionResultType.PASS;
    } else if (!this.world.isRemote) {
      return player.startRiding(this) ? ActionResultType.CONSUME : ActionResultType.PASS;
    } else {
      return ActionResultType.SUCCESS;
    }
  }

  /**
   * @author Darmo
   * @reason Modify furnace minecarts’ behavior.
   */
  @Override
  @Overwrite
  public void tick() {
    super.tick();
    if (!this.world.isRemote()) {
      List<Entity> passengers = this.getPassengers();
      if (!passengers.isEmpty() && passengers.get(0) instanceof PlayerEntity) {
        // TODO set speed to IDLE when minecart hits block
//        TrainSpeedSetting speedSetting = this.getCapability(TrainSpeedSettingCapabilityManager.INSTANCE)
//            .orElseGet(TrainSpeedSettingWrapper::new).getSpeedSetting();
//        if (this.prevPosX == this.getPosX() && this.prevPosY == this.getPosY() && this.prevPosZ == this.getPosZ() && speedSetting != TrainSpeedSetting.IDLE) {
//          this.pushX = 0;
//          this.pushZ = 0;
//          this.fuel = 0;
//          this.getCapability(TrainSpeedSettingCapabilityManager.INSTANCE)
//              .ifPresent(trainSpeedSettingWrapper -> trainSpeedSettingWrapper.setSpeedSetting(TrainSpeedSetting.IDLE));
//          // Send to clients
//          ModNetworkManager.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new TrainSpeedMessage(TrainSpeedSetting.IDLE, this.getEntityId()));
//        } else {
        this.fuel = 1;
//        }
      } else {
        this.pushX = 0;
        this.pushZ = 0;
        this.fuel = 0;
        this.getCapability(TrainSpeedSettingCapabilityManager.INSTANCE)
            .ifPresent(trainSpeedSettingWrapper -> trainSpeedSettingWrapper.setSpeedSetting(TrainSpeedSetting.IDLE));
        // Send to clients
        ModNetworkManager.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new TrainSpeedMessage(TrainSpeedSetting.IDLE, this.getEntityId()));
      }

      this.setMinecartPowered(this.fuel > 0);
    }

    if (this.isMinecartPowered() && this.rand.nextInt(4) == 0) {
      this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getPosX(), this.getPosY() + 0.8, this.getPosZ(), 0, 0, 0);
    }
  }

  /**
   * @author Darmo
   * @reason DEBUG
   */
  @Override
  @Overwrite
  protected void moveAlongTrack(BlockPos pos, BlockState state) {
    super.moveAlongTrack(pos, state);
    Vector3d motion = this.getMotion();
    double horizontalMag = horizontalMag(motion);
    double pushMag = this.pushX * this.pushX + this.pushZ * this.pushZ;
    // FIXME overrides pushX and pushZ when speed setting changes and speed is higher than 0.001
    Utils.print(this.pushX, this.pushZ, pushMag); // DEBUG
    if (pushMag > 1e-4/* && horizontalMag > 0.001*/) {
      double d4 = MathHelper.sqrt(horizontalMag);
      double d5 = MathHelper.sqrt(pushMag);
      this.pushX = motion.x / d4 * d5;
      this.pushZ = motion.z / d4 * d5;
    }
  }

  /**
   * @author Darmo
   * @reason Control movement speed and direction through {@link TrainSpeedSettingCapabilityManager}.
   */
  @Override
  @Overwrite
  protected void applyDrag() {
    double DRAG = 0.85;
    double pushMagnitudeSq = this.pushX * this.pushX + this.pushZ * this.pushZ;
    if (pushMagnitudeSq > 1e-7) {
      TrainSpeedSetting speedSetting = this.getCapability(TrainSpeedSettingCapabilityManager.INSTANCE)
          .orElseGet(TrainSpeedSettingWrapper::new).getSpeedSetting();
      double pushMagnitude = MathHelper.sqrt(pushMagnitudeSq);
      double speedCoefficient = speedSetting.getMagnitude() / 20.0;
      this.pushX = speedCoefficient * this.pushX / pushMagnitude;
      this.pushZ = speedCoefficient * this.pushZ / pushMagnitude;
      this.setMotion(this.getMotion().mul(DRAG, 0, DRAG).add(this.pushX, 0, this.pushZ));
      System.out.println(this.getMotion()); // DEBUG
    } else {
      this.setMotion(this.getMotion().mul(DRAG, 0, DRAG));
    }
  }

  @Shadow
  protected abstract boolean isMinecartPowered();

  @Shadow
  protected abstract void setMinecartPowered(boolean powered);

  /**
   * @author Darmo
   * @reason Restore default max speed.
   */
  @Override
  @Overwrite
  protected double getMaximumSpeed() {
    TrainSpeedSetting speedSetting = this.getCapability(TrainSpeedSettingCapabilityManager.INSTANCE)
        .orElseGet(TrainSpeedSettingWrapper::new).getSpeedSetting();
    return 0.1 * speedSetting.getMagnitude();
  }

  /**
   * @author Darmo
   * @reason Restore default max speed.
   */
  @Override
  @Overwrite(remap = false) // https://github.com/SpongePowered/Mixin/issues/292#issuecomment-431660620
  public float getMaxCartSpeedOnRail() {
    return 1.2f;
  }

  /**
   * @author Darmo
   * @reason Make ridable.
   */
  @Override
  @Overwrite
  public Type getMinecartType() {
    return Type.RIDEABLE;
  }

  /**
   * @author Darmo
   * @reason Change displayed block.
   */
  @Override
  @Overwrite
  public BlockState getDefaultDisplayTile() {
    return Blocks.RED_CARPET.getDefaultState();
  }
}
