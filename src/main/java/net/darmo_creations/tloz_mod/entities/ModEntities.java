package net.darmo_creations.tloz_mod.entities;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.entities.trains.FreightCarEntity;
import net.darmo_creations.tloz_mod.entities.trains.RailCannonEntity;
import net.darmo_creations.tloz_mod.entities.trains.TrainCoachEntity;
import net.darmo_creations.tloz_mod.entities.trains.TrainEngineEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
  public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, TLoZ.MODID);

  public static final RegistryObject<EntityType<BombEntity>> BOMB = register(
      "bomb",
      EntityType.Builder.<BombEntity>create(BombEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
  );
  public static final RegistryObject<EntityType<JarEntity>> JAR = register(
      "jar",
      EntityType.Builder.<JarEntity>create(JarEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
  );
  public static final RegistryObject<EntityType<ItemBulbEntity>> ITEM_BULB = register(
      "item_bulb",
      EntityType.Builder.<ItemBulbEntity>create(ItemBulbEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
  );
  public static final RegistryObject<EntityType<RockEntity>> ROCK = register(
      "rock",
      EntityType.Builder.<RockEntity>create(RockEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
  );
  public static final RegistryObject<EntityType<BossKeyEntity>> BOSS_KEY = register(
      "boss_key",
      EntityType.Builder.<BossKeyEntity>create(BossKeyEntity::new, EntityClassification.MISC)
          .size(0.5f, 0.5f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
  );
  public static final RegistryObject<EntityType<TLoZArrowEntity>> ARROW = register(
      "arrow",
      EntityType.Builder.<TLoZArrowEntity>create(TLoZArrowEntity::new, EntityClassification.MISC)
          .size(0.5f, 0.5f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
  );
  public static final RegistryObject<EntityType<WhirlwindEntity>> WHIRLWIND = register(
      "whirlwind",
      EntityType.Builder.<WhirlwindEntity>create(WhirlwindEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
  );
  public static final RegistryObject<EntityType<TrainEngineEntity>> TRAIN_ENGINE = register(
      "train_engine",
      EntityType.Builder.<TrainEngineEntity>create(TrainEngineEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(8)
          .updateInterval(3)
          .immuneToFire()
  );
  public static final RegistryObject<EntityType<RailCannonEntity>> RAIL_CANNON = register(
      "rail_cannon",
      EntityType.Builder.<RailCannonEntity>create(RailCannonEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(8)
          .updateInterval(3)
          .immuneToFire()
  );
  public static final RegistryObject<EntityType<TrainCoachEntity>> TRAIN_COACH = register(
      "train_coach",
      EntityType.Builder.<TrainCoachEntity>create(TrainCoachEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(8)
          .updateInterval(3)
          .immuneToFire()
  );
  public static final RegistryObject<EntityType<FreightCarEntity>> FREIGHT_CAR = register(
      "freight_car",
      EntityType.Builder.<FreightCarEntity>create(FreightCarEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(8)
          .updateInterval(3)
          .immuneToFire()
  );

  private static <T extends Entity> RegistryObject<EntityType<T>> register(final String id, EntityType.Builder<T> builder) {
    return REGISTER.register(id, () -> builder.build(id));
  }
}
