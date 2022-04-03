package net.darmo_creations.tloz_mod.tile_entities;

import net.darmo_creations.tloz_mod.blocks.PickableBlock;
import net.darmo_creations.tloz_mod.entities.PickableEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

/**
 * Tile entity for {@link PickableBlock}.
 * <p>
 * Handles spawning of associated {@link PickableEntity}.
 *
 * @see PickableBlock
 * @see PickableEntity
 */
public abstract class PickableTileEntity extends TileEntity implements ITickableTileEntity {
  public static final String GROWABLE_KEY = "Growable";
  public static final String GROWTH_DELAY_KEY = "GrowthDelay";
  public static final String DELAY_KEY = "Delay";
  public static final String GROWTH_STAGE_KEY = "GrowthStage";
  public static final String HAS_BOMB_KEY = "HasBomb";

  private static final float GROWTH_STEP = 0.1f;

  private boolean growable;
  private int maxGrowthDelay;

  private int delay;
  private float growthStage;
  private boolean hasBlock;

  public PickableTileEntity(TileEntityType<?> type) {
    this(type, -1);
  }

  public PickableTileEntity(TileEntityType<?> type, final int growthDelay) {
    super(type);
    this.growable = growthDelay >= 0;
    this.maxGrowthDelay = growthDelay;
    this.delay = 0;
    this.growthStage = 1;
    this.hasBlock = true;
  }

  protected boolean resetGrowthTimer() {
    if (!this.hasBlock || this.world == null) {
      return false;
    }

    this.hasBlock = false;
    this.delay = this.maxGrowthDelay;
    this.growthStage = 0;
    this.markDirty();
    this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 2);

    return true;
  }

  @Override
  public void tick() {
    if (!this.growable || this.hasBlock) {
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
        this.hasBlock = true;
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
    compound.putBoolean(GROWABLE_KEY, this.growable);
    compound.putInt(GROWTH_DELAY_KEY, this.maxGrowthDelay);
    compound.putInt(DELAY_KEY, this.delay);
    compound.putFloat(GROWTH_STAGE_KEY, this.growthStage);
    compound.putBoolean(HAS_BOMB_KEY, this.hasBlock);
    return compound;
  }

  @Override
  public void read(BlockState state, CompoundNBT nbt) {
    super.read(state, nbt);
    this.growable = nbt.getBoolean(GROWABLE_KEY);
    this.maxGrowthDelay = nbt.getInt(GROWTH_DELAY_KEY);
    this.delay = nbt.getInt(DELAY_KEY);
    this.growthStage = nbt.getFloat(GROWTH_STAGE_KEY);
    this.hasBlock = nbt.getBoolean(HAS_BOMB_KEY);
  }

  public float getGrowthStage() {
    return this.growthStage;
  }

  public boolean hasBlock() {
    return this.hasBlock;
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
