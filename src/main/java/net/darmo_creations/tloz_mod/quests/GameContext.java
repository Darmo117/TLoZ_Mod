package net.darmo_creations.tloz_mod.quests;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Objects;
import java.util.Optional;

public class GameContext {
  private PlayerEntity player;
  private AbstractMinecartEntity train;

  public GameContext(PlayerEntity player, AbstractMinecartEntity train) {
    this.player = Objects.requireNonNull(player);
    this.train = train; // May be null
  }

  public PlayerEntity getPlayer() {
    return this.player;
  }

  public Optional<AbstractMinecartEntity> getTrain() {
    return Optional.ofNullable(this.train);
  }
}
