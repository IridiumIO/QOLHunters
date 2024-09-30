package io.iridium.qolhunters.mixin.rarityhighlighter;

import appeng.api.client.AEStackRendering;
import appeng.api.stacks.AEKey;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.util.SharedFunctions;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AEStackRendering.class)
public abstract class MixinAEStackRendering {

    @Inject(method="drawInGui", at= @At(value = "TAIL"), remap = false)
    private static void renderSlot(Minecraft minecraft, PoseStack poseStack, int x, int y, int z, AEKey what, CallbackInfo ci) {
        if (QOLHuntersClientConfigs.RARITY_HIGHLIGHTER.get()) {
            if ((InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT))) {
                SharedFunctions.renderSlotRarityHighlight(poseStack, what.wrapForDisplayOrFilter(), x, y);
            }
        }

    }

}
