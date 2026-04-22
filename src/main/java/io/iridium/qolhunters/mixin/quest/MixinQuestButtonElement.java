package io.iridium.qolhunters.mixin.quest;

import com.llamalad7.mixinextras.sugar.Local;
import io.iridium.qolhunters.features.quest.QuestDependencyState;
import iskallia.vault.client.gui.screen.quest.QuestButtonElement;
import iskallia.vault.quest.QuestState;
import iskallia.vault.quest.base.Quest;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = QuestButtonElement.class, remap = false)
public class MixinQuestButtonElement {
    @Shadow @Final private Quest quest;

    @Inject(method = "lambda$new$0", at = @At("TAIL"))
    private void changeStyle(CallbackInfoReturnable<Component> cir, @Local(name = "locked") boolean locked, @Local(name = "state") QuestState state){
        if (!state.getCompleted().contains(quest.getId())) {
            String currentDep = QuestDependencyState.getCurrentDep();
            if (currentDep != null && currentDep.equals(quest.getId())) {
                if (cir.getReturnValue() instanceof TextComponent tc) {
                    tc.withStyle(ChatFormatting.UNDERLINE);
                }
            }
        }
    }
}
