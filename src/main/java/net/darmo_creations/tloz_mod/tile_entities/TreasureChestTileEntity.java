package net.darmo_creations.tloz_mod.tile_entities;

import net.darmo_creations.tloz_mod.UpdateFlags;
import net.darmo_creations.tloz_mod.blocks.TreasureChestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class TreasureChestTileEntity extends SynchronizedTileEntity implements ITickableTileEntity {
  public static final String LOOT_KEY = "Loot";
  public static final String LINKED_INVENTORY_POS_KEY = "LinkedInventoryPos";

  private ItemStack loot;
  private BlockPos linkedInventoryPos;

  public TreasureChestTileEntity() {
    super(ModTileEntities.TREASURE_CHEST.get());
  }

  @Override
  public void tick() {
    if (this.world != null && this.getBlockState().get(TreasureChestBlock.MIMIC) && this.world.getRandom().nextFloat() < 0.0025) {
      if (this.world.isRemote) {
        Minecraft.getInstance().particles.addBlockHitEffects(this.pos, Direction.getRandomDirection(this.world.rand));
      }
      this.world.playSound(null, this.pos, SoundEvents.BLOCK_LADDER_STEP, SoundCategory.BLOCKS, 1, 1);
    }
  }

  public Optional<ItemStack> getLoot() {
    return Optional.ofNullable(this.useOwnInventory()
        ? this.loot
        : this.getLinkedTileEntity().flatMap(TreasureChestTileEntity::getLoot).orElse(null));
  }

  public void setLoot(ItemStack loot) {
    if (this.useOwnInventory()) {
      this.loot = loot;
    } else {
      this.getLinkedTileEntity().ifPresent(te -> te.setLoot(loot));
    }
    this.markDirty();
    //noinspection ConstantConditions
    this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), UpdateFlags.SEND_TO_CLIENT);
  }

  public void setLinkedInventoryPos(BlockPos linkedInventoryPos) {
    this.linkedInventoryPos = linkedInventoryPos;
    this.markDirty();
    //noinspection ConstantConditions
    this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), UpdateFlags.SEND_TO_CLIENT);
  }

  private boolean useOwnInventory() {
    Block block = this.getBlockState().getBlock();
    boolean isMain = this.getBlockState().get(TreasureChestBlock.MAIN);
    return !(block instanceof TreasureChestBlock) || !((TreasureChestBlock) block).isDouble() || !isMain;
  }

  private Optional<TreasureChestTileEntity> getLinkedTileEntity() {
    if (this.linkedInventoryPos == null) {
      return Optional.empty();
    }
    //noinspection ConstantConditions
    TileEntity te = this.world.getTileEntity(this.linkedInventoryPos);
    if (te instanceof TreasureChestTileEntity) {
      return Optional.of((TreasureChestTileEntity) te);
    }
    return Optional.empty();
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    super.write(compound);
    if (this.loot != null) {
      compound.put(LOOT_KEY, this.loot.write(new CompoundNBT()));
    }
    if (this.linkedInventoryPos != null) {
      compound.put(LINKED_INVENTORY_POS_KEY, NBTUtil.writeBlockPos(this.linkedInventoryPos));
    }
    return compound;
  }

  @Override
  public void read(BlockState state, CompoundNBT nbt) {
    super.read(state, nbt);
    if (nbt.contains(LOOT_KEY)) {
      this.loot = ItemStack.read(nbt.getCompound(LOOT_KEY));
    } else {
      this.loot = null;
    }
    if (nbt.contains(LINKED_INVENTORY_POS_KEY)) {
      this.linkedInventoryPos = NBTUtil.readBlockPos(nbt.getCompound(LINKED_INVENTORY_POS_KEY));
    } else {
      this.linkedInventoryPos = null;
    }
  }
}
