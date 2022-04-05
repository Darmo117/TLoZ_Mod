package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.tile_entities.SpawnpointSetterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

/**
 * A block that sets the spawnpoint of any player that collides with it.
 * The spawnpoint is set to the position of the collided block.
 */
@SuppressWarnings("deprecation")
public class SpawnpointSetterBlock extends Block implements ITileEntityProvider {
  public SpawnpointSetterBlock() {
    super(Properties.create(Material.AIR)
        .setAir()
        .doesNotBlockMovement()
        .noDrops());
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.empty();
  }

  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    if (entity instanceof ServerPlayerEntity) {
      // Set the spawn point of the player if it is not already within 2 blocks
      ServerPlayerEntity player = (ServerPlayerEntity) entity;
      BlockPos currentSpawnPoint = player.func_241140_K_();
      if (currentSpawnPoint == null || !pos.withinDistance(currentSpawnPoint, 2)) {
        player.func_242111_a(world.getDimensionKey(), pos, player.rotationYaw, true, false);
      }
    }
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createNewTileEntity(IBlockReader world) {
    return new SpawnpointSetterTileEntity();
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.INVISIBLE;
  }
}
