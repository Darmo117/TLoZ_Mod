package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.Utils;
import net.minecraft.block.Block;

import java.util.List;

@SuppressWarnings("unused")
public final class ModBlocks {
  // TODO mailbox (only if worth it)

  public static final Block BLUE_LIGHT_TELEPORTER = new BlueLightTeleporter().setRegistryName("blue_light_teleporter");
  public static final Block SPAWNPOINT_SETTER = new SpawnpointSetterBlock().setRegistryName("spawnpoint_setter");
  public static final Block KILL_TRIGGER = new KillTriggerBlock().setRegistryName("kill_trigger");

  public static final Block SHOCK_SWITCH = new ShockSwitchBlock().setRegistryName("shock_switch");
  public static final Block PULL_SWITCH = new PullSwitchBlock().setRegistryName("pull_switch");
  public static final Block FLOOR_SWITCH = new FloorSwitchBlock().setRegistryName("floor_switch");
  // TODO models
  public static final Block EYE_SWITCH = new EyeSwitchBlock().setRegistryName("eye_switch");
  // TODO models
  public static final Block LIGHT_EYE_SWITCH = new LightEyeSwitchBlock().setRegistryName("light_eye_switch");
  // TODO models
  public static final Block WIND_SWITCH = new WindSwitchBlock().setRegistryName("wind_switch");
  // TODO torchlight

  public static final Block LOCKED_DOOR = new LockedDoor().setRegistryName("locked_door");
  // TODO model
  public static final Block BOSS_DOOR = new BossDoorBlock().setRegistryName("boss_door");
  // TODO model
  public static final Block BOSS_KEY = new BossKeyBlock().setRegistryName("boss_key");

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

  public static final Block LEAVES_PATCH = new LeavesPatchBlock().setRegistryName("leaves_patch");

  public static final Block SAFE_ZONE = new SafeZoneBlock().setRegistryName("safe_zone");
  public static final Block SAFE_ZONE_EFFECT_AREA = new SafeZoneEffectAreaBlock().setRegistryName("safe_zone_effect_area");

  public static final Block TREASURE_CHEST = new TreasureChestBlock(TreasureChestBlock.Type.SIMPLE).setRegistryName("treasure_chest");
  public static final Block DOUBLE_TREASURE_CHEST = new TreasureChestBlock(TreasureChestBlock.Type.DOUBLE).setRegistryName("double_treasure_chest");
  public static final Block MIMIC_CHEST = new TreasureChestBlock(TreasureChestBlock.Type.MIMIC).setRegistryName("mimic_chest");

  /**
   * The list of all declared blocks for this mod.
   */
  public static final List<Block> BLOCKS;

  static {
    BLOCKS = Utils.gatherEntries(ModBlocks.class, Block.class);
  }

  private ModBlocks() {
  }
}
