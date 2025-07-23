package io.iridium.qolhunters.mixin.vaultmodifiers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.vaultmodifiertextoverlays.VaultModifierOverlays;
import iskallia.vault.client.atlas.ITextureAtlas;
import iskallia.vault.config.VaultModifierOverlayConfig;
import iskallia.vault.core.vault.modifier.spi.VaultModifier;
import iskallia.vault.core.vault.overlay.ModifiersRenderer;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModTextureAtlases;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Map;
import java.util.Optional;

@Mixin(value = ModifiersRenderer.class, remap = false)
public class MixinModifiersRendererScreenPos {


    @Final @Shadow private static ModifiersRenderer.ModifierTextRenderMode MODIFIER_TEXT_RENDER_MODE;
    @Final @Shadow private static float BG_R;
    @Final @Shadow private static float BG_G;
    @Final @Shadow private static float BG_B;
    @Final @Shadow private static float BG_A;
    @Final @Shadow private static BufferBuilder ICON_BUFFER;
    @Final @Shadow private static BufferBuilder BOX_BUFFER;
    @Final @Shadow private static MultiBufferSource.BufferSource TEXT_BUFFER;
    @Final @Shadow public static Vector3f SHADOW_OFFSET;


    // TODO: is this vanilla vh feat?
    @Inject(method="renderVaultModifiers(Ljava/util/Map;Lcom/mojang/blaze3d/vertex/PoseStack;ZZ)V", at=@At("HEAD"), cancellable = true)
    private static void renderVaultModifiersScreenPos(Map<VaultModifier<?>, Integer> group, PoseStack matrixStack, boolean depthTest, boolean useInvHudPositions, CallbackInfo ci) {

        if(QOLHuntersClientConfigs.VAULT_MODIFIERS_TOP_RIGHT.get()){

            Minecraft minecraft = Minecraft.getInstance();
            int right = minecraft.getWindow().getGuiScaledWidth();
            int top = 0;
            VaultModifierOverlayConfig config = ModConfigs.VAULT_MODIFIER_OVERLAY;

            ITextureAtlas textureAtlas = ModTextureAtlases.MODIFIERS.get();
            Matrix4f matrix = matrixStack.last().pose();
            if (!ICON_BUFFER.building()) {
                ICON_BUFFER.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            }

            if (MODIFIER_TEXT_RENDER_MODE == ModifiersRenderer.ModifierTextRenderMode.BOX && !BOX_BUFFER.building()) {
                BOX_BUFFER.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            }

            int index = 0;

            for (Map.Entry<VaultModifier<?>, Integer> entry : group.entrySet()) {
                VaultModifier<?> modifier = entry.getKey();
                int amount = entry.getValue();
                Optional<ResourceLocation> icon = VaultModifierOverlays.getModifierIcon(modifier);
                if (!icon.isEmpty()) {
                    TextureAtlasSprite sprite = textureAtlas.getSprite(icon.get());
                    int iconOffsetX = (config.size + config.spacingX) * (index % config.columns);
                    int iconOffsetY = (config.size + config.spacingY) * (index / config.columns);
                    int iconX = right - (config.rightMargin + config.size) - iconOffsetX;
                    int iconY = top + config.bottomMargin + iconOffsetY;
                    ICON_BUFFER.vertex(matrix, (float)iconX, (float)iconY + (float)config.size, 0.0F).uv(sprite.getU0(), sprite.getV1()).endVertex();
                    ICON_BUFFER.vertex(matrix, (float)iconX + (float)config.size, (float)iconY + (float)config.size, 0.0F)
                            .uv(sprite.getU1(), sprite.getV1())
                            .endVertex();
                    ICON_BUFFER.vertex(matrix, (float)iconX + (float)config.size, (float)iconY, 0.0F).uv(sprite.getU1(), sprite.getV0()).endVertex();
                    ICON_BUFFER.vertex(matrix, (float)iconX, (float)iconY, 0.0F).uv(sprite.getU0(), sprite.getV0()).endVertex();
                    if (amount > 1) {
                        String textString = "x" + amount;
                        int textWidth = minecraft.font.width(textString);
                        if (MODIFIER_TEXT_RENDER_MODE != ModifiersRenderer.ModifierTextRenderMode.NONE) {
                            minecraft.font
                                    .drawInBatch(
                                            textString,
                                            (float)(iconX + config.size - textWidth + 4),
                                            (float)(iconY + config.size - 9 + 2),
                                            Color.WHITE.getRGB(),
                                            false,
                                            matrix,
                                            TEXT_BUFFER,
                                            false,
                                            0,
                                            15728880
                                    );
                        }

                        if (MODIFIER_TEXT_RENDER_MODE == ModifiersRenderer.ModifierTextRenderMode.OUTLINE) {
                            Matrix4f outlineMatrix = matrix.copy();
                            outlineMatrix.translate(SHADOW_OFFSET);

                            for (int x = -1; x <= 1; x++) {
                                for (int y = -1; y <= 1; y++) {
                                    if (x != 0 || y != 0) {
                                        minecraft.font
                                                .drawInBatch(
                                                        textString,
                                                        (float)(x + iconX + config.size - textWidth + 4),
                                                        (float)(y + iconY + config.size - 9 + 2),
                                                        Color.BLACK.getRGB(),
                                                        false,
                                                        outlineMatrix,
                                                        TEXT_BUFFER,
                                                        false,
                                                        0,
                                                        15728880
                                                );
                                    }
                                }
                            }
                        }

                        if (MODIFIER_TEXT_RENDER_MODE == ModifiersRenderer.ModifierTextRenderMode.SHADOW) {
                            Matrix4f outlineMatrix = matrix.copy();
                            outlineMatrix.translate(SHADOW_OFFSET);
                            minecraft.font
                                    .drawInBatch(
                                            textString,
                                            (float)(1 + iconX + config.size - textWidth + 4),
                                            (float)(1 + iconY + config.size - 9 + 2),
                                            Color.BLACK.getRGB(),
                                            false,
                                            outlineMatrix,
                                            TEXT_BUFFER,
                                            false,
                                            0,
                                            15728880
                                    );
                        }

                        if (MODIFIER_TEXT_RENDER_MODE == ModifiersRenderer.ModifierTextRenderMode.BOX) {
                            float minX = (float)(iconX + config.size - textWidth - 1 + 4);
                            float minY = (float)(iconY + config.size - 9 - 1 + 2);
                            float maxX = (float)(iconX + config.size + 4);
                            float maxY = (float)(iconY + config.size - 1 + 2);
                            BOX_BUFFER.vertex(matrix, minX, maxY, 0.0F).color(BG_R, BG_G, BG_B, BG_A).endVertex();
                            BOX_BUFFER.vertex(matrix, maxX, maxY, 0.0F).color(BG_R, BG_G, BG_B, BG_A).endVertex();
                            BOX_BUFFER.vertex(matrix, maxX, minY, 0.0F).color(BG_R, BG_G, BG_B, BG_A).endVertex();
                            BOX_BUFFER.vertex(matrix, minX, minY, 0.0F).color(BG_R, BG_G, BG_B, BG_A).endVertex();
                        }
                    }

                    index++;
                }
            }

            RenderSystem.enableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            if (!depthTest) {
                RenderSystem.disableDepthTest();
            }

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, textureAtlas.getAtlasResourceLocation());
            ICON_BUFFER.end();
            BufferUploader.end(ICON_BUFFER);
            if (MODIFIER_TEXT_RENDER_MODE == ModifiersRenderer.ModifierTextRenderMode.BOX) {
                RenderSystem.disableTexture();
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                BOX_BUFFER.end();
                BufferUploader.end(BOX_BUFFER);
            }

            if (MODIFIER_TEXT_RENDER_MODE != ModifiersRenderer.ModifierTextRenderMode.NONE) {
                TEXT_BUFFER.endBatch();
            }

            ci.cancel();

        }

    }



}
