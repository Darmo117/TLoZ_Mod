package net.darmo_creations.tloz_mod.quests.actions;

import net.darmo_creations.tloz_mod.quests.GameContext;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class GiveItemQuestStageAction extends QuestStageAction {
  private final ResourceLocation item;

  public GiveItemQuestStageAction(final ResourceLocation item) {
    this.item = Objects.requireNonNull(item);
  }

  @Override
  public void execute(GameContext context) {
    // TODO give item
  }
}
