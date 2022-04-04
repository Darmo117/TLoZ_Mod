package net.darmo_creations.tloz_mod;

import net.darmo_creations.tloz_mod.blocks.IModBlock;
import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.entities.ModEntities;
import net.darmo_creations.tloz_mod.entities.renderers.BombEntityRenderer;
import net.darmo_creations.tloz_mod.entities.renderers.JarEntityRenderer;
import net.darmo_creations.tloz_mod.entities.renderers.RockEntityRenderer;
import net.darmo_creations.tloz_mod.entities.renderers.TLoZArrowRenderer;
import net.darmo_creations.tloz_mod.items.BombBagItem;
import net.darmo_creations.tloz_mod.items.HeartItem;
import net.darmo_creations.tloz_mod.items.ModItems;
import net.darmo_creations.tloz_mod.items.QuiverItem;
import net.darmo_creations.tloz_mod.tile_entities.ModTileEntities;
import net.darmo_creations.tloz_mod.tile_entities.renderers.BombBreakableBlockTileEntityRenderer;
import net.darmo_creations.tloz_mod.tile_entities.renderers.BombFlowerTileEntityRenderer;
import net.darmo_creations.tloz_mod.tile_entities.renderers.SafeZoneEffectAreaTileEntityRenderer;
import net.darmo_creations.tloz_mod.tile_entities.renderers.SpikesEffectAreaTileEntityRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Mod’s main class.
 */
@Mod(TLoZ.MODID)
public class TLoZ {
  public static final String MODID = "tloz";

  private static final Logger LOGGER = LogManager.getLogger();

  /**
   * Mod’s creative mode tab.
   */
  public static final CreativeTab CREATIVE_MODE_TAB = new CreativeTab();

  public TLoZ() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::setup);
    ModTileEntities.REGISTER.register(modEventBus);
    ModEntities.REGISTER.register(modEventBus);
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(BombBagItem.class);
    MinecraftForge.EVENT_BUS.register(QuiverItem.class);
    MinecraftForge.EVENT_BUS.register(HeartItem.class);
  }

  private void setup(final FMLCommonSetupEvent event) {
    RenderingRegistry.registerEntityRenderingHandler(ModEntities.BOMB.get(), BombEntityRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntities.JAR.get(), JarEntityRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntities.ROCK.get(), RockEntityRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntities.ARROW.get(), TLoZArrowRenderer::new);
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.BOMB_FLOWER.get(), BombFlowerTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.BOMB_BREAKABLE_BLOCK.get(), BombBreakableBlockTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.SAFE_ZONE_EFFECT_AREA.get(), SafeZoneEffectAreaTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.SPIKES_EFFECT_AREA.get(), SpikesEffectAreaTileEntityRenderer::new);
    RenderTypeLookup.setRenderLayer(ModBlocks.BOMB_FLOWER, RenderType.getCutoutMipped());
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
      blockRegistryEvent.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
      itemRegistryEvent.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
      // Generate BlockItems
      itemRegistryEvent.getRegistry().registerAll(ModBlocks.BLOCKS.stream()
          .filter(block -> !(block instanceof IModBlock) || ((IModBlock) block).hasGeneratedItemBlock())
          .map(block -> {
            BlockItem itemBlock = new BlockItem(block, new Item.Properties().group(CREATIVE_MODE_TAB));
            //noinspection ConstantConditions
            itemBlock.setRegistryName(block.getRegistryName());
            ModItems.ITEM_BLOCKS.put(block, itemBlock);
            return itemBlock;
          })
          .toArray(Item[]::new)
      );
      // Bow’s pulling properties
      ItemModelsProperties.registerProperty(ModItems.BOW, new ResourceLocation("pull"), (stack, world, entity) -> {
        if (entity == null) {
          return 0;
        } else {
          return entity.getActiveItemStack() != stack ? 0 : (stack.getUseDuration() - entity.getItemInUseCount()) / 20F;
        }
      });
      ItemModelsProperties.registerProperty(ModItems.BOW, new ResourceLocation("pulling"),
          (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1 : 0);
      // Bow of light’s pulling properties
      ItemModelsProperties.registerProperty(ModItems.BOW_OF_LIGHT, new ResourceLocation("pull"), (stack, world, entity) -> {
        if (entity == null) {
          return 0;
        } else {
          return entity.getActiveItemStack() != stack ? 0 : (stack.getUseDuration() - entity.getItemInUseCount()) / 20F;
        }
      });
      ItemModelsProperties.registerProperty(ModItems.BOW_OF_LIGHT, new ResourceLocation("pulling"),
          (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1 : 0);
    }
  }
}
