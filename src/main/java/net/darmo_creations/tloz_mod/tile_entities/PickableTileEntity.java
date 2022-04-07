package net.darmo_creations.tloz_mod.tile_entities;

import net.darmo_creations.tloz_mod.UpdateFlags;
import net.darmo_creations.tloz_mod.blocks.PickableBlock;
import net.darmo_creations.tloz_mod.entities.PickableEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Tile entity for {@link PickableBlock}.
 * <p>
 * Handles spawning of associated {@link PickableEntity}.
 *
 * @see PickableBlock
 * @see PickableEntity
 */
public abstract class PickableTileEntity extends SynchronizedTileEntity implements ITickableTileEntity {
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

  @SuppressWarnings("BooleanMethodIsAlwaysInverted")
  protected boolean resetGrowthTimer() {
    if (!this.hasBlock || this.world == null) {
      return false;
    }

    this.hasBlock = false;
    this.delay = this.maxGrowthDelay;
    this.growthStage = 0;
    this.markDirty();
    this.world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), UpdateFlags.SEND_TO_CLIENT);

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
        BlockPos pos = this.getPos();
        //noinspection ConstantConditions
        List<Entity> entities = this.world.getEntitiesWithinAABB(
            Entity.class,
            new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)
        );
        if (entities.isEmpty() || this.growthStage > 0) {
          this.growthStage += GROWTH_STEP;
        }
      } else {
        this.growthStage = 1; // Account for precision errors
        this.hasBlock = true;
      }
    }
    this.markDirty();
    //noinspection ConstantConditions
    this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), UpdateFlags.SEND_TO_CLIENT);
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
}
