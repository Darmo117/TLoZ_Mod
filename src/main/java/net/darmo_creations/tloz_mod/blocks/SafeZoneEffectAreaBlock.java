package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.UpdateFlags;
import net.darmo_creations.tloz_mod.tile_entities.SafeZoneEffectAreaTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Protects any player that touches it against any enemy.
 */
@SuppressWarnings("deprecation")
public class SafeZoneEffectAreaBlock extends Block implements IModBlock, ITileEntityProvider {
  public SafeZoneEffectAreaBlock() {
    super(Properties.create(Material.AIR)
        .notSolid()
        .doesNotBlockMovement()
        .noDrops()
        .setAir()
        .setAllowsSpawn((blockState, blockReader, pos, entityType) -> false));
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
    Block block = world.getBlockState(pos.down()).getBlock();
    return block == ModBlocks.SAFE_ZONE || block == ModBlocks.SAFE_ZONE_EFFECT_AREA;
  }

  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
    if (!this.isValidPosition(state, world, pos)) {
      world.setBlockState(pos, Blocks.AIR.getDefaultState(), UpdateFlags.UPDATE_BLOCK | UpdateFlags.SEND_TO_CLIENT);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
    return VoxelShapes.empty();
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    provideEffect(entity);
  }

  public static void provideEffect(Entity entity) {
    if (entity instanceof PlayerEntity) {
      // TODO custom effect?
      ((PlayerEntity) entity).addPotionEffect(new EffectInstance(Effects.GLOWING, 1));
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  @OnlyIn(Dist.CLIENT)
  public float getAmbientOcclusionLightValue(BlockState state, IBlockReader world, BlockPos pos) {
    return 1;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.INVISIBLE;
  }

  @Override
  @SuppressWarnings("deprecation")
  public PushReaction getPushReaction(BlockState state) {
    return PushReaction.DESTROY;
  }

  @Override
  public boolean hasGeneratedItemBlock() {
    return false;
  }

  @Override
  public TileEntity createNewTileEntity(IBlockReader world) {
    return new SafeZoneEffectAreaTileEntity();
  }
}
