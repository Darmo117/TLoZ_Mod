package net.darmo_creations.tloz_mod.items;

import net.darmo_creations.tloz_mod.TLoZ;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import java.util.*;

/**
 * This class declares all items for this mod.
 */
@SuppressWarnings("unused")
public final class ModItems {
  // Tools & weapons
  public static final Item BOW = new QuiverBowItem(false).setRegistryName("bow");
  public static final Item BOW_OF_LIGHT = new QuiverBowItem(true).setRegistryName("bow_of_light");
  public static final Item SMALL_QUIVER = new QuiverItem(20).setRegistryName("quiver_small");
  public static final Item MEDIUM_QUIVER = new QuiverItem(30).setRegistryName("quiver_medium");
  public static final Item BIG_QUIVER = new QuiverItem(50).setRegistryName("quiver_big");

  // Bomb-related
  public static final Item SMALL_BOMB_BAG = new BombBagItem(10).setRegistryName("bomb_bag_small");
  public static final Item MEDIUM_BOMB_BAG = new BombBagItem(20).setRegistryName("bomb_bag_medium");
  public static final Item BIG_BOMB_BAG = new BombBagItem(30).setRegistryName("bomb_bag_big");
  public static final Item BOMB_AMMO = new BombItem().setRegistryName("bomb_ammo");

  // Health-related
  public static final Item RED_POTION = new HealthPotionItem(6).setRegistryName("potion.red");
  public static final Item YELLOW_POTION = new HealthPotionItem(8).setRegistryName("potion.yellow");
  public static final Item PURPLE_POTION = new HealthPotionItem(100).setRegistryName("potion.purple");
  public static final Item HEART = new HeartItem().setRegistryName("heart");

  // Treasures
  public static final Item DEMON_FOSSIL = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("demon_fossil");
  public static final Item STALFOS_SKULL = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("stalfos_skull");
  public static final Item STAR_FRAGMENT = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("star_fragment");
  public static final Item BEE_LARVAE = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("bee_larvae");
  public static final Item WOOD_HEART = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("wood_heart");
  public static final Item DARK_PEARL_LOOP = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("dark_pearl_loop");
  public static final Item PEARL_NECKLACE = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("pearl_necklace");
  public static final Item RUTO_CROWN = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("ruto_crown");
  public static final Item DRAGON_SCALE = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("dragon_scale");
  public static final Item PIRATE_NECKLACE = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("pirate_necklace");
  public static final Item PALACE_DISH = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("palace_dish");
  public static final Item GORON_AMBER = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("goron_amber");
  public static final Item MYSTIC_JADE = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("mystic_jade");
  public static final Item ANCIENT_GOLD_PIECE = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("ancient_gold_piece");
  public static final Item ALCHEMY_STONE = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("alchemy_stone");
  public static final Item REGAL_RING = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("regal_ring");

  // Rupees
  public static final Item RUPEE_BAG = new RupeeBagItem().setRegistryName("rupee_bag");
  public static final Item GREEN_RUPEE = new RupeeItem(1).setRegistryName("rupee_green");
  public static final Item BLUE_RUPEE = new RupeeItem(5).setRegistryName("rupee_blue");
  public static final Item RED_RUPEE = new RupeeItem(20).setRegistryName("rupee_red");
  public static final Item BIG_GREEN_RUPEE = new RupeeItem(100).setRegistryName("rupee_big_green");
  public static final Item BIG_RED_RUPEE = new RupeeItem(200).setRegistryName("rupee_big_red");
  public static final Item BIG_GOLD_RUPEE = new RupeeItem(300).setRegistryName("rupee_big_gold");

  // Keys
  public static final Item SMALL_KEY = new TLoZItem(new Item.Properties().group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("small_key");
  public static final Item BOSS_KEY = new TLoZItem(new Item.Properties().maxStackSize(1).group(TLoZ.CREATIVE_MODE_TAB)).setRegistryName("boss_key");

  /**
   * The list of all explicitly declared items for this mod.
   */
  public static final List<Item> ITEMS = new LinkedList<>();
  /**
   * The list of all generated items for this mod’s blocks.
   */
  public static final Map<Block, BlockItem> ITEM_BLOCKS = new HashMap<>();

  static {
    Arrays.stream(ModItems.class.getDeclaredFields())
        .filter(field -> Item.class.isAssignableFrom(field.getType()))
        .map(field -> {
          Item item;
          try {
            item = (Item) field.get(null);
          } catch (IllegalAccessException e) {
            // Should never happen
            throw new RuntimeException(e);
          }
          return item;
        })
        .forEach(ITEMS::add);
  }

  private ModItems() {
  }
}
