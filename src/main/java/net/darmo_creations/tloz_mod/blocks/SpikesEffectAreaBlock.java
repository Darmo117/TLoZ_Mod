package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.tile_entities.SpikesEffectAreaTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class SpikesEffectAreaBlock extends Block implements ITileEntityProvider, IModBlock {
  public static final List<Class<? extends Entity>> IMMUNE_ENTITIES = new ArrayList<>();

  static {
    // TODO add phantoms to immunity list
    IMMUNE_ENTITIES.add(ItemEntity.class);
  }

  public SpikesEffectAreaBlock() {
    super(Properties.create(Material.AIR)
        .notSolid()
        .doesNotBlockMovement()
        .noDrops()
        .setAir()
        .setAllowsSpawn((blockState, blockReader, pos, entityType) -> false));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.empty();
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    return facing == Direction.DOWN && !state.isValidPosition(world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
    return world.getBlockState(pos.down()).getBlock() == ModBlocks.SPIKES;
  }

  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
    if (!this.isValidPosition(state, world, pos)) {
      world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    dealDamageAndKnockback(pos, entity);
  }

  public static void dealDamageAndKnockback(BlockPos pos, Entity entity) {
    if (IMMUNE_ENTITIES.stream().noneMatch(c -> c.isAssignableFrom(entity.getClass()))) {
      entity.attackEntityFrom(DamageSource.GENERIC, 1);
      if (entity instanceof LivingEntity) {
        // Apply knockback to prevent entity from passing through the block.
        int blockX = pos.getX();
        int blockZ = pos.getZ();
        double entityX = entity.getPosX();
        double entityZ = entity.getPosZ();
        double ratioX = 0;
        double ratioZ = 0;
        if (entityX < blockX) {
          ratioX = 1;
        } else if (entityX > blockX + 1) {
          ratioX = -1;
        }
        if (entityZ < blockZ) {
          ratioZ = 1;
        } else if (entityZ > blockZ + 1) {
          ratioZ = -1;
        }
        ((LivingEntity) entity).applyKnockback(0.5F, ratioX, ratioZ);
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean allowsMovement(BlockState state, IBlockReader world, BlockPos pos, PathType type) {
    return true;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createNewTileEntity(IBlockReader world) {
    return new SpikesEffectAreaTileEntity();
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.ENTITYBLOCK_ANIMATED;
  }

  @Override
  public boolean hasGeneratedItemBlock() {
    return false;
  }
}
