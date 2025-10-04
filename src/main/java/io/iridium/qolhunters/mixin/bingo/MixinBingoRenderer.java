package io.iridium.qolhunters.mixin.bingo;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.task.Task;
import iskallia.vault.task.renderer.BingoRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Mixin(value = BingoRenderer.Root.class, remap = false)
public class MixinBingoRenderer {


    @ModifyArg(method="renderExpanded", at= @At(value = "INVOKE", target = "iskallia/vault/task/renderer/context/TaskRendererContext.drawColoredRect (FFFFI)V", ordinal = 0), index = 4)
    public int modifyBackgroundOpacity(int color){

        int configopacity = QOLHuntersClientConfigs.BINGO_GRID_BACKGROUND_OPACITY.get();

        int opacity = (int) configopacity * 255 / 100;

        return (opacity << 24);

    }


    @ModifyArg(method="renderExpanded", at= @At(value = "INVOKE", target = "iskallia/vault/task/renderer/context/TaskRendererContext.drawColoredRect (FFFFI)V", ordinal = 1), index = 4)
    public int modifySelectionColorExpanded(int color){

        return (QOLHuntersClientConfigs.BINGO_GRID_SELECTION_COLOR.get().getColorCode());

    }

    @ModifyArg(method="renderExpanded", at= @At(value = "INVOKE", target = "iskallia/vault/task/renderer/context/TaskRendererContext.drawColoredRect (FFFFI)V", ordinal = 2), index = 4)
    public int modifyCompletedColorExpanded(int color){

        return (QOLHuntersClientConfigs.BINGO_GRID_COMPLETED_COLOR.get().getColorCode());

    }

    @ModifyArg(method="renderCompact", at= @At(value = "INVOKE", target = "iskallia/vault/task/renderer/context/TaskRendererContext.drawColoredRect (FFFFI)V", ordinal = 2), index = 4)
    public int modifySelectionColorCompact(int color){
        return (QOLHuntersClientConfigs.BINGO_GRID_SELECTION_COLOR.get().getColorCode());
    }

    @ModifyConstant(method="renderCompact", constant = @Constant(intValue = 1677786880))
    public int modifyCompletedColorCompact(int color){
        return (QOLHuntersClientConfigs.BINGO_GRID_COMPLETED_COLOR.get().getColorCode());
    }

    @Redirect(method = "renderCompact", at = @At(value = "NEW", target = "()Ljava/util/HashMap;"))
    private HashMap<Integer, Task> sortedBingo(){
        return new LinkedHashMap<>(); // LinkedHashMap#forEach iterates in insertion order
    }
}
