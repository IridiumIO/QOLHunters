package io.iridium.qolhunters.features.temporalmodifiertimer;

import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.core.vault.modifier.spi.VaultModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;

import java.awt.Color;
import java.util.Objects;

public class TemporalModifierRenderer {
    public static void render(
        Minecraft minecraft,
        Matrix4f matrix,
        float scale,
        float size,
        int iconX,
        int iconY,
        float textOffsetX,
        float textOffsetY,
        VaultModifier<?> modifier,
        MultiBufferSource.BufferSource textBuffer
    ) {
        if (QOLHuntersClientConfigs.TEMPORAL_MODIFIER_TIMER_ENABLE.get() && modifier instanceof VaultModifierWithTime modWithCtx) {
            scale = scale * QOLHuntersClientConfigs.TEMPORAL_MODIFIER_TIMER_SCALE.get();
            var time = modWithCtx.qOLHunters$getTime();
            if (time == null) {
                return;
            }
            long seconds = time / 20L % 60L;
            long minutes = time / 20L / 60L % 60L;
            long hours = time / 20L / 60L / 60L;
            String textString = hours > 0L ? String.format("%2d:%02d:%02d", hours, minutes, seconds)  : String.format("%d:%02d", minutes, seconds);
            int textWidth = (int) (minecraft.font.width(textString) * scale);

            Matrix4f shadowMatrix = matrix.copy();
            shadowMatrix.multiply(Matrix4f.createScaleMatrix(scale, scale, 1.0F));
            shadowMatrix.translate(new Vector3f(0.0F, 0.0F, 1.0F));
            Font var47 = minecraft.font;
            // iconLeft + ((iconWidth - textWidth) / 2)
            float x = (scale + iconX +((size - textWidth)/2) + QOLHuntersClientConfigs.TEMPORAL_MODIFIER_TIMER_X_OFFSET.get()) / scale;
            float y = ((scale + iconY + size) - 9.0F * scale + textOffsetY + QOLHuntersClientConfigs.TEMPORAL_MODIFIER_TIMER_Y_OFFSET.get() * scale) / scale;
            Objects.requireNonNull(minecraft.font);
            var47.drawInBatch(textString, x, y , Color.PINK.getRGB(), true, shadowMatrix, textBuffer, false, 0, 15728880);
        }
    }
}
