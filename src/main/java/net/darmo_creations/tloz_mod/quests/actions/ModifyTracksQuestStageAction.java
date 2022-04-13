package net.darmo_creations.tloz_mod.quests.actions;

import net.darmo_creations.tloz_mod.quests.GameContext;

public class ModifyTracksQuestStageAction extends QuestStageAction {
  private final boolean unlock;
  private final TrackRegion trackRegion;

  public ModifyTracksQuestStageAction(final boolean unlock, final TrackRegion trackRegion) {
    this.unlock = unlock;
    this.trackRegion = trackRegion;
  }

  @Override
  public void execute(GameContext context) {
    // TODO call mcfunction to unlock/lock tracks
  }
}
