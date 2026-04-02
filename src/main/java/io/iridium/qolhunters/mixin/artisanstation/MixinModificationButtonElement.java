package io.iridium.qolhunters.mixin.artisanstation;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.framework.element.ModificationButtonElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ModificationButtonElement.class, remap = false)
public class MixinModificationButtonElement {
    @WrapOperation(method = "lambda$new$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;hasShiftDown()Z"))
    private boolean configurableShiftDown(Operation<Boolean> original){
        return original.call() || !QOLHuntersClientConfigs.ARTISAN_GEAR_TOOLTIP_REQUIRES_SHIFT.get();
    }
}
