package io.iridium.qolhunters.mixin.zoomoutfurther;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.screen.player.legacy.tab.split.spi.AbstractPanRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AbstractPanRegion.class, remap = false)
public class MixinAbstractPanRegion {

    @WrapOperation(method = "clampViewportScale", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", remap = true))
    private float zoomOutFurther(float pValue, float pMin, float pMax, Operation<Float> original) {
        // Mth.clamp(this.viewportScale, 0.3F, 5F)
        if (QOLHuntersClientConfigs.ZOOM_OUT_FURTHER.get()) {
            return original.call(pValue, 0.1F, pMax);
        }
        return original.call(pValue, pMin, pMax);
    }
}
