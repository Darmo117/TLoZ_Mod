package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.ItemBulbEntity;
import net.darmo_creations.tloz_mod.entities.PickableEntity;
import net.darmo_creations.tloz_mod.tile_entities.ItemBulbFlowerTileEntity;
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
 * A plant-like block that grows acorn-shaped bulbs containing items.
 *
 * @see ItemBulbFlowerTileEntity
 * @see ItemBulbEntity
 */
public class ItemBulbFlowerBlock extends PickableBlock<ItemBulbFlowerTileEntity> {
  /**
   * Speed above which a bulb should break when hitting this block.
   */
  public static final float BREAKING_SPEED_THRESHOLD = 0.05f;

  public ItemBulbFlowerBlock() {
    super(Properties.create(Material.PLANTS).sound(SoundType.PLANT), ItemBulbFlowerTileEntity.class);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    TileEntity te = world.getTileEntity(pos);
    if (te instanceof ItemBulbFlowerTileEntity) {
      ItemBulbFlowerTileEntity t = (ItemBulbFlowerTileEntity) te;
      VoxelShape baseShape = makeCuboidShape(0, 0, 0, 16, 1, 16);
      if (!t.hasBulb() && t.getGrowthStage() == 0) {
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
        // Prevent bulb from this block from breaking when spawning
        && (!(entity instanceof ItemBulbEntity) || entity.getMotion().length() > BREAKING_SPEED_THRESHOLD)) {
      ((PickableEntity) entity).die();
    }
  }

  @Override
  protected InteractionResult onInteraction(ItemBulbFlowerTileEntity tileEntity, World world, BlockPos pos, InteractionContext interactionContext) {
    switch (interactionContext.interactionType) {
      case PLAYER_INTERACT:
        return this.spawnBulb(interactionContext.player, tileEntity, false);
      case ENTITY_COLLISION:
        if (interactionContext.entity instanceof PickableEntity) {
          return this.spawnBulb(null, tileEntity, true);
        }
        break;
      case PLAYER_HIT:
      case PROJECTILE_COLLISION:
      case BOMB_EXPLOSION:
        return this.spawnBulb(null, tileEntity, true);
    }
    return InteractionResult.FAIL;
  }

  private InteractionResult spawnBulb(PlayerEntity player, ItemBulbFlowerTileEntity tileEntity, boolean breakInstantly) {
    return tileEntity.popBulb(player, breakInstantly) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }
}
