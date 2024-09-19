package io.iridium.qolhunters.mixin.scavenger;

import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.customimplementations.Scavenger;
import io.iridium.qolhunters.util.SharedFunctions;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.core.vault.objective.ScavengerObjective;
import iskallia.vault.core.vault.objective.scavenger.ScavengerGoal;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ScavengerObjective.class)
public class MixinScavengerObjective {


    @Inject(method = "renderItemRequirement", at = @At(value = "INVOKE",
            target ="Liskallia/vault/client/gui/helper/UIHelper;renderCenteredWrappedText(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/network/chat/Component;II)I", shift = At.Shift.AFTER),
            cancellable = true, remap = false, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void renderItemRequirement(PoseStack matrixStack, ScavengerGoal goal, int itemBoxWidth, int totalX, int totalY, float partialTicks, CallbackInfoReturnable<Integer> cir, List<ScavengerGoal.Entry> entries, float time, ScavengerGoal.Entry entry, ItemStack requiredStack, ResourceLocation iconPath, String requiredText, MutableComponent cmp) throws NoSuchFieldException {

        if(!Scavenger.ScavengerItems.containsKey(requiredStack.getItem())){
            Scavenger.ScavengerItems.put(requiredStack.getItem(), entry.getColor());
        }

        if(QOLHuntersClientConfigs.SCAVENGER_INV_COUNT.get()) {
            Integer inventoryItems = SharedFunctions.GetPlayerInventoryItemCount(Minecraft.getInstance().player, requiredStack.getItem(), 500);

            if (inventoryItems > 0) {
                matrixStack.translate(0.0, 10.0, 0.0);
                matrixStack.pushPose();
                matrixStack.scale(0.8F, 0.8F, 1.0F);
                MutableComponent c2 = new TextComponent("(" + inventoryItems + ")").withStyle(ChatFormatting.GRAY);
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
            cir.setReturnValue(25 + lines * 5);
        }
    }

}
