package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.tile_entities.KillTriggerTileEntity;
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
 * A block that kills any player that collides with it.
 */
@SuppressWarnings("deprecation")
public class KillTriggerBlock extends Block implements ITileEntityProvider {
  public KillTriggerBlock() {
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
      ServerPlayerEntity player = (ServerPlayerEntity) entity;
      if (!player.isCreative() && !player.isSpectator()) {
        player.onKillCommand();
      }
    }
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createNewTileEntity(IBlockReader world) {
    return new KillTriggerTileEntity();
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.INVISIBLE;
  }
}
