package net.darmo_creations.tloz_mod.quests.actions;

import net.darmo_creations.tloz_mod.quests.GameContext;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class FillFreightCarQuestStageAction extends QuestStageAction {
  private final ResourceLocation item;
  private final int amount;

  public FillFreightCarQuestStageAction(final ResourceLocation item, final int amount) {
    this.item = Objects.requireNonNull(item);
    this.amount = amount;
  }

  @Override
  public void execute(GameContext context) {
    if (!context.getTrain().isPresent()) {
      // TODO fill minecart
    }
  }
}
