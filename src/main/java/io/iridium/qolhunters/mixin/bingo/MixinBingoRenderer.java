package io.iridium.qolhunters.mixin.bingo;

import com.mojang.blaze3d.platform.Window;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.task.BingoTask;
import iskallia.vault.task.renderer.BingoRenderer;
import iskallia.vault.task.renderer.context.BingoRendererContext;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(value = BingoRenderer.Root.class, remap = false)
public class MixinBingoRenderer {


    @ModifyArg(method="renderExpanded", at= @At(value = "INVOKE", target = "iskallia/vault/task/renderer/context/BingoRendererContext.drawColoredRect (FFFFI)V", ordinal = 0), index = 4)
    public int modifyBackgroundOpacity(int color){

        int configopacity = QOLHuntersClientConfigs.BINGO_GRID_BACKGROUND_OPACITY.get();

        int opacity = (int) configopacity * 255 / 100;

        return (opacity << 24);

    }


    @ModifyArg(method="renderExpanded", at= @At(value = "INVOKE", target = "iskallia/vault/task/renderer/context/BingoRendererContext.drawColoredRect (FFFFI)V", ordinal = 1), index = 4)
    public int modifySelectionColor(int color){

        return (QOLHuntersClientConfigs.BINGO_GRID_SELECTION_COLOR.get().getColorCode());

    }

    @ModifyArg(method="renderExpanded", at= @At(value = "INVOKE", target = "iskallia/vault/task/renderer/context/BingoRendererContext.drawColoredRect (FFFFI)V", ordinal = 2), index = 4)
    public int modifyCompletedColor(int color){

        return (QOLHuntersClientConfigs.BINGO_GRID_COMPLETED_COLOR.get().getColorCode());

    }

    @Inject(method="renderCompact", at= @At(value = "INVOKE", target = "iskallia/vault/task/renderer/context/BingoRendererContext.drawColoredRect (FFFFI)V", ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void modifyColor4(BingoTask root, BingoRendererContext context, CallbackInfo ci, Minecraft minecraft, Window window, Map children, int sizeX, int spanX, float gridHeight, float gridWidth, float cellWidth, float cellHeight){


        for (int column = 0; column < root.getWidth(); column++) {
            context.push();

            for (int row = 0; row < root.getHeight(); row++) {
                context.drawColoredRect(
                        1.0F, 1.0F, cellWidth - 1.5F, cellHeight - 1.5F, root.getState(row, column) == BingoTask.State.INCOMPLETE ? 369098751 : QOLHuntersClientConfigs.BINGO_GRID_COMPLETED_COLOR.get().getColorCode()
                );

                for (int index : root.getSelectedLine(context.getUuid())) {
                    if (root.getIndex(row, column) == index) {
                        context.drawColoredRect(0.25F, 0.25F, cellWidth, cellHeight, QOLHuntersClientConfigs.BINGO_GRID_SELECTION_COLOR.get().getColorCode());
                        break;
                    }
                }

                context.translate(0.0, (double)cellHeight, 0.0);
            }

            context.pop();
            context.translate((double)cellWidth, 0.0, 0.0);
        }

        context.pop();

        ci.cancel();

    }

}
