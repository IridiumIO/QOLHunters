package io.iridium.qolhunters.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(iskallia.vault.config.VaultModifiersConfig.class)
public class MixinVaultModifiersConfig {

    @Inject(at = @At("HEAD"), method = "getName()Ljava/lang/String;", cancellable = true, remap = false)
    private void getName(CallbackInfoReturnable<String> cir) {

        cir.setReturnValue("vault_modifiers_iridium");
    }

}
