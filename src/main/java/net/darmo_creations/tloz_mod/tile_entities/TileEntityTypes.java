package net.darmo_creations.tloz_mod.tile_entities;

import com.mojang.datafixers.types.Type;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraft.util.registry.Registry;

public class TileEntityTypes {
  public static final TileEntityType<OrbSwitchTileEntity> ORB_SWITCH = register("orb_switch", TileEntityType.Builder.create(OrbSwitchTileEntity::new));

  private static <T extends TileEntity> TileEntityType<T> register(String key, TileEntityType.Builder<T> builder) {
    Type<?> type = Util.attemptDataFix(TypeReferences.BLOCK_ENTITY, key);
    //noinspection deprecation,ConstantConditions
    return Registry.register(Registry.BLOCK_ENTITY_TYPE, key, builder.build(type));
  }
}
