package io.iridium.qolhunters.mixin.cardrollrange;

import iskallia.vault.core.card.modifier.card.TaskLootCardModifier;
import iskallia.vault.core.world.roll.IntRoll;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = TaskLootCardModifier.Config.class, remap = false)
public interface AccessorTaskLootCardModifierConfig {
    @Accessor
    Map<Integer, IntRoll> getCount();
}
