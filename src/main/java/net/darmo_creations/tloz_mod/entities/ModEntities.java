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
      "bomb_entity",
      () -> EntityType.Builder.<BombEntity>create(BombEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("bomb_entity")
  );
  public static final RegistryObject<EntityType<JarEntity>> JAR = REGISTER.register(
      "jar_entity",
      () -> EntityType.Builder.<JarEntity>create(JarEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("jar_entity")
  );
  public static final RegistryObject<EntityType<ItemBulbEntity>> ITEM_BULB = REGISTER.register(
      "item_bulb_entity",
      () -> EntityType.Builder.<ItemBulbEntity>create(ItemBulbEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("item_bulb_entity")
  );
  public static final RegistryObject<EntityType<RockEntity>> ROCK = REGISTER.register(
      "rock_entity",
      () -> EntityType.Builder.<RockEntity>create(RockEntity::new, EntityClassification.MISC)
          .size(0.98f, 0.98f) // TODO size
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("rock_entity")
  );
  public static final RegistryObject<EntityType<TLoZArrowEntity>> ARROW = REGISTER.register(
      "arrow_entity",
      () -> EntityType.Builder.<TLoZArrowEntity>create(TLoZArrowEntity::new, EntityClassification.MISC)
          .size(0.5f, 0.5f)
          .trackingRange(10)
          .updateInterval(10)
          .immuneToFire()
          .build("arrow_entity")
  );
}
