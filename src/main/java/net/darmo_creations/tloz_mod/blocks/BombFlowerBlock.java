package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.darmo_creations.tloz_mod.entities.PickableEntity;
import net.darmo_creations.tloz_mod.tile_entities.BombFlowerTileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * A plant-like block that grows bombs that can be picked up by players.
 * <p>
 * Conditions for the bomb to explode:
 * <li>Attacked by an entity
 * <li>Hit by a projectile
 * <li>Collides with another bomb
 * <p>
 * Condition for the bomb to pop out:
 * <li>Right-clicked by player
 *
 * @see BombFlowerTileEntity
 * @see BombEntity
 */
public class BombFlowerBlock extends PickableBlock<BombFlowerTileEntity> {
  /**
   * Speed above which a bomb should explode when hitting this block.
   */
  public static final float EXPLOSION_SPEED_THRESHOLD = 0.05f;

  public BombFlowerBlock() {
    super(Properties.create(Material.PLANTS).sound(SoundType.PLANT), BombFlowerTileEntity.class);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    TileEntity te = world.getTileEntity(pos);
    if (te instanceof BombFlowerTileEntity) {
      BombFlowerTileEntity t = (BombFlowerTileEntity) te;
      VoxelShape baseShape = makeCuboidShape(0, 0, 0, 16, 1, 16);
      if (!t.hasBomb() && t.getGrowthStage() == 0) {
        return baseShape;
      }
      float stage = 7 * t.getGrowthStage();
      return VoxelShapes.or(
          baseShape,
          makeCuboidShape(8 - stage, 1, 8 - stage, 8 + stage, 1 + 14 * t.getGrowthStage(), 8 + stage)
      );
    } else {
      return VoxelShapes.fullCube();
    }
  }

  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    this.onInteraction(world, pos, InteractionContext.entityCollision(entity));
    if (entity instanceof PickableEntity
        // Prevent bomb from this block to explode when spawning
        && (!(entity instanceof BombEntity) || entity.getMotion().length() > EXPLOSION_SPEED_THRESHOLD)) {
      ((PickableEntity) entity).die();
    }
  }

  @Override
  protected InteractionResult onInteraction(BombFlowerTileEntity tileEntity, World world, BlockPos pos, InteractionContext interactionContext) {
    switch (interactionContext.interactionType) {
      case PLAYER_INTERACT:
        return this.spawnBomb(interactionContext.player, tileEntity, BombFlowerTileEntity.FUSE_DELAY, false);
      case ENTITY_COLLISION:
        if (interactionContext.entity instanceof PickableEntity) {
          return this.spawnBomb(null, tileEntity, 0, true);
        }
        break;
      case PLAYER_HIT:
      case PROJECTILE_COLLISION:
        return this.spawnBomb(null, tileEntity, 0, false);
      case BOMB_EXPLOSION:
        return this.spawnBomb(null, tileEntity, 3, true); // Small delay to mimic in-game behavior
    }
    return InteractionResult.FAIL;
  }

  private InteractionResult spawnBomb(PlayerEntity player, BombFlowerTileEntity tileEntity, int fuse, boolean invulnerable) {
    return tileEntity.popBomb(player, fuse, invulnerable) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }
}
