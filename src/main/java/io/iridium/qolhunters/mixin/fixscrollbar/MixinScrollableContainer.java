package io.iridium.qolhunters.mixin.fixscrollbar;

import com.llamalad7.mixinextras.sugar.Local;
import iskallia.vault.client.gui.component.ScrollableContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = ScrollableContainer.class, remap = false)
public abstract class MixinScrollableContainer {
    @Shadow protected int scrollingOffsetY;

    // vh was scaling the arg twice
    @ModifyArg(method = "mouseMoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(III)I", remap = true), index = 0)
    private int fixScrollDelta(int pValue, @Local(ordinal = 3) double deltaOffset) {
        return this.scrollingOffsetY + (int) (deltaOffset);
    }
}
