package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.tile_entities.OrbSwitchTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class OrbSwitchBlock extends ContainerBlock {
  public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

  public OrbSwitchBlock() {
    super(Properties.create(Material.GLASS).sound(SoundType.GLASS));
    this.setDefaultState(this.getStateContainer().getBaseState().with(ACTIVATED, false));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
    super.onBlockClicked(state, world, pos, player);
  }

  @Override
  public TileEntity createNewTileEntity(IBlockReader world) {
    return new OrbSwitchTileEntity();
  }
}
