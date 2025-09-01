package io.iridium.qolhunters.mixin.cardrollrange;

import com.llamalad7.mixinextras.sugar.Local;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.core.card.modifier.card.CardModifier;
import iskallia.vault.core.card.modifier.card.TaskLootCardModifier;
import iskallia.vault.core.world.roll.IntRoll;
import iskallia.vault.task.ProgressConfiguredTask;
import iskallia.vault.task.Task;
import iskallia.vault.task.counter.TargetTaskCounter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mixin(value = TaskLootCardModifier.class, remap = false)
public abstract class MixinTaskLootCardModifier extends CardModifier<TaskLootCardModifier.Config> {
    @Shadow private Task task;

    private MixinTaskLootCardModifier(TaskLootCardModifier.Config config) {
        super(config);
    }

    @Inject(method = "addText", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void addRollRange(List<Component> tooltip, int minIndex, TooltipFlag flag, float time, int tier, CallbackInfo ci, @Local Component[] text) {
        if (!QOLHuntersClientConfigs.RESOURCE_CARD_ROLL_RANGE.get()) {
            return;
        }
        if (Screen.hasShiftDown() && text[0] instanceof TextComponent txtCmp) {
            ProgressConfiguredTask<?,?> progressTask = (ProgressConfiguredTask<?,?>) this.task.streamSelfAndDescendants().filter(ProgressConfiguredTask.class::isInstance).findFirst().orElse(null);
            if (progressTask != null){
                var targetCountConfig = progressTask.getCounter().getConfig() instanceof TargetTaskCounter.Config<?,?> ttc ? ttc : null;
                if (targetCountConfig == null) {
                    return;
                }

                IntRoll targetRoll = targetCountConfig.getTarget() instanceof IntRoll intRoll ? intRoll : null;
                if (targetRoll == null) {
                    return;
                }

                var cntCfg = this.getConfig() instanceof AccessorTaskLootCardModifierConfig atlcm ? atlcm : null;
                if (cntCfg == null) {
                    return;
                }
                Map<Integer, IntRoll> counts = cntCfg.getCount();

                int minKey = Collections.min(counts.keySet());
                int maxKey = Collections.max(counts.keySet());

                int minCount = counts.get(minKey).getMin();
                int maxCount = counts.get(maxKey).getMax();

                int minTarget = targetRoll.getMin();
                int maxTarget = targetRoll.getMax();

                String countString = Objects.equals(minCount, maxCount) ? String.valueOf(minCount) : minCount + "-" + maxCount;
                String targetString = Objects.equals(minTarget, maxTarget) ? String.valueOf(minTarget) : minTarget + "-" + maxTarget;

                txtCmp.append(new TextComponent(" (" + countString+ " per " + targetString + ")").withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
