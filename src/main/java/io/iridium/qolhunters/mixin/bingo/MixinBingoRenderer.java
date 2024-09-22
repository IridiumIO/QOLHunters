package io.iridium.qolhunters.mixin.bingo;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.task.renderer.BingoRenderer;
import iskallia.vault.task.renderer.context.BingoRendererContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = BingoRenderer.Root.class, remap = false)
public class MixinBingoRenderer {


    @ModifyArg(method="renderExpanded", at= @At(value = "INVOKE", target = "iskallia/vault/task/renderer/context/BingoRendererContext.drawColoredRect (FFFFI)V", ordinal = 0), index = 4)
    public int modifyColor(int color){

        int configopacity = QOLHuntersClientConfigs.BINGO_GRID_BACKGROUND_OPACITY.get();

        int opacity = (int) configopacity * 255 / 100;

        return (opacity << 24);

    }

}
