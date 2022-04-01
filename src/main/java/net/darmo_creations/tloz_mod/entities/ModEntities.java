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
}
