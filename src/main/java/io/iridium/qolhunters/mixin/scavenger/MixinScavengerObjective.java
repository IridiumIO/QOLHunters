package io.iridium.qolhunters.mixin.scavenger;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.util.SharedFunctions;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.util.ClientScheduler;
import iskallia.vault.core.data.key.FieldKey;
import iskallia.vault.core.vault.objective.ScavengerObjective;
import iskallia.vault.core.vault.objective.scavenger.ScavengerGoal;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.lang.reflect.Field;
import java.util.*;

@Mixin(ScavengerObjective.class)
public class MixinScavengerObjective {

    @Shadow(remap = false)
    private static void renderItemStack(PoseStack renderStack, ItemStack item, int totalX, int totalY) {
    }


    /**
     * @author df
     * @reason d d
     */

    @OnlyIn(Dist.CLIENT) @Overwrite(remap = false)
    private static int renderItemRequirement(PoseStack matrixStack, ScavengerGoal goal, int itemBoxWidth, int totalX, int totalY, float partialTicks) throws NoSuchFieldException {
        List<ScavengerGoal.Entry> entries = new ArrayList<>();
        Iterator<ScavengerGoal.Entry> var10000 = goal.getEntries();
        Objects.requireNonNull(entries);
        var10000.forEachRemaining(entries::add);
        float time = (float) ClientScheduler.INSTANCE.getTickCount() + partialTicks;
        ScavengerGoal.Entry entry = (ScavengerGoal.Entry)entries.get((int)(time / 20.0F) % entries.size());
        ItemStack requiredStack = entry.getStack();
        ResourceLocation iconPath = entry.getIcon();
        matrixStack.pushPose();
        matrixStack.translate(0.0, (double)((float)(-itemBoxWidth) / 2.0F), 0.0);
        totalY = (int)((float)totalY + (float)(-itemBoxWidth) / 2.0F);
        renderItemStack(matrixStack, requiredStack, totalX, totalY);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, iconPath);
        matrixStack.pushPose();
        matrixStack.translate(-16.0, -2.4, 0.0);
        matrixStack.scale(0.4F, 0.4F, 1.0F);
        ScreenDrawHelper.drawTexturedQuads((buf) -> {
            ScreenDrawHelper.rect(buf, matrixStack).dim(16.0F, 16.0F).draw();
        });
        matrixStack.popPose();
        matrixStack.translate(0.0, 10.0, 0.0);
        Object var16 = goal.get(ScavengerGoal.CURRENT);


        Integer inventoryItems = SharedFunctions.GetPlayerInventoryItemCount(Minecraft.getInstance().player, requiredStack.getItem(), 500);

        String requiredText = var16 + "/" + goal.get(ScavengerGoal.TOTAL);
        MutableComponent cmp = (new TextComponent(requiredText)).withStyle(ChatFormatting.GREEN);

        UIHelper.renderCenteredWrappedText(matrixStack, cmp, 35, 0);


        if (inventoryItems > 0){
            matrixStack.translate(0.0, 10.0, 0.0);
            matrixStack.pushPose();
            matrixStack.scale(0.8F, 0.8F, 1.0F);
            MutableComponent c2 = new TextComponent(  "(" + inventoryItems + ")").withStyle(ChatFormatting.GRAY);
            UIHelper.renderCenteredWrappedText(matrixStack, c2, 35, 0);
            matrixStack.popPose();
        }


        matrixStack.translate(0.0, 8.0, 0.0);
        matrixStack.pushPose();
        matrixStack.scale(0.5F, 0.5F, 1.0F);

        Component name = requiredStack.getHoverName();
        MutableComponent display = name.copy().withStyle(Style.EMPTY.withColor(entry.getColor()));
        int lines = UIHelper.renderCenteredWrappedText(matrixStack, display, 60, 0);

        matrixStack.popPose();
        matrixStack.popPose();
        return 25 + lines * 5;
    }

}
