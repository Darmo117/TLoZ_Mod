package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.tile_entities.BombBreakableBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
public class BombBreakableBlock extends Block implements IModBlock, ITileEntityProvider {
  public BombBreakableBlock() {
    super(Properties.create(Material.ROCK)
        .hardnessAndResistance(-1, 1));
  }

  @SuppressWarnings("deprecation")
  @Override
  @OnlyIn(Dist.CLIENT)
  public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return 0.2f;
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    // Non-full shape to "fix" light problems while rendering
    return VoxelShapes.create(0, 0, 0, 1, 0.999, 1);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
    // Play different sound when hit
    world.playSound(player, pos, SoundEvents.BLOCK_BONE_BLOCK_HIT, SoundCategory.BLOCKS, 1, 1);
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    if (player.isCreative()) {
      if (!world.isRemote) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof BombBreakableBlockTileEntity) {
          BombBreakableBlockTileEntity t = (BombBreakableBlockTileEntity) te;
          Item item = player.getHeldItem(hand).getItem();
          if (item instanceof BlockItem && item != Items.AIR) {
            t.setBlock(((BlockItem) item).getBlock());
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_DISPENSER_DISPENSE, SoundCategory.BLOCKS, 1, 1);
            return ActionResultType.SUCCESS;
          } else if (item == Items.WOODEN_HOE) {
            t.setBlock(null);
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1, 1);
            return ActionResultType.SUCCESS;
          }
        }
      }
      return ActionResultType.CONSUME;
    }
    return ActionResultType.FAIL;
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }

  @Override
  public TileEntity createNewTileEntity(IBlockReader worldIn) {
    return new BombBreakableBlockTileEntity();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }
}
