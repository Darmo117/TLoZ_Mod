package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
  public static final DeferredRegister<EntityType<?>> REGISTER =
      DeferredRegister.create(ForgeRegistries.ENTITIES, TLoZ.MODID);

  public static final RegistryObject<EntityType<BombEntity>> BOMB = REGISTER.register(
      "bomb",
      () -> EntityType.Builder.<BombEntity>create(BombEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("bomb")
  );
  public static final RegistryObject<EntityType<JarEntity>> JAR = REGISTER.register(
      "jar",
      () -> EntityType.Builder.<JarEntity>create(JarEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("jar")
  );
  public static final RegistryObject<EntityType<ItemBulbEntity>> ITEM_BULB = REGISTER.register(
      "item_bulb",
      () -> EntityType.Builder.<ItemBulbEntity>create(ItemBulbEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("item_bulb")
  );
  public static final RegistryObject<EntityType<RockEntity>> ROCK = REGISTER.register(
      "rock",
      () -> EntityType.Builder.<RockEntity>create(RockEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("rock")
  );
  public static final RegistryObject<EntityType<BossKeyEntity>> BOSS_KEY = REGISTER.register(
      "boss_key",
      () -> EntityType.Builder.<BossKeyEntity>create(BossKeyEntity::new, EntityClassification.MISC)
          .size(0.5f, 0.5f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("boss_key")
  );
  public static final RegistryObject<EntityType<TLoZArrowEntity>> ARROW = REGISTER.register(
      "arrow",
      () -> EntityType.Builder.<TLoZArrowEntity>create(TLoZArrowEntity::new, EntityClassification.MISC)
          .size(0.5f, 0.5f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("arrow")
  );
  public static final RegistryObject<EntityType<WhirlwindEntity>> WHIRLWIND = REGISTER.register(
      "whirlwind",
      () -> EntityType.Builder.<WhirlwindEntity>create(WhirlwindEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("whirlwind")
  );
  public static final RegistryObject<EntityType<LocomotiveEntity>> LOCOMOTIVE = REGISTER.register(
      "locomotive",
      () -> EntityType.Builder.<LocomotiveEntity>create(LocomotiveEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("locomotive")
  );
  public static final RegistryObject<EntityType<TestMinecartEntity>> TEST_MINECART = REGISTER.register(
      "test_minecart",
      () -> EntityType.Builder.<TestMinecartEntity>create(TestMinecartEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("test_minecart")
  );
}
