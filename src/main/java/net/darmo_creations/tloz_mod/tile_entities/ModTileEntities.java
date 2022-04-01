package net.darmo_creations.tloz_mod.tile_entities;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
  public static final DeferredRegister<TileEntityType<?>> REGISTER =
      DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TLoZ.MODID);

  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<BombFlowerTileEntity>> BOMB_FLOWER = REGISTER.register(
      "bomb_plant_tile_entity",
      () -> TileEntityType.Builder.create(BombFlowerTileEntity::new, ModBlocks.BOMB_FLOWER).build(null)
  );

  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<BombBreakableBlockTileEntity>> BOMB_BREAKABLE_BLOCK = REGISTER.register(
      "bomb_breakable_block_tile_entity",
      () -> TileEntityType.Builder.create(BombBreakableBlockTileEntity::new, ModBlocks.BOMB_BREAKABLE_BLOCK).build(null)
  );
}
