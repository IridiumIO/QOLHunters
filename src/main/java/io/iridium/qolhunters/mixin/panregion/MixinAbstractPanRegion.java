package io.iridium.qolhunters.mixin.panregion;

import iskallia.vault.client.gui.screen.player.legacy.tab.split.spi.AbstractPanRegion;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractPanRegion.class, remap = false)
public class MixinAbstractPanRegion {


    @Inject(method = "clampViewportScale", at=@At(value = "HEAD"), cancellable = true)
    private void clampViewportScale(float viewportScale, CallbackInfoReturnable<Float> cir) {
        float x =  Mth.clamp(viewportScale, 0.1F, 5.0F);

        cir.setReturnValue(x);

    }

}
