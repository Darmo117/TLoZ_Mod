package net.darmo_creations.tloz_mod;

import net.darmo_creations.tloz_mod.blocks.BlueLightTeleporter;
import net.darmo_creations.tloz_mod.blocks.IModBlock;
import net.darmo_creations.tloz_mod.blocks.ModBlocks;
import net.darmo_creations.tloz_mod.commands.SetMaxHealthCommand;
import net.darmo_creations.tloz_mod.entities.ModEntities;
import net.darmo_creations.tloz_mod.entities.PickableEntity;
import net.darmo_creations.tloz_mod.entities.renderers.*;
import net.darmo_creations.tloz_mod.gui.HUD;
import net.darmo_creations.tloz_mod.gui.InventoryGUI;
import net.darmo_creations.tloz_mod.gui.TeleporterEffectGUI;
import net.darmo_creations.tloz_mod.items.ModItems;
import net.darmo_creations.tloz_mod.items.QuiverItem;
import net.darmo_creations.tloz_mod.items.SpecialPickableItem;
import net.darmo_creations.tloz_mod.particles.BlueTeleporterParticle;
import net.darmo_creations.tloz_mod.particles.ModParticles;
import net.darmo_creations.tloz_mod.tile_entities.ModTileEntities;
import net.darmo_creations.tloz_mod.tile_entities.renderers.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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

  @OnlyIn(Dist.CLIENT)
  public static final HUD MAIN_HUD = new HUD(Minecraft.getInstance());
  @OnlyIn(Dist.CLIENT)
  public static final TeleporterEffectGUI TELEPORTER_EFFECT_GUI = new TeleporterEffectGUI(Minecraft.getInstance());
  @OnlyIn(Dist.CLIENT)
  public static final InventoryGUI INVENTORY_GUI = new InventoryGUI(Minecraft.getInstance());

  public TLoZ() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::setupClient);
    ModTileEntities.REGISTER.register(modEventBus);
    ModEntities.REGISTER.register(modEventBus);
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.register(QuiverItem.class);
    MinecraftForge.EVENT_BUS.register(SpecialPickableItem.class);
    MinecraftForge.EVENT_BUS.register(PickableEntity.class);
    MinecraftForge.EVENT_BUS.register(BlueLightTeleporter.class);
  }

  private void setupClient(final FMLClientSetupEvent event) {
    // Entity renderers
    RenderingRegistry.registerEntityRenderingHandler(ModEntities.BOMB.get(), BombEntityRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntities.ITEM_BULB.get(), ItemBulbEntityRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntities.JAR.get(), JarEntityRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntities.ROCK.get(), RockEntityRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntities.BOSS_KEY.get(), BossKeyEntityRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntities.ARROW.get(), TLoZArrowRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntities.WHIRLWIND.get(), WhirlwindEntityRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(ModEntities.LOCOMOTIVE.get(), LocomotiveEntityRenderer::new);
    // Tile entity renderers
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.BOMB_BREAKABLE_BLOCK.get(), BombBreakableBlockTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.BOMB_FLOWER.get(), BombFlowerTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.ITEM_BULB_FLOWER.get(), ItemBulbTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.BOSS_KEY.get(), BossKeyTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.SAFE_ZONE_EFFECT_AREA.get(), SafeZoneEffectAreaTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.SPIKES_EFFECT_AREA.get(), SpikesEffectAreaTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.SPAWNPOINT_SETTER.get(), SpawnPointSetterTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.TREASURE_CHEST.get(), TreasureChestTileEntityRenderer::new);
    ClientRegistry.bindTileEntityRenderer(ModTileEntities.BLUE_LIGHT_TELEPORTER.get(), BlueLightTeleporterTileEntityRenderer::new);
    // Transparent block textures
    RenderTypeLookup.setRenderLayer(ModBlocks.BOMB_FLOWER, RenderType.getCutoutMipped());
    RenderTypeLookup.setRenderLayer(ModBlocks.ITEM_BULB_FLOWER, RenderType.getCutoutMipped());
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
  public static class ModEvents {
    @SubscribeEvent
    public static void onCommandsRegistry(final RegisterCommandsEvent event) {
      SetMaxHealthCommand.register(event.getDispatcher());
    }
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {
    @SubscribeEvent
    public static void onSoundEventRegistry(final RegistryEvent.Register<SoundEvent> event) {
      event.getRegistry().registerAll(ModSoundEvents.SOUND_EVENTS.toArray(new SoundEvent[0]));
    }

    @SubscribeEvent
    public static void onParticleFactoryRegistry(final ParticleFactoryRegisterEvent event) {
      Minecraft.getInstance().particles.registerFactory(ModParticles.BLUE_TELEPORTER, BlueTeleporterParticle.Factory::new);
    }

    @SubscribeEvent
    public static void onParticleRegistry(final RegistryEvent.Register<ParticleType<?>> event) {
      event.getRegistry().registerAll(ModParticles.PARTICLE_TYPES.toArray(new ParticleType[0]));
    }

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
