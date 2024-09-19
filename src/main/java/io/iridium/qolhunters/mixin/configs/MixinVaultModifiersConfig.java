package io.iridium.qolhunters.mixin.configs;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


//UNUSED

@Mixin(iskallia.vault.config.VaultModifiersConfig.class)
public class MixinVaultModifiersConfig {

    @Inject(at = @At("HEAD"), method = "getName()Ljava/lang/String;", cancellable = true, remap = false)
    private void getName(CallbackInfoReturnable<String> cir) {

        cir.setReturnValue("iridium/vault_modifiers");
    }

}
