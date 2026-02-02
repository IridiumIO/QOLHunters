package io.iridium.qolhunters.mixin.jewelselection;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.screen.block.UnboxingStationScreen;
import iskallia.vault.gear.GearScoreHelper;
import iskallia.vault.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = UnboxingStationScreen.CardSection.class, remap = false)
public class MixinCardSection {
    @Shadow @Final private ItemStack stack;

    @Inject(method = "renderButton", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), remap = true)
    private void addWeight(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci,
                           @Local(name = "coms") List<Component> coms) {
        if (QOLHuntersClientConfigs.AUTOCHOSEN_WEIGHT.get() && stack.getItem() == ModItems.JEWEL) {
            coms.add(new TextComponent("w: " + GearScoreHelper.getWeight(stack)).withStyle(ChatFormatting.GOLD));

        }
    }
}
