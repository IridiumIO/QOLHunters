package io.iridium.qolhunters.mixin.betterlootstats;

import io.iridium.qolhunters.features.bettercheststats.ScreenTextures;
import iskallia.vault.core.vault.stat.VaultChestType;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = VaultChestType.class, remap = false)
public abstract class MixinVaultChestType {
    @Shadow public abstract String getName();

    @Shadow private Object icon;

    @Inject(method = "initClientResources", at = @At(value = "FIELD", target = "Liskallia/vault/core/vault/stat/VaultChestType;iconsInitialized:Z", opcode = Opcodes.PUTFIELD, ordinal = 0))
    private void icons(CallbackInfo ci){
        if (Objects.equals(this.getName(), VaultChestType.ENIGMA.getName())) {
            this.icon = ScreenTextures.ENIGMA_CHEST_SMALL_ICON;
        }
        if (Objects.equals(this.getName(), VaultChestType.HARDENED.getName())) {
            this.icon = ScreenTextures.HARDENED_CHEST_SMALL_ICON;
        }
        if (Objects.equals(this.getName(), VaultChestType.FLESH.getName())) {
            this.icon = ScreenTextures.FLESH_CHEST_SMALL_ICON;
        }
    }
}
