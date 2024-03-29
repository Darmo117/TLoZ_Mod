package net.darmo_creations.tloz_mod.blocks;

import net.darmo_creations.tloz_mod.ModSoundEvents;
import net.darmo_creations.tloz_mod.entities.AdditionalDataParameters;
import net.darmo_creations.tloz_mod.tile_entities.BlueLightTeleporterTileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Locale;
import java.util.Optional;
import java.util.OptionalInt;

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
        PlayerEntity player = (PlayerEntity) entity;
        EntityDataManager dataManager = player.getDataManager();
        if (!dataManager.get(AdditionalDataParameters.PLAYER_TELEPORTER_DELAY).isPresent()) {
          dataManager.set(AdditionalDataParameters.PLAYER_TELEPORTER_DELAY, OptionalInt.of(DELAY));
          dataManager.set(AdditionalDataParameters.PLAYER_TELEPORTER_TARGET_POS, Optional.of(targetPos));
          dataManager.set(AdditionalDataParameters.PLAYER_TELEPORTER_YAW, t.getYaw());
          dataManager.set(AdditionalDataParameters.PLAYER_TELEPORTER_PITCH, t.getPitch());
          world.playSound(null, pos, ModSoundEvents.TELEPORT, SoundCategory.BLOCKS, 0.2f, 1);
        }
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
    EntityDataManager dataManager = player.getDataManager();
    OptionalInt optDelay = dataManager.get(AdditionalDataParameters.PLAYER_TELEPORTER_DELAY);
    Optional<BlockPos> optPos = dataManager.get(AdditionalDataParameters.PLAYER_TELEPORTER_TARGET_POS);
    if (optDelay.isPresent() && optPos.isPresent()) {
      int delay = optDelay.getAsInt();
      BlockPos targetPos = optPos.get();
      if (!(player.world.getBlockState(player.getPosition()).getBlock() instanceof BlueLightTeleporter)) {
        resetTeleportation(dataManager);
      } else if (delay == 0) {
        MinecraftServer server = ((ServerWorld) player.world).getServer();
        String command = String.format(Locale.ENGLISH, "tp %s %d %d %d %f %f",
            player.getGameProfile().getName(),
            targetPos.getX(), targetPos.getY(), targetPos.getZ(),
            dataManager.get(AdditionalDataParameters.PLAYER_TELEPORTER_YAW).orElse(player.rotationYaw),
            dataManager.get(AdditionalDataParameters.PLAYER_TELEPORTER_PITCH).orElse(player.rotationPitch)
        );
        server.getCommandManager().handleCommand(server.getCommandSource().withFeedbackDisabled(), command);
        resetTeleportation(dataManager);
      } else {
        dataManager.set(AdditionalDataParameters.PLAYER_TELEPORTER_DELAY, OptionalInt.of(delay - 1));
      }
    }
  }

  private static void resetTeleportation(EntityDataManager dataManager) {
    dataManager.set(AdditionalDataParameters.PLAYER_TELEPORTER_DELAY, OptionalInt.empty());
    dataManager.set(AdditionalDataParameters.PLAYER_TELEPORTER_TARGET_POS, Optional.empty());
    dataManager.set(AdditionalDataParameters.PLAYER_TELEPORTER_YAW, Optional.empty());
    dataManager.set(AdditionalDataParameters.PLAYER_TELEPORTER_PITCH, Optional.empty());
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
