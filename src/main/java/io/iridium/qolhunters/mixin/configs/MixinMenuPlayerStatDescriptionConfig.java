package io.iridium.qolhunters.mixin.configs;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(iskallia.vault.config.MenuPlayerStatDescriptionConfig.class)
public class MixinMenuPlayerStatDescriptionConfig {

    @Inject(at = @At("HEAD"), method = "getName()Ljava/lang/String;", cancellable = true, remap = false)
    private void getName(CallbackInfoReturnable<String> cir) {
        if(QOLHuntersClientConfigs.BETTER_STATS_DESCRIPTIONS.get()) cir.setReturnValue("iridium/menu_player_stat_description");
    }

}
