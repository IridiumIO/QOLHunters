package io.iridium.qolhunters.mixin.jewelselection;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.screen.JewelPouchSelectionScreen;
import iskallia.vault.gear.GearScoreHelper;
import iskallia.vault.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = JewelPouchSelectionScreen.JewelSelection.class, remap = false)
public class MixinJewelSelection {
    @Inject(method = "renderButton", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), remap = true)
    private void addWeight(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci,
                           @Local(name = "components") List<Component> components, @Local(name = "renderStack") ItemStack renderStack) {
        if (QOLHuntersClientConfigs.AUTOCHOSEN_WEIGHT.get() && renderStack.getItem() == ModItems.JEWEL) {
            components.add(new TextComponent("w: " + GearScoreHelper.getWeight(renderStack)).withStyle(ChatFormatting.GOLD));
        }
    }
}
