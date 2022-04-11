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
      "bomb_plant",
      () -> TileEntityType.Builder.create(BombFlowerTileEntity::new, ModBlocks.BOMB_FLOWER).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<ItemBulbFlowerTileEntity>> ITEM_BULB_FLOWER = REGISTER.register(
      "item_bulb_flower",
      () -> TileEntityType.Builder.create(ItemBulbFlowerTileEntity::new, ModBlocks.ITEM_BULB_FLOWER).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<JarTileEntity>> JAR = REGISTER.register(
      "jar",
      () -> TileEntityType.Builder.create(JarTileEntity::new, ModBlocks.JAR).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<RockTileEntity>> ROCK = REGISTER.register(
      "rock",
      () -> TileEntityType.Builder.create(RockTileEntity::new, ModBlocks.ROCK).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<BossKeyTileEntity>> BOSS_KEY = REGISTER.register(
      "boss_key",
      () -> TileEntityType.Builder.create(BossKeyTileEntity::new, ModBlocks.BOSS_KEY).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<BombBreakableBlockTileEntity>> BOMB_BREAKABLE_BLOCK = REGISTER.register(
      "bomb_breakable_block",
      () -> TileEntityType.Builder.create(BombBreakableBlockTileEntity::new, ModBlocks.BOMB_BREAKABLE_BLOCK).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<SafeZoneEffectAreaTileEntity>> SAFE_ZONE_EFFECT_AREA = REGISTER.register(
      "safe_zone_effect_area",
      () -> TileEntityType.Builder.create(SafeZoneEffectAreaTileEntity::new, ModBlocks.SAFE_ZONE_EFFECT_AREA).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<SpikesEffectAreaTileEntity>> SPIKES_EFFECT_AREA = REGISTER.register(
      "spikes_effect_area",
      () -> TileEntityType.Builder.create(SpikesEffectAreaTileEntity::new, ModBlocks.SPIKES_EFFECT_AREA).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<SpawnpointSetterTileEntity>> SPAWNPOINT_SETTER = REGISTER.register(
      "spawnpoint_setter",
      () -> TileEntityType.Builder.create(SpawnpointSetterTileEntity::new, ModBlocks.SPAWNPOINT_SETTER).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<KillTriggerTileEntity>> KILL_TRIGGER = REGISTER.register(
      "kill_trigger",
      () -> TileEntityType.Builder.create(KillTriggerTileEntity::new, ModBlocks.KILL_TRIGGER).build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<TreasureChestTileEntity>> TREASURE_CHEST = REGISTER.register(
      "treasure_chest",
      () -> TileEntityType.Builder.create(TreasureChestTileEntity::new, ModBlocks.TREASURE_CHEST, ModBlocks.DOUBLE_TREASURE_CHEST, ModBlocks.MIMIC_CHEST)
          .build(null)
  );
  @SuppressWarnings("ConstantConditions")
  public static final RegistryObject<TileEntityType<BlueLightTeleporterTileEntity>> BLUE_LIGHT_TELEPORTER = REGISTER.register(
      "blue_light_teleporter",
      () -> TileEntityType.Builder.create(BlueLightTeleporterTileEntity::new, ModBlocks.BLUE_LIGHT_TELEPORTER).build(null)
  );
}
