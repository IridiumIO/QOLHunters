package io.iridium.qolhunters.mixin.temporalmodifiertimer;

import io.iridium.qolhunters.features.temporalmodifiertimer.VaultModifierWithTime;
import iskallia.vault.core.vault.modifier.spi.VaultModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(VaultModifier.class)
public abstract class MixinVaultModifier implements VaultModifierWithTime {
    @Unique private Integer qOLHunters$ticksLeft;

    @Override public Integer qOLHunters$getTime() {
        return qOLHunters$ticksLeft;
    }

    @Override public void qOLHunters$setTime(Integer time) {
        qOLHunters$ticksLeft = time;
    }
}
