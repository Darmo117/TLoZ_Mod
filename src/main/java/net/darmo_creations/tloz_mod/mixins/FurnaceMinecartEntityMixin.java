package net.darmo_creations.tloz_mod.mixins;

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
import net.minecraft.world.World;
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
//      System.out.println(this.rotationYaw); // DEBUG
      List<Entity> passengers = this.getPassengers();
      if (!passengers.isEmpty() && passengers.get(0) instanceof PlayerEntity) {
        this.fuel = 1;
      } else {
        this.pushX = 0;
        this.pushZ = 0;
        this.fuel = 0;
      }

      this.setMinecartPowered(this.fuel > 0);
    }

    if (this.isMinecartPowered() && this.rand.nextInt(4) == 0) {
      this.world.addParticle(ParticleTypes.LARGE_SMOKE, this.getPosX(), this.getPosY() + 0.8, this.getPosZ(), 0, 0, 0);
    }
  }

  @Shadow
  protected abstract boolean isMinecartPowered();

  @Shadow
  protected abstract void setMinecartPowered(boolean powered);

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
