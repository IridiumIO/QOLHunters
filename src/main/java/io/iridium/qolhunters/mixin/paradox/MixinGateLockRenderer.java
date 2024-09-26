package io.iridium.qolhunters.mixin.paradox;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.block.GateLockBlock;
import iskallia.vault.block.entity.GateLockTileEntity;
import iskallia.vault.block.render.GateLockRenderer;
import iskallia.vault.client.ClientStatisticsData;
import iskallia.vault.config.VaultModifierOverlayConfig;
import iskallia.vault.core.vault.modifier.VaultModifierStack;
import iskallia.vault.core.vault.modifier.registry.VaultModifierRegistry;
import iskallia.vault.core.vault.overlay.ModifiersRenderer;
import iskallia.vault.init.ModConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(value = GateLockRenderer.class, remap = false)
public abstract class MixinGateLockRenderer {


    /**
     * @author
     * @reason
     */
    @Overwrite
    public void render(GateLockTileEntity entity, float pPartialTick, PoseStack matrices, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (entity.getGod() != null) {

            float scale = 0.010416667F;

            boolean isZoom = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT) && QOLHuntersClientConfigs.PARADOX_GATE_ZOOM.get();

            if (isZoom) {
                scale = 0.05F;
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.depthFunc(GL11.GL_ALWAYS);
                RenderSystem.disableCull();
            }

            BlockState blockstate = entity.getBlockState();
            matrices.pushPose();
            float f = 0.6666667F;
            Vec3i vector = ((Direction)blockstate.getValue(GateLockBlock.FACING)).getNormal();
            matrices.translate(0.5 + 0.9 * (double)vector.getX(), 0.5 + 0.9 * (double)vector.getY(), 0.5 + 0.9 * (double)vector.getZ());
            float f4 = -((Direction)blockstate.getValue(GateLockBlock.FACING)).toYRot();
            matrices.mulPose(Vector3f.YP.rotationDegrees(f4));
            matrices.translate(0.0, -0.3125, -0.4375);
            matrices.translate(0.0, 0.3333333432674408, 0.046666666865348816);


            matrices.scale(scale, -scale, scale);
            matrices.pushPose();
            matrices.pushPose();

            matrices.scale(2.0F, 2.0F, 2.0F);
            qol$renderLine((new TextComponent(entity.getName())).withStyle(Style.EMPTY.withColor(entity.getColor())), true, matrices, pBufferSource, pPackedLight, isZoom);
            matrices.popPose();
            Minecraft minecraft = Minecraft.getInstance();
            List<ItemStack> items = minecraft.player.inventoryMenu.getItems().stream().map(ItemStack::copy).toList();
            int count = entity.getModifiers().size();
            int reputation;
            if (count > 0) {
                matrices.translate(0.0, 30.0, 0.0);
                matrices.pushPose();
                int right = minecraft.getWindow().getGuiScaledWidth();
                reputation = minecraft.getWindow().getGuiScaledHeight();
                matrices.translate((double)(-right), (double)(-reputation), 0.0);
                VaultModifierOverlayConfig config = ModConfigs.VAULT_MODIFIER_OVERLAY;
                matrices.translate((double)config.spacingX + (double)((config.size + config.spacingX) * count) / 2.0, 0.0, 0.0);
                ModifiersRenderer.renderVaultModifiers(entity.getModifiers(), matrices);
                matrices.popPose();
            }

            AtomicInteger index = new AtomicInteger(1);
            Iterator var19 = entity.getModifiers().iterator();

            while(var19.hasNext()) {
                VaultModifierStack stack = (VaultModifierStack)var19.next();
                VaultModifierRegistry.getOpt(stack.getModifierId()).ifPresent((modifier) -> {

                    matrices.pushPose();
                    matrices.translate(0.0, 10.0 * (double)index.get(), 0.0);
                    qol$renderLine(modifier.getChatDisplayNameComponent(stack.getSize()), true, matrices, pBufferSource, pPackedLight, isZoom);
                    matrices.popPose();
                    index.getAndIncrement();
                });
            }

            if (entity.getReputationCost() > 0) {
                matrices.translate(0.0, 10.0 * (double)index.get(), 0.0);
                matrices.pushPose();

                reputation = ClientStatisticsData.getReputation(entity.getGod());
                ChatFormatting form = reputation >= entity.getReputationCost() ? ChatFormatting.WHITE : ChatFormatting.RED;
                qol$renderLine((new TextComponent("")).append((new TextComponent(entity.getReputationCost() + " ")).withStyle(form)).append((new TextComponent(entity.getGod().getName())).withStyle(entity.getGod().getChatColor())).append((new TextComponent(" Reputation")).withStyle(form)), true, matrices, pBufferSource, pPackedLight, isZoom);
                matrices.popPose();
            }

            var19 = entity.getCost().iterator();

            while(var19.hasNext()) {

                ItemStack stack = (ItemStack)var19.next();
                matrices.translate(0.0, 45.0, 0.0);
                ChatFormatting color = this.check(items, stack.copy(), true) && this.check(items, stack.copy(), false) ? ChatFormatting.WHITE : ChatFormatting.RED;
                MutableComponent var10002 = stack.getHoverName().copy().withStyle(color);
                String var10005 = stack.getCount() < 10 ? " " : "";
                qol$renderItemLine(stack, var10002, (new TextComponent(var10005 + stack.getCount())).withStyle(color), true, matrices, pBufferSource, pPackedLight, isZoom);
            }

            matrices.popPose();
            matrices.popPose();

            if (isZoom) {
                RenderSystem.depthFunc(GL11.GL_LEQUAL);
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.enableCull();
            }
        }
    }

    @Shadow
    protected abstract boolean check(List<ItemStack> items, ItemStack stack, boolean remove);


    @Shadow @Final
    private Font font;

    @Shadow @Final
    private ItemRenderer itemRenderer;


    public void qol$renderLine(Component component, boolean centered, PoseStack matrices, MultiBufferSource source, int light, boolean zoom){
        FormattedCharSequence formatted = (FormattedCharSequence)this.font.split(component, 9000).get(0);
        float offsetX = centered ? (float)((double)(-this.font.width(formatted)) / 2.0) : 0.0F;
        float var10000;
        if (centered) {
            Objects.requireNonNull(this.font);
            var10000 = (float)((double)(-9) / 2.0);
        } else {
            var10000 = 0.0F;
        }

        float offsetY = var10000;
        if(!zoom) RenderSystem.enableDepthTest();
        this.font.drawInBatch(formatted, offsetX, offsetY, 16777215, false, matrices.last().pose(), source, true, 0, light);
        if(!zoom) RenderSystem.enableDepthTest();
        this.font.drawInBatch(formatted, offsetX, offsetY, -1, false, matrices.last().pose(), source, false, 0, light);
    }



    public void qol$renderItemLine(ItemStack stack, Component text, Component count, boolean centered, PoseStack matrices, MultiBufferSource source, int light, boolean zoom) {
        FormattedCharSequence formatted = (FormattedCharSequence) this.font.split(text, 9000).get(0);
        float offsetX = centered ? (float) ((double) (-this.font.width(formatted)) / 2.0) + 15.0F : 0.0F;
        float var10000;
        if (centered) {
            Objects.requireNonNull(this.font);
            var10000 = (float) ((double) (-9) / 2.0);
        } else {
            var10000 = 0.0F;
        }

        float offsetY = var10000;
        if(!zoom) RenderSystem.enableDepthTest();
        this.font.drawInBatch(formatted, offsetX, offsetY, 16777215, false, matrices.last().pose(), source, true, 0, light);
        if(!zoom) RenderSystem.enableDepthTest();
        this.font.drawInBatch(formatted, offsetX, offsetY, -1, false, matrices.last().pose(), source, false, 0, light);
        matrices.pushPose();
        matrices.translate((double) (offsetX - 18.0F), -2.0, -0.4000000059604645);
        matrices.scale(24.0F, -24.0F, 1.0F);
        this.itemRenderer.renderStatic(stack, ItemTransforms.TransformType.GUI, light, OverlayTexture.NO_OVERLAY, matrices, source, light);
        matrices.scale(1.0F, -1.0F, 0.4F);
        matrices.translate(0.0, 0.0, 0.20000000298023224);
        formatted = (FormattedCharSequence) this.font.split(count, 9000).get(0);
        matrices.scale(0.045454547F, 0.045454547F, 1.0F);
        if(!zoom) RenderSystem.enableDepthTest();
        this.font.drawInBatch(formatted, 1.0F, offsetY + 8.0F, 16777215, false, matrices.last().pose(), source, true, 0, light);
        if(!zoom) RenderSystem.enableDepthTest();
        this.font.drawInBatch(formatted, 1.0F, offsetY + 8.0F, -1, false, matrices.last().pose(), source, false, 0, light);
        matrices.popPose();

    }

}
