package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.darmo_creations.tloz_mod.Utils;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class declares all items for this mod.
 */
@SuppressWarnings("unused")
public final class ModItems {
  // Tools & weapons
  public static final Item BOW = new QuiverBowItem(false).setRegistryName("bow");
  public static final Item BOW_OF_LIGHT = new QuiverBowItem(true).setRegistryName("bow_of_light");
  public static final SimpleContainerItem SMALL_QUIVER = (SimpleContainerItem) new QuiverItem(20).setRegistryName("quiver_small");
  public static final SimpleContainerItem MEDIUM_QUIVER = (SimpleContainerItem) new QuiverItem(30).setRegistryName("quiver_medium");
  public static final SimpleContainerItem BIG_QUIVER = (SimpleContainerItem) new QuiverItem(50).setRegistryName("quiver_big");

  // Creative tools
  public static final Item BLUE_TELEPORTER_SETUP_STICK = new BlueLightTeleporterSetupStickItem().setRegistryName("blue_teleporter_setup_stick");

  // Bomb-related
  public static final SimpleContainerItem SMALL_BOMB_BAG = (SimpleContainerItem) new BombBagItem(10).setRegistryName("bomb_bag_small");
  public static final SimpleContainerItem MEDIUM_BOMB_BAG = (SimpleContainerItem) new BombBagItem(20).setRegistryName("bomb_bag_medium");
  public static final SimpleContainerItem BIG_BOMB_BAG = (SimpleContainerItem) new BombBagItem(30).setRegistryName("bomb_bag_big");
  public static final Item BOMB_AMMO = new BombItem().setRegistryName("bomb_ammo");

  // Health-related
  public static final Item RED_POTION = new HealthPotionItem(6).setRegistryName("potion.red");
  public static final Item YELLOW_POTION = new HealthPotionItem(8).setRegistryName("potion.yellow");
  public static final Item PURPLE_POTION = new HealthPotionItem(100).setRegistryName("potion.purple");
  public static final Item HEART = new HeartItem().setRegistryName("heart");
  public static final Item HEART_CONTAINER = new HeartContainerItem().setRegistryName("heart_container");

  // Treasures
  // TODO texture
  public static final TreasureBagItem TREASURE_BAG = (TreasureBagItem) new TreasureBagItem().setRegistryName("treasure_bag");
  public static final Item DEMON_FOSSIL = new TreasureItem(50).setRegistryName("demon_fossil");
  public static final Item STALFOS_SKULL = new TreasureItem(50).setRegistryName("stalfos_skull");
  public static final Item STAR_FRAGMENT = new TreasureItem(50).setRegistryName("star_fragment");
  public static final Item BEE_LARVAE = new TreasureItem(50).setRegistryName("bee_larvae");
  public static final Item WOOD_HEART = new TreasureItem(50).setRegistryName("wood_heart");
  public static final Item DARK_PEARL_LOOP = new TreasureItem(150).setRegistryName("dark_pearl_loop");
  public static final Item PEARL_NECKLACE = new TreasureItem(150).setRegistryName("pearl_necklace");
  public static final Item RUTO_CROWN = new TreasureItem(150).setRegistryName("ruto_crown");
  public static final Item DRAGON_SCALE = new TreasureItem(150).setRegistryName("dragon_scale");
  public static final Item PIRATE_NECKLACE = new TreasureItem(150).setRegistryName("pirate_necklace");
  public static final Item PALACE_DISH = new TreasureItem(500).setRegistryName("palace_dish");
  public static final Item GORON_AMBER = new TreasureItem(500).setRegistryName("goron_amber");
  public static final Item MYSTIC_JADE = new TreasureItem(500).setRegistryName("mystic_jade");
  public static final Item ANCIENT_GOLD_PIECE = new TreasureItem(500).setRegistryName("ancient_gold_piece");
  public static final Item ALCHEMY_STONE = new TreasureItem(2500).setRegistryName("alchemy_stone");
  public static final Item REGAL_RING = new TreasureItem(2500).setRegistryName("regal_ring");

  // Rupees
  public static final SimpleContainerItem RUPEE_BAG = (SimpleContainerItem) new RupeeBagItem().setRegistryName("rupee_bag");
  public static final Item GREEN_RUPEE = new RupeeItem(1).setRegistryName("rupee_green");
  public static final Item BLUE_RUPEE = new RupeeItem(5).setRegistryName("rupee_blue");
  public static final Item RED_RUPEE = new RupeeItem(20).setRegistryName("rupee_red");
  public static final Item BIG_GREEN_RUPEE = new RupeeItem(100).setRegistryName("rupee_big_green");
  public static final Item BIG_RED_RUPEE = new RupeeItem(200).setRegistryName("rupee_big_red");
  public static final Item BIG_GOLD_RUPEE = new RupeeItem(300).setRegistryName("rupee_big_gold");

  // Keys
  public static final Item SMALL_KEY = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("small_key");

  /**
   * The list of all explicitly declared items for this mod.
   */
  public static final List<Item> ITEMS;
  /**
   * The list of all generated items for this mod’s blocks.
   */
  public static final Map<Block, BlockItem> ITEM_BLOCKS = new HashMap<>();

  static {
    ITEMS = Utils.gatherEntries(ModItems.class, Item.class);
  }

  private ModItems() {
  }
}
