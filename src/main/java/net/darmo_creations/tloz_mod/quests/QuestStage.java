package net.darmo_creations.tloz_mod.quests;

import net.darmo_creations.tloz_mod.quests.actions.QuestStageAction;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class QuestStage {
  private final String descriptionKey;
  private final List<QuestStageAction> actions;

  public QuestStage(final String descriptionKey, final List<QuestStageAction> actions) {
    this.descriptionKey = descriptionKey;
    this.actions = new LinkedList<>(actions);
  }

  public Optional<TextComponent> getDescription() {
    return this.descriptionKey == null ? Optional.empty() : Optional.of(new TranslationTextComponent(this.descriptionKey));
  }

  public void execute(GameContext context) {
    this.actions.forEach(action -> action.execute(context));
  }
}
