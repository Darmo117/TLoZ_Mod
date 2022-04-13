package net.darmo_creations.tloz_mod.quests.actions;

import net.darmo_creations.tloz_mod.quests.GameContext;

import java.util.Objects;

public class NPCRideQuestStageAction extends QuestStageAction {
  private final boolean embark;
  private final String npcName;

  public NPCRideQuestStageAction(boolean embark, final String npcName) {
    this.embark = embark;
    this.npcName = Objects.requireNonNull(npcName);
  }

  @Override
  public void execute(GameContext context) {
    // TODO
  }
}
