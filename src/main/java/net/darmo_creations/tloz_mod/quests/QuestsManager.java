package net.darmo_creations.tloz_mod.quests;

import java.util.HashMap;
import java.util.Map;

public class QuestsManager {
  private final Map<String, Quest> quests;

  public QuestsManager() {
    this.quests = new HashMap<>();
  }

  public void addQuest(final String name, Quest quest) {
    this.quests.put(name, quest);
  }

  public boolean advanceQuest(GameContext context, final String name) {
    if (!this.quests.containsKey(name)) {
      return false;
    }
    return this.quests.get(name).goToNextStage(context);
  }
}
