package io.iridium.qolhunters.mixin.abilities;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.VaultAbilityKeyBinding;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = VaultAbilityKeyBinding.class, remap = false)
public class MixinVaultAbilityKeybinding {
    @WrapOperation(method = "setKeyMapping", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;setKeyModifierAndCode(Lnet/minecraftforge/client/settings/KeyModifier;Lcom/mojang/blaze3d/platform/InputConstants$Key;)V", ordinal = 0))
    private static void dontUnbindIfMulticast(KeyMapping instance, KeyModifier keyModifier, InputConstants.Key keyCode, Operation<Void> original) {
       if(QOLHuntersClientConfigs.ABILITY_MULTICAST.get()) {
           QOLHunters.LOGGER.info("keybinding {} won't be unassigned because multicast is enabled, ignore following log message", instance.getKey());
           return;
       }
       original.call(instance, keyModifier, keyCode);
    }
}
