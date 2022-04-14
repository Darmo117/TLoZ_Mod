package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.ModSoundEvents;
import net.darmo_creations.tloz_mod.entities.capabilities.TeleportData;
import net.darmo_creations.tloz_mod.entities.capabilities.TeleportDataCapabilityManager;
import net.darmo_creations.tloz_mod.network.ModNetworkManager;
import net.darmo_creations.tloz_mod.network.TeleportDataMessage;
import net.darmo_creations.tloz_mod.tile_entities.BlueLightTeleporterTileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Locale;
import java.util.Optional;

public class BlueLightTeleporter extends ContainerBlock {
  public static final int DELAY = 100; // 5s

  private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 1, 16);

  protected BlueLightTeleporter() {
    super(Properties.create(Material.PORTAL)
        .doesNotBlockMovement()
        .hardnessAndResistance(-1)
        .sound(SoundType.GLASS)
        .setLightLevel(value -> 11));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return SHAPE;
  }

  @Override
  @SuppressWarnings("deprecation")
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    TileEntity te = world.getTileEntity(pos);
    if (!world.isRemote && te instanceof BlueLightTeleporterTileEntity
        && entity instanceof PlayerEntity && entity.getPosition().equals(pos)) {
      BlueLightTeleporterTileEntity t = (BlueLightTeleporterTileEntity) te;
      t.getTargetPos().ifPresent(targetPos -> {
        ServerPlayerEntity player = (ServerPlayerEntity) entity;
        LazyOptional<TeleportData> capability = player.getCapability(TeleportDataCapabilityManager.INSTANCE);
        capability.ifPresent(teleportData -> {
          if (!teleportData.getDelay().isPresent()) {
            teleportData.setDelay(DELAY);
            teleportData.setTargetPosition(targetPos);
            teleportData.setTargetYaw(t.getYaw().orElse(null));
            teleportData.setTargetPitch(t.getPitch().orElse(null));
            world.playSound(null, pos, ModSoundEvents.TELEPORT, SoundCategory.BLOCKS, 0.2f, 1);
            ModNetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new TeleportDataMessage(teleportData));
          }
        });
      });
    }
  }

  /**
   * Updates the teleportation logic of any player that stepped into a teleporter.
   * Teleportation is cancelled if the player steps out of the teleporter.
   */
  @SubscribeEvent
  public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
    PlayerEntity player = event.player;
    if (player.world.isRemote) {
      return;
    }
    player.getCapability(TeleportDataCapabilityManager.INSTANCE).ifPresent(teleportData -> {
      Optional<Integer> optDelay = teleportData.getDelay();
      Optional<BlockPos> optPos = teleportData.getTargetPosition();
      if (!optDelay.isPresent() || !optPos.isPresent()) {
        return;
      }
      int delay = optDelay.get();
      if (!(player.world.getBlockState(player.getPosition()).getBlock() instanceof BlueLightTeleporter)) {
        teleportData.reset();
      } else if (delay == 0) {
        BlockPos targetPos = optPos.get();
        MinecraftServer server = ((ServerWorld) player.world).getServer();
        String command = String.format(Locale.ENGLISH, "tp %s %d %d %d %f %f",
            player.getGameProfile().getName(),
            targetPos.getX(), targetPos.getY(), targetPos.getZ(),
            teleportData.getTargetYaw().orElse(player.rotationYaw),
            teleportData.getTargetPitch().orElse(player.rotationPitch)
        );
        server.getCommandManager().handleCommand(server.getCommandSource().withFeedbackDisabled(), command);
        teleportData.reset();
      } else {
        teleportData.setDelay(delay - 1);
      }
      ModNetworkManager.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new TeleportDataMessage(teleportData));
    });
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.INVISIBLE;
  }

  @Override
  public TileEntity createNewTileEntity(IBlockReader world) {
    return new BlueLightTeleporterTileEntity();
  }
}
