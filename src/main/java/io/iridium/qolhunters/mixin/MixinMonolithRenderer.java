package io.iridium.qolhunters.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.block.MonolithBlock;
import iskallia.vault.block.entity.MonolithTileEntity;
import iskallia.vault.block.render.MonolithRenderer;
import iskallia.vault.core.vault.modifier.VaultModifierStack;
import iskallia.vault.core.vault.modifier.registry.VaultModifierRegistry;
import iskallia.vault.core.vault.overlay.ModifiersRenderer;
import iskallia.vault.task.renderer.context.RendererContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Mixin(MonolithRenderer.class)
public class MixinMonolithRenderer {

    @Shadow(remap = false) @Final
    private Font font;

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void render(MonolithTileEntity tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlayIn) {
        RenderSystem.enableDepthTest();
        matrixStack.pushPose();

        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        Vec3 tilePos = new Vec3(tileEntity.getBlockPos().getX() + 0.5, tileEntity.getBlockPos().getY() + 0.5, tileEntity.getBlockPos().getZ() + 0.5);
        double distance = cameraPos.distanceTo(tilePos);

        float minDistance = 0.0F;
        float maxDistance = 28.0F;
        float minScale = 0.02F;
        float maxScale = 0.07F;

        float scale = minScale + (maxScale - minScale) * ((float)distance - minDistance) / (maxDistance - minDistance);
        scale = Math.max(minScale, Math.min(scale, maxScale));
        matrixStack.translate(0.5, 2.5, 0.5);
        matrixStack.scale(scale, scale, scale);
        matrixStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.translate(-65.0, -11.0 * scale/0.02F - (2.5F * scale/0.02F), 0.0);
        RendererContext context = new RendererContext(matrixStack, partialTicks, MultiBufferSource.immediate(Tesselator.getInstance().getBuilder()), this.font);
        MonolithBlock.State state = (MonolithBlock.State)tileEntity.getBlockState().getValue(MonolithBlock.STATE);
        List<Component> lines = new ArrayList();
        List<Component> description = new ArrayList();
        List<VaultModifierStack> stack = new ArrayList();
        if (tileEntity.isOverStacking() && state == MonolithBlock.State.EXTINGUISHED) {
            lines.add((new TextComponent("Pillage for Loot")).withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));

        }

        tileEntity.getModifiers().forEach((id, count) -> {
            VaultModifierRegistry.getOpt(id).ifPresent((modifier) -> {

                if (QOLHuntersClientConfigs.BRAZIER_HOLOGRAM_MODE.get() == QOLHuntersClientConfigs.BrazierHologramMode.DEFAULT) {
                    lines.add(modifier.getChatDisplayNameComponent(count));
                    description.add((new TextComponent(modifier.getDisplayDescriptionFormatted(count))).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));

                }else if (QOLHuntersClientConfigs.BRAZIER_HOLOGRAM_MODE.get() == QOLHuntersClientConfigs.BrazierHologramMode.MODE1) {
                    if (distance < 12) {
                        lines.add(modifier.getChatDisplayNameComponent(count));
                        description.add((new TextComponent(modifier.getDisplayDescriptionFormatted(count))).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));

                    }

                }else if (QOLHuntersClientConfigs.BRAZIER_HOLOGRAM_MODE.get() == QOLHuntersClientConfigs.BrazierHologramMode.MODE2) {
                    if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT)) {
                        lines.add(modifier.getChatDisplayNameComponent(count));
                        description.add((new TextComponent(modifier.getDisplayDescriptionFormatted(count))).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));

                    }
                }else if (QOLHuntersClientConfigs.BRAZIER_HOLOGRAM_MODE.get() == QOLHuntersClientConfigs.BrazierHologramMode.MODE3) {
                    lines.add(modifier.getChatDisplayNameComponent(count));
                    if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT)) {
                        description.add((new TextComponent(modifier.getDisplayDescriptionFormatted(count))).withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
                    }
                }



                stack.add(VaultModifierStack.of(modifier, count));
            });
        });
        Collections.reverse(lines);
        Collections.reverse(stack);
        Iterator var13 = description.iterator();

        Component line;
        MutableComponent shadow;
        while(var13.hasNext()) {
            line = (Component)var13.next();
            shadow = (new TextComponent("")).append(line.getString()).withStyle(Style.EMPTY.withColor(ChatFormatting.BLACK));
            context.renderText(shadow, 66.0F, 68.0F, true, true);
            context.renderText(line, 65.0F, 67.0F, true, true);
            context.translate(0.0, -11.0, 0.0);
        }

        var13 = lines.iterator();

        while(var13.hasNext()) {
            line = (Component)var13.next();
            shadow = (new TextComponent("")).append(line.getString()).withStyle(Style.EMPTY.withColor(ChatFormatting.BLACK));
            context.renderText(shadow, 66.0F, 66.0F, true, true);
            context.renderText(line, 65.0F, 65.0F, true, true);
            context.translate(0.0, -11.0, 0.0);
        }

        Minecraft minecraft = Minecraft.getInstance();
        double xTranslation = stack.size() > 1 ? 82.5 + (double)(stack.size() * 5) : 82.5;
        matrixStack.translate(xTranslation, 73.0, 0.0);
        matrixStack.pushPose();
        int right = minecraft.getWindow().getGuiScaledWidth();
        int bottom = minecraft.getWindow().getGuiScaledHeight();
        matrixStack.translate((double)(-right), (double)(-bottom), 0.0);
        ModifiersRenderer.renderVaultModifiersWithDepth(stack, matrixStack);
        matrixStack.popPose();
        matrixStack.popPose();
    }

}
