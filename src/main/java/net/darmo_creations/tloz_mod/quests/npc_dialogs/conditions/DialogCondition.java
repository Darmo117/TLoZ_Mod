package net.darmo_creations.tloz_mod.quests.npc_dialogs.conditions;

import net.darmo_creations.tloz_mod.quests.GameContext;

public abstract class DialogCondition {
  public abstract boolean evaluate(GameContext context);
}
