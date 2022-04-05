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
  public static final RegistryObject<TileEntityType<ItemBulbFlowerTileEntity>> ITEM_BULB_FLOWER = REGISTER.register(
      "item_bulb_flower_tile_entity",
      () -> TileEntityType.Builder.create(ItemBulbFlowerTileEntity::new, ModBlocks.ITEM_BULB_FLOWER).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<JarTileEntity>> JAR = REGISTER.register(
      "jar_tile_entity",
      () -> TileEntityType.Builder.create(JarTileEntity::new, ModBlocks.JAR).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<RockTileEntity>> ROCK = REGISTER.register(
      "rock_tile_entity",
      () -> TileEntityType.Builder.create(RockTileEntity::new, ModBlocks.ROCK).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<BombBreakableBlockTileEntity>> BOMB_BREAKABLE_BLOCK = REGISTER.register(
      "bomb_breakable_block_tile_entity",
      () -> TileEntityType.Builder.create(BombBreakableBlockTileEntity::new, ModBlocks.BOMB_BREAKABLE_BLOCK).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<SafeZoneEffectAreaTileEntity>> SAFE_ZONE_EFFECT_AREA = REGISTER.register(
      "safe_zone_effect_area_tile_entity",
      () -> TileEntityType.Builder.create(SafeZoneEffectAreaTileEntity::new, ModBlocks.SAFE_ZONE_EFFECT_AREA).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<SpikesEffectAreaTileEntity>> SPIKES_EFFECT_AREA = REGISTER.register(
      "spikes_effect_area_tile_entity",
      () -> TileEntityType.Builder.create(SpikesEffectAreaTileEntity::new, ModBlocks.SPIKES_EFFECT_AREA).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<SpawnpointSetterTileEntity>> SPAWNPOINT_SETTER = REGISTER.register(
      "spawnpoint_setter_tile_entity",
      () -> TileEntityType.Builder.create(SpawnpointSetterTileEntity::new, ModBlocks.SPAWNPOINT_SETTER).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<TreasureChestTileEntity>> TREASURE_CHEST = REGISTER.register(
      "treasure_chest_tile_entity",
      () -> TileEntityType.Builder.create(TreasureChestTileEntity::new, ModBlocks.TREASURE_CHEST, ModBlocks.DOUBLE_TREASURE_CHEST, ModBlocks.MIMIC_CHEST)
          .build(null)
  );
}
