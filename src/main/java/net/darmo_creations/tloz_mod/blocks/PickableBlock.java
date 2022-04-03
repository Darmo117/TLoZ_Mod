package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.PickableEntity;
import net.darmo_creations.tloz_mod.tile_entities.PickableTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Random;

/**
 * A block that spawns an entity that can be picked up by the player.
 *
 * @param <T> Type of associated tile entity.
 */
public abstract class PickableBlock<T extends PickableTileEntity> extends ContainerBlock implements ExplodableBlock {
  private final Class<T> tileEntityClass;

  public PickableBlock(Properties properties, Class<T> tileEntityClass) {
    super(properties
        .hardnessAndResistance(-1)
        .notSolid()
        .setAllowsSpawn((blockState, blockReader, pos, entityType) -> false));
    this.tileEntityClass = tileEntityClass;
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
    return facing == Direction.DOWN && !state.isValidPosition(world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
    BlockPos blockpos = pos.down();
    return hasSolidSideOnTop(world, blockpos) || hasEnoughSolidSide(world, blockpos, Direction.UP);
  }

  @SuppressWarnings("deprecation")
  @Override
  @OnlyIn(Dist.CLIENT)
  public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 1;
  }

  // Spawn entity
  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (!world.isRemote) {
      TileEntity te = world.getTileEntity(pos);
      if (te != null && this.tileEntityClass.isAssignableFrom(te.getClass())) {
        //noinspection unchecked
        switch (this.onInteraction((T) te, world, pos, InteractionContext.playerInteraction(player))) {
          case SUCCESS:
            return ActionResultType.SUCCESS;
          case FAIL:
            return ActionResultType.FAIL;
          case BREAK_BLOCK:
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            return ActionResultType.SUCCESS;
        }
      }
      return ActionResultType.FAIL;
    } else {
      return ActionResultType.CONSUME;
    }
  }

  // Player attacked -> break
  @SuppressWarnings("deprecation")
  @Override
  public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
    this.onInteraction(world, pos, InteractionContext.playerHit(player));
  }

  // Collision with pickable entity -> break
  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity) {
    this.onInteraction(world, pos, InteractionContext.entityCollision(entity));
    if (entity instanceof PickableEntity) {
      ((PickableEntity) entity).die();
    }
  }

  // Collision with pickable entity -> break
  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    this.onInteraction(world, pos, InteractionContext.entityCollision(entity));
    if (entity instanceof PickableEntity) {
      ((PickableEntity) entity).die();
    }
  }

  // Projectile hit -> break
  @SuppressWarnings("deprecation")
  @Override
  public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
    this.onInteraction(world, hit.getPos(), InteractionContext.projectileCollision(projectile));
    projectile.remove();
  }

  // Bomb exploded -> break
  @Override
  public void onBombExplosion(World world, BlockPos pos) {
    this.onInteraction(world, pos, InteractionContext.bombExplosion());
  }

  /**
   * Called whenever any entity interacts with this block.
   *
   * @param world              World the block is in.
   * @param pos                Position of the block.
   * @param interactionContext An object that holds the interaction data.
   */
  protected void onInteraction(World world, BlockPos pos, InteractionContext interactionContext) {
    TileEntity te = world.getTileEntity(pos);
    if (!world.isRemote && te != null && this.tileEntityClass.isAssignableFrom(te.getClass())) {
      //noinspection unchecked
      if (this.onInteraction((T) te, world, pos, interactionContext) == InteractionResult.BREAK_BLOCK) {
        // Delay block destruction to allow sound to play correctly
        world.getPendingBlockTicks().scheduleTick(pos, this, 1);
      }
    }
  }

  /**
   * Called when any entity interacts with this block.
   *
   * @param world              World the block is in.
   * @param pos                Position of the block.
   * @param interactionContext An object that holds the interaction data.
   * @return The result of the action.
   */
  protected abstract InteractionResult onInteraction(T tileEntity, World world, BlockPos pos, InteractionContext interactionContext);

  @SuppressWarnings("deprecation")
  @Override
  public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
  }

  @Override
  public TileEntity createNewTileEntity(IBlockReader world) {
    try {
      return this.tileEntityClass.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      throw new RuntimeException("Missing default constructor for tile entity class!", e);
    }
  }

  /**
   * List of ways this block can be interacted with.
   */
  public enum InteractionType {
    /**
     * A player right-clicked this block.
     */
    PLAYER_INTERACT,
    /**
     * A player attacked this block.
     */
    PLAYER_HIT,
    /**
     * An entity collided with this block.
     */
    ENTITY_COLLISION,
    /**
     * A projectile hit this block.
     */
    PROJECTILE_COLLISION,
    /**
     * A bomb exploded near this block.
     */
    BOMB_EXPLOSION
  }

  /**
   * List of possible interaction results.
   */
  public enum InteractionResult {
    /**
     * The action succeeded.
     */
    SUCCESS,
    /**
     * The action failed, cancel any further actions.
     */
    FAIL,
    /**
     * The action succeeded, break the block.
     */
    BREAK_BLOCK
  }

  /**
   * A class that holds data related to an interaction event on a block.
   */
  public static class InteractionContext {
    /**
     * Type of block interaction.
     */
    public final InteractionType interactionType;
    /**
     * Player that interacted with the block. Null if {@link #interactionType} is not {@link InteractionType#PLAYER_HIT}
     * or {@link InteractionType#PLAYER_INTERACT}.
     */
    public final PlayerEntity player;
    /**
     * Entity that collided with the block. Null if {@link #interactionType} is not {@link InteractionType#ENTITY_COLLISION}.
     */
    public final Entity entity;
    /**
     * Projectile that hit the block. Null if {@link #interactionType} is not {@link InteractionType#PROJECTILE_COLLISION}.
     */
    public final ProjectileEntity projectile;

    private InteractionContext(final InteractionType interactionType, final PlayerEntity player, final Entity entity, final ProjectileEntity projectile) {
      this.interactionType = Objects.requireNonNull(interactionType);
      this.player = player;
      this.entity = entity;
      this.projectile = projectile;
    }

    public static InteractionContext playerInteraction(final PlayerEntity player) {
      return new InteractionContext(InteractionType.PLAYER_INTERACT, player, null, null);
    }

    public static InteractionContext playerHit(final PlayerEntity player) {
      return new InteractionContext(InteractionType.PLAYER_HIT, player, null, null);
    }

    public static InteractionContext entityCollision(final Entity entity) {
      return new InteractionContext(InteractionType.ENTITY_COLLISION, null, entity, null);
    }

    public static InteractionContext projectileCollision(final ProjectileEntity projectile) {
      return new InteractionContext(InteractionType.PROJECTILE_COLLISION, null, null, projectile);
    }

    public static InteractionContext bombExplosion() {
      return new InteractionContext(InteractionType.BOMB_EXPLOSION, null, null, null);
    }
  }
}
