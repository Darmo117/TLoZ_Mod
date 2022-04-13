package net.darmo_creations.tloz_mod.quests.actions;

import net.darmo_creations.tloz_mod.quests.GameContext;

public class EmptyFreightCarQuestStageAction extends QuestStageAction {
  @Override
  public void execute(GameContext context) {
    if (context.getTrain().isPresent()) {
      // TODO empty minecart
    }
  }
}
