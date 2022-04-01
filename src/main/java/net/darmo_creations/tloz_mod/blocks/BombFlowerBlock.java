package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.entities.BombEntity;
import net.darmo_creations.tloz_mod.tile_entities.BombFlowerTileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
public class BombFlowerBlock extends ContainerBlock implements IModBlock {
  public BombFlowerBlock() {
    super(Properties.create(Material.PLANTS)
        .sound(SoundType.PLANT)
        .hardnessAndResistance(-1)
        .notSolid()
        .setAllowsSpawn((blockState, blockReader, pos, entityType) -> false)
        .setOpaque((blockState, blockReader, pos) -> false)
        .setSuffocates((blockState, blockReader, pos) -> false)
        .setBlocksVision((blockState, blockReader, pos) -> false));
  }

  @SuppressWarnings("deprecation")
  @Override
  @OnlyIn(Dist.CLIENT)
  public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 1;
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

  // Pop out bomb
  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (!world.isRemote) {
      TileEntity te = world.getTileEntity(pos);
      if (te instanceof BombFlowerTileEntity) {
        BombFlowerTileEntity t = (BombFlowerTileEntity) te;
        if (t.hasBomb()) {
          return t.popBomb(player, world, BombFlowerTileEntity.FUSE_DELAY) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
        }
      }
      return ActionResultType.FAIL;
    } else {
      return ActionResultType.CONSUME;
    }
  }

  // Player attacked -> explode bomb
  @SuppressWarnings("deprecation")
  @Override
  public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
    if (!world.isRemote) {
      this.explodeBomb(world, pos);
    }
  }

  // Collision with bomb entity -> explode bomb
  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (!world.isRemote && entity instanceof BombEntity) {
      this.explodeBomb(world, pos);
    }
  }

  // Projectile hit -> explode bomb
  @SuppressWarnings("deprecation")
  @Override
  public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
    if (!world.isRemote) {
      this.explodeBomb(world, hit.getPos());
    }
  }

  private void explodeBomb(World world, BlockPos pos) {
    TileEntity te = world.getTileEntity(pos);
    if (te instanceof BombFlowerTileEntity) {
      BombFlowerTileEntity t = (BombFlowerTileEntity) te;
      if (t.hasBomb()) {
        t.popBomb(null, world, 0);
      }
    }
  }

  @Override
  public TileEntity createNewTileEntity(IBlockReader world) {
    return new BombFlowerTileEntity();
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }
}
