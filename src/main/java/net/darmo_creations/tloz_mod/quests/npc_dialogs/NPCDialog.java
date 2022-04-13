package net.darmo_creations.tloz_mod.quests.npc_dialogs;

import net.darmo_creations.tloz_mod.quests.GameContext;
import net.darmo_creations.tloz_mod.quests.npc_dialogs.conditions.DialogCondition;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NPCDialog {
  private final List<DialogCondition> conditions;
  private final List<DialogText> lines;

  public NPCDialog(final List<DialogCondition> conditions, final List<DialogText> lines) {
    this.conditions = new LinkedList<>(conditions);
    this.lines = new ArrayList<>(lines);
  }

  public boolean isAvailable(GameContext context) {
    return this.conditions.stream().allMatch(condition -> condition.evaluate(context));
  }

  public boolean onTrigger(GameContext context) {
    // TODO
    return false;
  }
}
