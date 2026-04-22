package io.iridium.qolhunters.mixin.quest;

import io.iridium.qolhunters.features.quest.QuestDependencyState;
import iskallia.vault.client.gui.screen.quest.QuestOverviewElementScreen;
import iskallia.vault.quest.base.Quest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = QuestOverviewElementScreen.class, remap = false)
public class MixinQuestOverviewElementScreen {
    @Inject(method = "selectQuest", at = @At("TAIL"))
    private void setSelectedQuestDep(Quest quest, CallbackInfo ci){
        if (quest != null) {
            QuestDependencyState.setCurrentDep(quest.getUnlockedBy());
        }
    }
}
