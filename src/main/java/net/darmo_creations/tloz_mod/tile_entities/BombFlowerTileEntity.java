package net.darmo_creations.tloz_mod.tile_entities;

import net.darmo_creations.tloz_mod.blocks.BombFlowerBlock;
import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Tile entity for the {@link BombFlowerBlock}.
 *
 * @see BombFlowerBlock
 * @see BombEntity
 */
public class BombFlowerTileEntity extends TileEntity implements ITickableTileEntity {
  public static final int FUSE_DELAY = 140; // 7 seconds

  public static final String DELAY_KEY = "Delay";
  public static final String GROWTH_STAGE_KEY = "GrowthStage";
  public static final String HAS_BOMB_KEY = "HasBomb";

  private static final float GROWTH_STEP = 0.1f;

  private int delay;
  private float growthStage;
  private boolean hasBomb;

  public BombFlowerTileEntity() {
    super(ModTileEntities.BOMB_FLOWER.get());
    this.delay = 0;
    this.growthStage = 1;
    this.hasBomb = true;
  }

  public boolean popBomb(PlayerEntity player, World world, int fuse) {
    if (!this.hasBomb) {
      return false;
    }

    BlockPos pos = this.getPos();
    BombEntity bomb = new BombEntity(world, pos.getX() + 0.5, pos.getY() + 0.0625, pos.getZ() + 0.5, fuse, true);
    world.addEntity(bomb);
    this.hasBomb = false;
    this.delay = FUSE_DELAY;
    this.growthStage = 0;
    this.markDirty();
    world.notifyBlockUpdate(pos, this.getBlockState(), this.getBlockState(), 2);

    return true;
  }

  @Override
  public void tick() {
    if (this.hasBomb) {
      return;
    }

    if (this.delay > 0) {
      this.delay--;
    } else {
      if (this.growthStage < 1) {
        PlayerEntity player = null;
        if (this.world != null) {
          BlockPos pos = this.getPos();
          player = this.world.getClosestPlayer(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.5, true);
        }
        if (player == null || this.growthStage > 0) {
          this.growthStage += GROWTH_STEP;
        }
      } else {
        this.growthStage = 1; // Account for precision errors
        this.hasBomb = true;
      }
    }
    this.markDirty();
    if (this.world != null) {
      this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), 2);
    }
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    super.write(compound);
    compound.putInt(DELAY_KEY, this.delay);
    compound.putFloat(GROWTH_STAGE_KEY, this.growthStage);
    compound.putBoolean(HAS_BOMB_KEY, this.hasBomb);
    return compound;
  }

  @Override
  public void read(BlockState state, CompoundNBT nbt) {
    super.read(state, nbt);
    this.delay = nbt.getInt(DELAY_KEY);
    this.growthStage = nbt.getFloat(GROWTH_STAGE_KEY);
    this.hasBomb = nbt.getBoolean(HAS_BOMB_KEY);
  }

  public float getGrowthStage() {
    return this.growthStage;
  }

  public boolean hasBomb() {
    return this.hasBomb;
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
