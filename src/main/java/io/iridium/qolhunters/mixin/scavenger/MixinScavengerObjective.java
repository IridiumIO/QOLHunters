package io.iridium.qolhunters.mixin.scavenger;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.vault_scavenger.Scavenger;
import io.iridium.qolhunters.features.vault_scavenger.NamedItem;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.objective.ScavengerObjective;
import iskallia.vault.core.vault.objective.scavenger.ScavengerGoal;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ScavengerObjective.class, remap = false)
public class MixinScavengerObjective {


    @Inject(method = "renderItemRequirement", at = @At(value = "INVOKE",
        target = "Liskallia/vault/client/gui/helper/UIHelper;renderCenteredWrappedText(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/network/chat/Component;II)I", shift = At.Shift.AFTER), cancellable = true)
    private static void renderItemRequirement(PoseStack matrixStack, ScavengerGoal goal, int itemBoxWidth, int totalX, int totalY, float partialTicks, Player player, CallbackInfoReturnable<Integer> cir,
                                              @Local(name = "entry") ScavengerGoal.Entry entry, @Local(name = "requiredStack") ItemStack requiredStack){

        NamedItem scavItem = NamedItem.of(requiredStack);
        if(!Scavenger.scavengerColors.containsKey(scavItem)){
            Scavenger.scavengerColors.put(scavItem, entry.getColor());
        }

        if(QOLHuntersClientConfigs.SCAVENGER_INV_COUNT.get()) {
            Integer inventoryItems = Scavenger.getPlayerInventoryItemCount(Minecraft.getInstance().player, scavItem, 500);

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

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0))
    private void removeCompletedGoals(Vault vault, PoseStack matrixStack, Window window, float partialTicks, Player player, CallbackInfoReturnable<Boolean> cir, @Local(name = "filteredGoals") List<ScavengerGoal> filteredGoals){
        Scavenger.updateGoals(filteredGoals.size());
    }

}
