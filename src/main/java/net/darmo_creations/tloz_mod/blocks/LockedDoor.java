package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.UpdateFlags;
import net.darmo_creations.tloz_mod.items.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

public class LockedDoor extends Block {
  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
  public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
  public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
  public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
  protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0, 0, 0, 16, 16, 3);
  protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0, 0, 13, 16, 16, 16);
  protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(13, 0, 0, 16, 16, 16);
  protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(0, 0, 0, 3, 16, 16);

  public LockedDoor() {
    super(Properties.create(Material.WOOD)
        .hardnessAndResistance(-1)
        .sound(SoundType.WOOD)
        .notSolid());
    this.setDefaultState(this.stateContainer.getBaseState()
        .with(FACING, Direction.NORTH)
        .with(OPEN, false)
        .with(HINGE, DoorHingeSide.LEFT)
        .with(HALF, DoubleBlockHalf.LOWER));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    Direction direction = state.get(FACING);
    boolean flag = !state.get(OPEN);
    boolean flag1 = state.get(HINGE) == DoorHingeSide.RIGHT;
    switch (direction) {
      case EAST:
      default:
        return flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);
      case SOUTH:
        return flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);
      case WEST:
        return flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);
      case NORTH:
        return flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
    }
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    BlockPos blockpos = context.getPos();
    if (blockpos.getY() < 255 && context.getWorld().getBlockState(blockpos.up()).isReplaceable(context)) {
      return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing())
          .with(HINGE, this.getHingeSide(context))
          .with(HALF, DoubleBlockHalf.LOWER);
    } else {
      return null;
    }
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER), UpdateFlags.UPDATE_BLOCK | UpdateFlags.SEND_TO_CLIENT);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    DoubleBlockHalf doubleblockhalf = state.get(HALF);
    if (facing.getAxis() == Direction.Axis.Y && doubleblockhalf == DoubleBlockHalf.LOWER == (facing == Direction.UP)) {
      return facingState.matchesBlock(this) && facingState.get(HALF) != doubleblockhalf
          ? state.with(FACING, facingState.get(FACING)).with(OPEN, facingState.get(OPEN)).with(HINGE, facingState.get(HINGE))
          : Blocks.AIR.getDefaultState();
    } else {
      return doubleblockhalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.isValidPosition(world, currentPos)
          ? Blocks.AIR.getDefaultState()
          : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
    }
  }

  @Override
  public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    if (!world.isRemote && player.isCreative()) {
      removeBottomHalf(world, pos, state, player);
    }
    super.onBlockHarvested(world, pos, state, player);
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    ItemStack heldItem = player.getHeldItem(hand);
    boolean isOpen = state.get(OPEN);
    if (heldItem.getItem() != ModItems.SMALL_KEY || isOpen) {
      if (!isOpen) {
        world.playSound(null, pos, SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, 1, 1);
        player.sendStatusMessage(new TranslationTextComponent("block.tloz.locked_door.locked_message"), true);
      }
      return ActionResultType.PASS;
    }
    world.setBlockState(pos, state.with(OPEN, true), UpdateFlags.SEND_TO_CLIENT | UpdateFlags.RERENDER_ON_MAIN_THREAD);
    if (!player.isCreative()) {
      heldItem.setCount(heldItem.getCount() - 1);
    }
    world.playSound(null, pos, SoundEvents.BLOCK_WOODEN_DOOR_OPEN, SoundCategory.BLOCKS, 1, 1);
    Optional<BlockPos> neighborPos = this.getNeighborDoor(state, world, pos);
    neighborPos.ifPresent(p -> world.setBlockState(p, world.getBlockState(p).with(OPEN, true),
        UpdateFlags.SEND_TO_CLIENT | UpdateFlags.RERENDER_ON_MAIN_THREAD));
    return ActionResultType.SUCCESS;
  }

  private Optional<BlockPos> getNeighborDoor(BlockState thisState, World world, BlockPos thisPos) {
    DoorHingeSide thisHingeSide = thisState.get(HINGE);
    Direction thisFacing = thisState.get(FACING);
    BlockPos otherPos = null;
    switch (thisFacing) {
      case NORTH:
        if (thisHingeSide == DoorHingeSide.LEFT) {
          otherPos = thisPos.east();
        } else {
          otherPos = thisPos.west();
        }
        break;
      case SOUTH:
        if (thisHingeSide == DoorHingeSide.LEFT) {
          otherPos = thisPos.west();
        } else {
          otherPos = thisPos.east();
        }
        break;
      case WEST:
        if (thisHingeSide == DoorHingeSide.LEFT) {
          otherPos = thisPos.north();
        } else {
          otherPos = thisPos.south();
        }
        break;
      case EAST:
        if (thisHingeSide == DoorHingeSide.LEFT) {
          otherPos = thisPos.south();
        } else {
          otherPos = thisPos.north();
        }
        break;
    }
    if (otherPos != null) {
      BlockState otherState = world.getBlockState(otherPos);
      if (otherState.getBlock() == this && otherState.get(FACING) == thisFacing && otherState.get(HINGE) != thisHingeSide) {
        return Optional.of(otherPos);
      }
    }
    return Optional.empty();
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean allowsMovement(BlockState state, IBlockReader world, BlockPos pos, PathType type) {
    switch (type) {
      case LAND:
      case AIR:
        return state.get(OPEN);
      default:
        return false;
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
    BlockPos blockpos = pos.down();
    BlockState blockstate = world.getBlockState(blockpos);
    return state.get(HALF) == DoubleBlockHalf.LOWER ? blockstate.isSolidSide(world, blockpos, Direction.UP) : blockstate.matchesBlock(this);
  }

  @SuppressWarnings("deprecation")
  @Override
  public PushReaction getPushReaction(BlockState state) {
    return PushReaction.DESTROY;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, Rotation rot) {
    return state.with(FACING, rot.rotate(state.get(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return mirror == Mirror.NONE ? state : state.rotate(mirror.toRotation(state.get(FACING))).cycleValue(HINGE);
  }

  @SuppressWarnings("deprecation")
  @Override
  @OnlyIn(Dist.CLIENT)
  public long getPositionRandom(BlockState state, BlockPos pos) {
    return MathHelper.getCoordinateRandom(pos.getX(), pos.down(state.get(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(HALF, FACING, OPEN, HINGE);
  }

  private DoorHingeSide getHingeSide(BlockItemUseContext context) {
    IBlockReader iblockreader = context.getWorld();
    BlockPos blockpos = context.getPos();
    Direction direction = context.getPlacementHorizontalFacing();
    BlockPos blockpos1 = blockpos.up();
    Direction direction1 = direction.rotateYCCW();
    BlockPos blockpos2 = blockpos.offset(direction1);
    BlockState blockstate = iblockreader.getBlockState(blockpos2);
    BlockPos blockpos3 = blockpos1.offset(direction1);
    BlockState blockstate1 = iblockreader.getBlockState(blockpos3);
    Direction direction2 = direction.rotateY();
    BlockPos blockpos4 = blockpos.offset(direction2);
    BlockState blockstate2 = iblockreader.getBlockState(blockpos4);
    BlockPos blockpos5 = blockpos1.offset(direction2);
    BlockState blockstate3 = iblockreader.getBlockState(blockpos5);
    int i = (blockstate.hasOpaqueCollisionShape(iblockreader, blockpos2) ? -1 : 0) + (blockstate1.hasOpaqueCollisionShape(iblockreader, blockpos3) ? -1 : 0) + (blockstate2.hasOpaqueCollisionShape(iblockreader, blockpos4) ? 1 : 0) + (blockstate3.hasOpaqueCollisionShape(iblockreader, blockpos5) ? 1 : 0);
    boolean flag = blockstate.matchesBlock(this) && blockstate.get(HALF) == DoubleBlockHalf.LOWER;
    boolean flag1 = blockstate2.matchesBlock(this) && blockstate2.get(HALF) == DoubleBlockHalf.LOWER;
    if ((!flag || flag1) && i <= 0) {
      if ((!flag1 || flag) && i >= 0) {
        int j = direction.getXOffset();
        int k = direction.getZOffset();
        Vector3d vector3d = context.getHitVec();
        double d0 = vector3d.x - (double) blockpos.getX();
        double d1 = vector3d.z - (double) blockpos.getZ();
        return (j >= 0 || !(d1 < 0.5D)) && (j <= 0 || !(d1 > 0.5D)) && (k >= 0 || !(d0 > 0.5D)) && (k <= 0 || !(d0 < 0.5D)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
      } else {
        return DoorHingeSide.LEFT;
      }
    } else {
      return DoorHingeSide.RIGHT;
    }
  }

  protected static void removeBottomHalf(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    DoubleBlockHalf doubleblockhalf = state.get(HALF);
    if (doubleblockhalf == DoubleBlockHalf.UPPER) {
      BlockPos blockpos = pos.down();
      BlockState blockstate = world.getBlockState(blockpos);
      if (blockstate.getBlock() == state.getBlock() && blockstate.get(HALF) == DoubleBlockHalf.LOWER) {
        world.setBlockState(blockpos, Blocks.AIR.getDefaultState(),
            UpdateFlags.UPDATE_BLOCK | UpdateFlags.SEND_TO_CLIENT | UpdateFlags.PREVENT_NEIGHBOR_DROPS);
        world.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
      }
    }
  }
}
