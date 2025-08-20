package io.iridium.qolhunters.mixin.fixscrollbar;

import io.iridium.qolhunters.features.fixscrollbar.IMouseRelease;
import iskallia.vault.client.gui.screen.player.legacy.SplitTabContent;
import iskallia.vault.client.gui.screen.player.legacy.tab.split.spi.AbstractDialog;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SplitTabContent.class, remap = false)
public abstract class MixinSplitTabContent {
    @Shadow @Final protected AbstractDialog<?> dialog;

    @Inject(method = "mouseReleased", at = @At("TAIL"))
    private void fixMouseScrolling(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir){
        var rDialog = this.dialog instanceof IMouseRelease mouseRelease ? mouseRelease : null;
        if (rDialog != null) {
            rDialog.qOLHunters$mouseReleased(mouseX, mouseY, button);
        }
    }
}
