package net.darmo_creations.tloz_mod.blocks;

import net.minecraft.block.Block;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public final class ModBlocks {
  // TODO blue flower teleporters
  // TODO leaves patch
  public static final Block SHOCK_SWITCH = new ShockSwitchBlock().setRegistryName("shock_switch");
  public static final Block PULL_SWITCH = new PullSwitchBlock().setRegistryName("pull_switch");
  public static final Block FLOOR_SWITCH = new FloorSwitchBlock().setRegistryName("floor_switch");
  public static final Block EYE_SWITCH = new EyeSwitchBlock().setRegistryName("eye_switch");
  public static final Block LIGHT_EYE_SWITCH = new LightEyeSwitchBlock().setRegistryName("light_eye_switch");

  public static final Block BOMB_FLOWER = new BombFlowerBlock().setRegistryName("bomb_flower");
  public static final Block FLOWER_BOMB = new DummyBombBlock().setRegistryName("flower_bomb");
  public static final Block BOMB = new DummyBombBlock().setRegistryName("bomb");

  public static final Block BOMB_BREAKABLE_BLOCK = new BombBreakableBlock().setRegistryName("bomb_breakable_block");
  public static final Block CRUMBLY_BLOCK = new CrumblyBlock().setRegistryName("crumbly_block");

  public static final Block SPIKES_BASE = new SpikesBaseBlock().setRegistryName("spikes_base");
  public static final Block SPIKES = new SpikesBlock().setRegistryName("spikes");
  public static final Block SPIKES_EFFECT_AREA = new SpikesEffectAreaBlock().setRegistryName("spikes_effect_area");

  public static final Block ITEM_BULB = new DummyItemBulbBlock().setRegistryName("item_bulb");
  public static final Block ITEM_BULB_FLOWER = new ItemBulbFlowerBlock().setRegistryName("item_bulb_flower");
  public static final Block JAR = new JarBlock().setRegistryName("jar");
  public static final Block ROCK = new RockBlock().setRegistryName("rock");
  // TODO growable "acorns" that drop arrows

  public static final Block SAFE_ZONE = new SafeZoneBlock().setRegistryName("safe_zone");
  public static final Block SAFE_ZONE_EFFECT_AREA = new SafeZoneEffectAreaBlock().setRegistryName("safe_zone_effect_area");

  /**
   * The list of all declared blocks for this mod.
   */
  public static final List<Block> BLOCKS = new LinkedList<>();

  static {
    Arrays.stream(ModBlocks.class.getDeclaredFields())
        .filter(field -> Block.class.isAssignableFrom(field.getType()))
        .map(field -> {
          Block block;
          try {
            block = (Block) field.get(null);
          } catch (IllegalAccessException e) {
            // Should never happen
            throw new RuntimeException(e);
          }
          return block;
        })
        .forEach(BLOCKS::add);
  }

  private ModBlocks() {
  }
}
