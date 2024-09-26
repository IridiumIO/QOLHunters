package io.iridium.qolhunters.mixin.betterdescriptions;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(iskallia.vault.config.BingoConfig.class)
public class MixinBingoConfig {

    @Inject(at = @At("HEAD"), method = "getName()Ljava/lang/String;", cancellable = true, remap = false)
    private void getName(CallbackInfoReturnable<String> cir) {

        if(QOLHuntersClientConfigs.BETTER_BINGO_DESCRIPTIONS.get()) cir.setReturnValue("iridium/bingo");
    }

}
