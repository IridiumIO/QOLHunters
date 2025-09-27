package io.iridium.qolhunters.mixin.temporalmodifiertimer;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import io.iridium.qolhunters.features.temporalmodifiertimer.TemporalModifierRenderer;
import iskallia.vault.core.vault.modifier.spi.VaultModifier;
import iskallia.vault.core.vault.overlay.ModifiersRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = ModifiersRenderer.class, remap = false)
public class MixinModifiersRenderer {

    @Shadow @Final private static MultiBufferSource.BufferSource TEXT_BUFFER;

    @Inject(method = "renderVaultModifiers(Ljava/util/Map;Lcom/mojang/blaze3d/vertex/PoseStack;ZZ)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;endVertex()V", ordinal = 3, shift = At.Shift.AFTER, remap = true))
    private static void renderTemporalTime(Map<VaultModifier<?>, Integer> group, PoseStack matrixStack, boolean depthTest, boolean useInvHudPositions,
                                           CallbackInfo ci,
                                           @Local(name = "amount") int amount,
                                           @Local(name = "minecraft")Minecraft minecraft,
                                           @Local(name = "matrix") Matrix4f matrix,
                                           @Local(name = "scale") float scale,
                                           @Local(name = "size") float size,
                                           @Local(name = "iconX") int iconX,
                                           @Local(name = "iconY") int iconY,
                                           @Local(name = "textOffsetX") float textOffsetX,
                                           @Local(name = "textOffsetY") float textOffsetY,
                                           @Local(name = "modifier") VaultModifier<?> modifier
                                           )
    {
        if (amount <= 1) {
            TemporalModifierRenderer.render(minecraft, matrix , scale, size, iconX, iconY, textOffsetX, textOffsetY, modifier, TEXT_BUFFER);
        }
    }

    @Inject(method = "renderVaultModifiers(Ljava/util/Map;Lcom/mojang/blaze3d/vertex/PoseStack;ZZ)V", at = @At(value = "FIELD", target = "Liskallia/vault/core/vault/overlay/ModifiersRenderer$ModifierTextRenderMode;BOX:Liskallia/vault/core/vault/overlay/ModifiersRenderer$ModifierTextRenderMode;", ordinal = 1))
    private static void renderTemporalTimeAfterAmount(Map<VaultModifier<?>, Integer> group, PoseStack matrixStack, boolean depthTest, boolean useInvHudPositions,
                                           CallbackInfo ci,
                                           @Local(name = "amount") int amount,
                                           @Local(name = "minecraft")Minecraft minecraft,
                                           @Local(name = "matrix") Matrix4f matrix,
                                           @Local(name = "scale") float scale,
                                           @Local(name = "size") float size,
                                           @Local(name = "iconX") int iconX,
                                           @Local(name = "iconY") int iconY,
                                           @Local(name = "textOffsetX") float textOffsetX,
                                           @Local(name = "textOffsetY") float textOffsetY,
                                           @Local(name = "modifier") VaultModifier<?> modifier
    )
    {
        TemporalModifierRenderer.render(minecraft, matrix , scale, size, iconX, iconY, textOffsetX, textOffsetY, modifier, TEXT_BUFFER);
    }
}
