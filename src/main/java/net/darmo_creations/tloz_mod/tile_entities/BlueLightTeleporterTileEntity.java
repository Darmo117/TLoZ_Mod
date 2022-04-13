package net.darmo_creations.tloz_mod.tile_entities;

import net.darmo_creations.tloz_mod.ModSoundEvents;
import net.darmo_creations.tloz_mod.UpdateFlags;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class BlueLightTeleporterTileEntity extends SynchronizedTileEntity implements ITickableTileEntity {
  private static final String TARGET_POS_KEY = "TargetPos";
  private static final String YAW_KEY = "Yaw";
  private static final String PITCH_KEY = "Pitch";

  private BlockPos targetPos;
  private Float yaw;
  private Float pitch;
  private int ticks;

  public BlueLightTeleporterTileEntity() {
    super(ModTileEntities.BLUE_LIGHT_TELEPORTER.get());
  }

  public Optional<BlockPos> getTargetPos() {
    return Optional.ofNullable(this.targetPos);
  }

  public Optional<Float> getYaw() {
    return Optional.ofNullable(this.yaw);
  }

  public Optional<Float> getPitch() {
    return Optional.ofNullable(this.pitch);
  }

  public void setTargetPos(BlockPos targetPos) {
    this.targetPos = targetPos;
    this.markDirty();
    //noinspection ConstantConditions
    this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), UpdateFlags.SEND_TO_CLIENT);
  }

  @Override
  public void tick() {
    //noinspection ConstantConditions
    if (!this.world.isRemote && this.ticks % 8 == 0) {
      this.world.playSound(null, this.getPos(), ModSoundEvents.TELEPORTER_HUM, SoundCategory.BLOCKS, 0.1f, 1);
    }
    this.ticks++;
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    super.write(compound);
    if (this.targetPos != null) {
      compound.put(TARGET_POS_KEY, NBTUtil.writeBlockPos(this.targetPos));
    }
    if (this.yaw != null) {
      compound.putFloat(YAW_KEY, this.yaw);
    }
    if (this.pitch != null) {
      compound.putFloat(PITCH_KEY, this.pitch);
    }
    return compound;
  }

  @Override
  public void read(BlockState state, CompoundNBT nbt) {
    super.read(state, nbt);
    this.targetPos = nbt.contains(TARGET_POS_KEY) ? NBTUtil.readBlockPos(nbt.getCompound(TARGET_POS_KEY)) : null;
    this.yaw = nbt.contains(YAW_KEY) ? nbt.getFloat(YAW_KEY) : null;
    this.pitch = nbt.contains(PITCH_KEY) ? nbt.getFloat(PITCH_KEY) : null;
  }
}
