package io.iridium.qolhunters.mixin.cake;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.core.vault.objective.CakeObjective;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = CakeObjective.class, remap = false)
public abstract class MixinCakeObjective {

    @Unique
    private static final ResourceLocation CAKE_PING_LEFT = ResourceLocation.fromNamespaceAndPath(QOLHunters.MOD_ID, "textures/gui/cake/ping.png");

    @ModifyVariable(method = "renderVignette", at = @At("LOAD"), argsOnly = true)
    private TextColor modifyColor(TextColor color) {
        return TextColor.fromRgb(QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_COLOR.get().getColorCode());
    }

    @Inject(method = "renderVignette", at = @At("HEAD"), cancellable = true)
    private void renderCakeRadarInsteadOfVignette(TextColor color, float alpha, int width, int height, CallbackInfo ci) {
        if (QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_STYLE.get() != QOLHuntersClientConfigs.CakeVaultOverlayStyle.RADAR) {
            return;
        }
        ci.cancel();

        color = TextColor.fromRgb(QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_COLOR.get().getColorCode());
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        int colorValue = color.getValue();
        float b = (colorValue & 255) / 255.0F;
        float g = (colorValue >> 8 & 255) / 255.0F;
        float r = (colorValue >> 16 & 255) / 255.0F;

        RenderSystem.setShaderColor(r, g, b, 0.9F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, CAKE_PING_LEFT);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        int numberOfCopies = alpha > 0.6 ? 3 :
                             alpha > 0.4 ? 2 :
                             alpha > 0.15 ? 1 : 0;
        double newWidth = 28;
        double newHeight = 28;

        for (int i = 0; i < numberOfCopies; i++) {
            double XLeft = width / 2F - 48.0F - i * 12;
            double XRight = width / 2F + 17.0F + i * 12;
            double newY = 3.6F;

            //Left
            bufferbuilder.vertex(XLeft, newY + newHeight, -90.0).uv(0.0F, 1.0F).endVertex();
            bufferbuilder.vertex(XLeft + newWidth, newY + newHeight, -90.0).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(XLeft + newWidth, newY, -90.0).uv(1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(XLeft, newY, -90.0).uv(0.0F, 0.0F).endVertex();

            // Right
            bufferbuilder.vertex(XRight, newY + newHeight, -90.0).uv(1.0F, 1.0F).endVertex();
            bufferbuilder.vertex(XRight + newWidth, newY + newHeight, -90.0).uv(0.0F, 1.0F).endVertex();
            bufferbuilder.vertex(XRight + newWidth, newY, -90.0).uv(0.0F, 0.0F).endVertex();
            bufferbuilder.vertex(XRight, newY, -90.0).uv(1.0F, 0.0F).endVertex();

        }
        tesselator.end();

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }
}
