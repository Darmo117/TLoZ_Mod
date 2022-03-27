package net.darmo_creations.tloz_mod.tile_entities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

public class OrbSwitchTileEntity extends TileEntity {
  private static final String ACTIVATED_KEY = "activated";

  private boolean activated;

  public OrbSwitchTileEntity() {
    super(TileEntityTypes.ORB_SWITCH);
    this.activated = false;
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    super.write(compound);
    compound.putBoolean(ACTIVATED_KEY, this.activated);
    return compound;
  }

  @Override
  public void read(BlockState state, CompoundNBT nbt) {
    super.read(state, nbt);
    this.activated = nbt.getBoolean(ACTIVATED_KEY);
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(this.pos, 2, this.write(new CompoundNBT()));
  }
}
