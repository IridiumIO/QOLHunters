package io.iridium.qolhunters.mixin.bettersoulvalue;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.util.SharedFunctions;
import iskallia.vault.config.VaultDiffuserConfig;
import iskallia.vault.event.ClientEvents;
import iskallia.vault.init.ModConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@OnlyIn(Dist.CLIENT)
@Mixin(value = ClientEvents.class, remap = false)
public class MixinClientEvents {


    @WrapOperation(method = "onItemTooltip", at = @At(value = "INVOKE", target = "Liskallia/vault/config/VaultDiffuserConfig;contains(Lnet/minecraft/world/item/ItemStack;)Z"))
    private static boolean betterSoulValue(VaultDiffuserConfig instance, ItemStack current, Operation<Boolean> original, @Local(argsOnly = true) ItemTooltipEvent event) {
        var ret = original.call(instance, current);
        if (ret && QOLHuntersClientConfigs.BETTER_SOUL_VALUE.get()) {
            int value = ModConfigs.VAULT_DIFFUSER.getDiffuserOutputMap().get(current.getItem().getRegistryName());
            if (value <= 0 || !Screen.hasShiftDown()) return true; // no stack value

            final String stackSoulValueText;
            if (QOLHuntersClientConfigs.BETTER_SOUL_VALUE_USE_SHARDS.get()) {
                if (QOLHuntersClientConfigs.BETTER_SOUL_VALUE_SHORTHAND.get()) {
                    stackSoulValueText = SharedFunctions.formatNumberWithDecimal(current.getCount() * value / 9.0) + " shards";
                } else {
                    stackSoulValueText = String.format("%.0f", current.getCount() * value / 9.0) + " shards";
                }
            } else {
                if (QOLHuntersClientConfigs.BETTER_SOUL_VALUE_SHORTHAND.get()) {
                    stackSoulValueText = SharedFunctions.formatNumber(current.getCount() * value);
                } else {
                    stackSoulValueText = current.getCount() * value + "";
                }
            }
            event.getToolTip().add(1, (new TextComponent("Soul Value: ")).withStyle(ChatFormatting.GRAY).append((new TextComponent(value + " [" + stackSoulValueText + "]")).withStyle(ChatFormatting.DARK_PURPLE)));
            // we did everything here - skip vanilla handling
            return false;
        }
        return ret;
    }
}
