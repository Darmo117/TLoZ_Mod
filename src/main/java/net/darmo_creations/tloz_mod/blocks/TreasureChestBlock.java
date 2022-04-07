package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.UpdateFlags;
import net.darmo_creations.tloz_mod.entities.PickableEntity;
import net.darmo_creations.tloz_mod.tile_entities.TreasureChestTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Objects;

/**
 * A chest that spawns an item on top of it when interacted with.
 */
public class TreasureChestBlock extends ContainerBlock implements ExplodableBlock {
  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
  public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
  public static final BooleanProperty MAIN = BooleanProperty.create("main");
  public static final BooleanProperty MIMIC = BooleanProperty.create("mimic");

  protected static final VoxelShape SHAPE_NORTH = Block.makeCuboidShape(1, 0, 0, 15, 15, 10);
  protected static final VoxelShape SHAPE_SOUTH = Block.makeCuboidShape(1, 0, 6, 15, 15, 16);
  protected static final VoxelShape SHAPE_WEST = Block.makeCuboidShape(0, 0, 1, 10, 15, 15);
  protected static final VoxelShape SHAPE_EAST = Block.makeCuboidShape(6, 0, 1, 16, 15, 15);
  protected static final VoxelShape SHAPE_NORTH_OPEN = Block.makeCuboidShape(1, 0, 0, 15, 9, 10);
  protected static final VoxelShape SHAPE_SOUTH_OPEN = Block.makeCuboidShape(1, 0, 6, 15, 9, 16);
  protected static final VoxelShape SHAPE_WEST_OPEN = Block.makeCuboidShape(0, 0, 1, 10, 9, 15);
  protected static final VoxelShape SHAPE_EAST_OPEN = Block.makeCuboidShape(6, 0, 1, 16, 9, 15);

  protected static final VoxelShape SHAPE_SINGLE = Block.makeCuboidShape(1, 0, 1, 15, 15, 15);
  protected static final VoxelShape SHAPE_SINGLE_OPEN = Block.makeCuboidShape(1, 0, 1, 15, 9, 15);

  private final boolean isDouble;

  public TreasureChestBlock(final Type type) {
    super(Properties.create(type == Type.DOUBLE ? Material.ROCK : Material.WOOD)
        .hardnessAndResistance(-1)
        .sound(type == Type.DOUBLE ? SoundType.STONE : SoundType.WOOD));
    Objects.requireNonNull(type);
    this.setDefaultState(this.getStateContainer().getBaseState()
        .with(FACING, Direction.NORTH)
        .with(OPEN, false)
        .with(MAIN, true)
        .with(MIMIC, type == Type.MIMIC));
    this.isDouble = type == Type.DOUBLE;
  }

  public boolean isDouble() {
    return this.isDouble;
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    boolean isOpen = state.get(OPEN);
    if (this.isDouble) {
      switch (this.getDirectionToAttached(state)) {
        case NORTH:
        default:
          return isOpen ? SHAPE_NORTH_OPEN : SHAPE_NORTH;
        case SOUTH:
          return isOpen ? SHAPE_SOUTH_OPEN : SHAPE_SOUTH;
        case WEST:
          return isOpen ? SHAPE_WEST_OPEN : SHAPE_WEST;
        case EAST:
          return isOpen ? SHAPE_EAST_OPEN : SHAPE_EAST;
      }
    } else {
      return isOpen ? SHAPE_SINGLE_OPEN : SHAPE_SINGLE;
    }
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    if (!world.isRemote && this.isDouble) {
      BlockPos blockpos = pos.offset(this.getDirectionToAttached(state));
      world.setBlockState(blockpos, state.with(MAIN, false), UpdateFlags.UPDATE_BLOCK | UpdateFlags.SEND_TO_CLIENT);
    }
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    Direction placementHorizontalFacing = context.getPlacementHorizontalFacing();
    if (this.isDouble) {
      //noinspection deprecation
      if (!context.getWorld().getBlockState(context.getPos().offset(placementHorizontalFacing)).isAir()) {
        return Blocks.AIR.getDefaultState();
      }
    }
    return this.getDefaultState().with(FACING, this.isDouble ? placementHorizontalFacing.rotateY() : placementHorizontalFacing.getOpposite());
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    if (this.isDouble) {
      TreasureChestTileEntity thisTE = this.getTileEntity(world, currentPos);
      TreasureChestTileEntity otherTE = this.getTileEntity(world, currentPos.offset(this.getDirectionToAttached(state)));
      if (thisTE == null || otherTE == null) {
        return Blocks.AIR.getDefaultState();
      } else {
        if (state.get(MAIN)) {
          thisTE.setLinkedInventoryPos(otherTE.getPos());
          otherTE.setLinkedInventoryPos(thisTE.getPos());
        }
      }
    }
    return state;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
    if (state.get(MIMIC)) {
      this.spawnEntity(world, pos);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
    if (state.get(MIMIC)) {
      this.spawnEntity(world, hit.getPos());
    }
  }

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {
    if (world.getBlockState(pos).get(MIMIC)) {
      this.spawnEntity(world, pos);
      if (entity instanceof PickableEntity) {
        ((PickableEntity) entity).die();
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (state.get(MIMIC)) {
      this.spawnEntity(world, pos);
      if (entity instanceof PickableEntity) {
        ((PickableEntity) entity).die();
      }
    }
  }

  @Override
  public void onBombExplosion(World world, BlockPos pos) {
    if (world.getBlockState(pos).get(MIMIC)) {
      this.spawnEntity(world, pos);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (!state.get(OPEN)) {
      if (!world.isRemote) {
        if (state.get(MIMIC)) {
          this.spawnEntity(world, pos);
        } else {
          boolean setLoot = !player.getHeldItem(hand).isEmpty() && player.isCreative();
          TreasureChestTileEntity te = this.getTileEntity(world, pos);
          if (te != null) {
            if (setLoot) {
              te.setLoot(player.getHeldItem(hand));
            } else {
              te.getLoot().ifPresent(loot -> {
                Vector3d offset = getItemSpawnOffset(state, this.isDouble);
                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + offset.x, pos.getY() + offset.y, pos.getZ() + offset.z, loot);
                itemEntity.setMotion(0, 0.2, 0);
                itemEntity.setDefaultPickupDelay();
                world.addEntity(itemEntity);
                te.setLoot(null);
              });
              world.playSound(null, pos, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 1, 1);
            }
          }
          if (!setLoot) {
            world.setBlockState(pos, state.with(OPEN, true),
                UpdateFlags.SEND_TO_CLIENT | UpdateFlags.RERENDER_ON_MAIN_THREAD);
            if (this.isDouble) { // Set adjacent half to open
              BlockPos offset = pos.offset(this.getDirectionToAttached(state));
              world.setBlockState(offset, world.getBlockState(offset).with(OPEN, true),
                  UpdateFlags.SEND_TO_CLIENT | UpdateFlags.RERENDER_ON_MAIN_THREAD);
            }
          }
        }
      }
      return ActionResultType.SUCCESS;
    }
    return ActionResultType.FAIL;
  }

  public static Vector3d getItemSpawnOffset(BlockState state, boolean isDouble) {
    double xOffset = 0;
    double zOffset = 0;

    if (isDouble) {
      // Center item entity if double chest
      switch (state.get(FACING)) {
        case NORTH:
          xOffset = 0.5;
          break;
        case SOUTH:
          xOffset = -0.5;
          break;
        case WEST:
          zOffset = -0.5;
          break;
        case EAST:
          zOffset = 0.5;
          break;
      }
      if (state.get(MAIN)) {
        xOffset = -xOffset;
        zOffset = -zOffset;
      }
    }

    return new Vector3d(xOffset + 0.5, 1, zOffset + 0.5);
  }

  public Direction getDirectionToAttached(BlockState state) {
    Direction direction = state.get(FACING);
    return state.get(MAIN) ? direction.rotateYCCW() : direction.rotateY();
  }

  private void spawnEntity(World world, BlockPos pos) {
    if (!world.isRemote) {
      // TODO spawn entity
      world.setBlockState(pos, Blocks.AIR.getDefaultState(), UpdateFlags.UPDATE_BLOCK | UpdateFlags.SEND_TO_CLIENT);
      world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.BLOCKS, 1, 1);
    }
  }

  private TreasureChestTileEntity getTileEntity(IWorld world, BlockPos pos) {
    TileEntity te = world.getTileEntity(pos);
    return te instanceof TreasureChestTileEntity ? (TreasureChestTileEntity) te : null;
  }

  @Override
  public TileEntity createNewTileEntity(IBlockReader world) {
    return new TreasureChestTileEntity();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FACING, OPEN, MAIN, MIMIC);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }

  public enum Type {
    SIMPLE,
    DOUBLE,
    MIMIC
  }
}
