package net.darmo_creations.tloz_mod.tile_entities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public class BombBreakableBlockTileEntity extends TileEntity {
  private static final String BLOCK_ID_KEY = "Block";

  private Block block;

  public BombBreakableBlockTileEntity() {
    super(ModTileEntities.BOMB_BREAKABLE_BLOCK.get());
    this.block = null;
  }

  public Optional<Block> getBlock() {
    return Optional.ofNullable(this.block);
  }

  public void setBlock(Block block) {
    this.block = block;
    this.markDirty();
    //noinspection ConstantConditions
    this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), 2);
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    super.write(compound);
    if (this.block != null) {
      //noinspection ConstantConditions
      compound.putString(BLOCK_ID_KEY, this.block.getRegistryName().toString());
    }
    return compound;
  }

  @Override
  public void read(BlockState state, CompoundNBT nbt) {
    super.read(state, nbt);
    ResourceLocation name = new ResourceLocation(nbt.getString(BLOCK_ID_KEY));
    if (ForgeRegistries.BLOCKS.containsKey(name)) {
      this.block = ForgeRegistries.BLOCKS.getValue(name);
    } else {
      this.block = null;
    }
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(this.pos, -1, this.write(new CompoundNBT()));
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    this.read(this.getBlockState(), pkt.getNbtCompound());
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return this.write(new CompoundNBT());
  }
}