package io.iridium.qolhunters.mixin.ascension;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.SoulFlameItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = SoulFlameItem.class)
public class MixinSoulFlameItem {
    @OnlyIn(Dist.CLIENT)
    @Inject(method = "appendHoverText", at = @At("TAIL"))
    private void appendEmberCountText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flag, CallbackInfo ci) {
        if (QOLHuntersClientConfigs.CHALLENGER_ROCK_EMBER_GRANT_AMOUNT.get()) {
            tooltip.remove(tooltip.size() - 1);
            if (Screen.hasShiftDown()) {
                tooltip.add((new TextComponent("")).append(new TextComponent("Stacks: ")).append((new TextComponent("" + SoulFlameItem.getStacks(stack))).withStyle(ChatFormatting.AQUA)).append((new TextComponent(" (" + ModConfigs.ASCENSION.getEmberCount(SoulFlameItem.getStacks(stack)) + " Embers)")).withStyle(ChatFormatting.AQUA)));
            } else {
                tooltip.add((new TextComponent("")).append(new TextComponent("Stacks: ")).append((new TextComponent("" + SoulFlameItem.getStacks(stack))).withStyle(ChatFormatting.AQUA)));
            }
        }
    }
}