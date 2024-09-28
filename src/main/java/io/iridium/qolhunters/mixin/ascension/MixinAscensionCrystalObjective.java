package io.iridium.qolhunters.mixin.ascension;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.crystal.objective.AscensionCrystalObjective;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = AscensionCrystalObjective.class, remap = false)
public class MixinAscensionCrystalObjective {
    @Shadow
    protected int stacks;

    @Inject(method = "addText", at = @At(value = "HEAD"))
    private void modifyStacksTooltip(List<Component> tooltip, int minIndex, TooltipFlag flag, float time, CallbackInfo ci) {
        if(QOLHuntersClientConfigs.ASCENSION_CRYSTAL_EMBER_GRANT_AMOUNT.get()) {
            tooltip.remove(tooltip.size() - 1);
            if (Screen.hasShiftDown()) {
                tooltip.add((new TextComponent("")).append((new TextComponent(" • ")).withStyle(ChatFormatting.GRAY)).append(new TextComponent("Stacks: ")).append((new TextComponent(this.stacks + " (" + ModConfigs.ASCENSION.getEmberCount(this.stacks) + " Embers)")).withStyle(ChatFormatting.AQUA)));

            } else {
                tooltip.add((new TextComponent("")).append((new TextComponent(" • ")).withStyle(ChatFormatting.GRAY)).append(new TextComponent("Stacks: ")).append((new TextComponent(String.valueOf(this.stacks))).withStyle(ChatFormatting.AQUA)));
            }
        }
    }
}