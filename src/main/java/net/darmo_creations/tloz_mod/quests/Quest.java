package net.darmo_creations.tloz_mod.quests;

import net.darmo_creations.tloz_mod.NBTSerializable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Quest implements NBTSerializable {
  private static final String STAGE_INDEX_KEY = "StageIndex";

  private final String nameKey;
  private final List<QuestStage> stages;
  private int stageIndex;

  public Quest(final String nameKey, final List<QuestStage> stages) {
    this.nameKey = Objects.requireNonNull(nameKey);
    this.stages = new ArrayList<>(stages);
    this.stageIndex = 0;
  }

  public TextComponent getName() {
    return new TranslationTextComponent(this.nameKey);
  }

  public boolean isDone() {
    return this.stageIndex >= this.stages.size();
  }

  public int getStageIndex() {
    return this.stageIndex;
  }

  public boolean goToNextStage(GameContext context) {
    if (this.isDone()) {
      return false;
    }
    this.stages.get(this.stageIndex).execute(context);
    this.stageIndex++;
    return true;
  }

  @Override
  public CompoundNBT writeToNBT() {
    CompoundNBT nbt = new CompoundNBT();
    nbt.putInt(STAGE_INDEX_KEY, this.stageIndex);
    return nbt;
  }

  @Override
  public void readFromNBT(CompoundNBT tag) {
    this.stageIndex = Math.max(0, tag.getInt(STAGE_INDEX_KEY));
  }
}
