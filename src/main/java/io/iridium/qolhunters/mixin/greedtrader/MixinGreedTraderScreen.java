package io.iridium.qolhunters.mixin.greedtrader;

import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.features.greedtrader.GreedTraderScreenTweaks;
import iskallia.vault.client.gui.screen.GreedTraderScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GreedTraderScreen.class, remap = false)
public class MixinGreedTraderScreen {
    @Inject(method = "render", at = @At("TAIL"), remap = true)
    private void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        GreedTraderScreenTweaks.renderCoins((GreedTraderScreen) (Object) this, poseStack, mouseX, mouseY, partialTick);
    }
}
