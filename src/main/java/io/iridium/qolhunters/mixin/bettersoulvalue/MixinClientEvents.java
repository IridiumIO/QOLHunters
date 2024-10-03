package io.iridium.qolhunters.mixin.bettersoulvalue;

import com.google.common.collect.Lists;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.util.SharedFunctions;
import iskallia.vault.event.ClientEvents;
import iskallia.vault.init.ModConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mixin(value = ClientEvents.class, remap = false)
public class MixinClientEvents {


    @Inject(method = "onItemTooltip", at = @At("HEAD"), cancellable = true)
    private static void onItemTooltip(ItemTooltipEvent event, CallbackInfo ci) {

        if(QOLHuntersClientConfigs.BETTER_SOUL_VALUE.get()){

            ModConfigs.TOOLTIP.getTooltipString(event.getItemStack().getItem()).ifPresent(str -> {
                List<Component> tooltip = event.getToolTip();
                List<String> added = Lists.reverse(Lists.newArrayList(str.split("\n")));
                if (!added.isEmpty()) {
                    tooltip.add(1, TextComponent.EMPTY);

                    for (String newStr : added) {
                        tooltip.add(1, new TextComponent(newStr).withStyle(ChatFormatting.GRAY));
                    }
                }
            });
            ItemStack current = event.getItemStack();
            if (!current.isEmpty()) {
                Item item = current.getItem();
                if (ModConfigs.VAULT_DIFFUSER.contains(current)) {
                    int value = ModConfigs.VAULT_DIFFUSER.getDiffuserOutputMap().get(item.getRegistryName());
                    if (value > 0) {

                        MutableComponent soulV;

                        if(QOLHuntersClientConfigs.BETTER_SOUL_VALUE_USE_SHARDS.get()){
                            if(QOLHuntersClientConfigs.BETTER_SOUL_VALUE_SHORTHAND.get()){
                                soulV = new TextComponent((value + " [" + SharedFunctions.formatNumberWithDecimal(current.getCount() * value / 9.0) + " shards]")).withStyle(ChatFormatting.DARK_PURPLE);
                            }else{
                                soulV = new TextComponent((value + " [" + String.format("%.0f",current.getCount() * value / 9.0) + " shards]")).withStyle(ChatFormatting.DARK_PURPLE);
                            }
                        }else {
                            if(QOLHuntersClientConfigs.BETTER_SOUL_VALUE_SHORTHAND.get()){
                                soulV = new TextComponent((value + " [" + SharedFunctions.formatNumber(current.getCount() * value) + "]")).withStyle(ChatFormatting.DARK_PURPLE);
                            }else{
                                soulV = new TextComponent(value + " [" + current.getCount() * value + "]").withStyle(ChatFormatting.DARK_PURPLE);
                            }
                        }


                        if (Screen.hasShiftDown()) {
                            event.getToolTip()
                                    .add(
                                            1,
                                            new TextComponent("Soul Value: ")
                                                    .withStyle(ChatFormatting.GRAY)
                                                    .append(soulV)
                                    );
                        } else {
                            event.getToolTip()
                                    .add(
                                            1,
                                            new TextComponent("Soul Value: ")
                                                    .withStyle(ChatFormatting.GRAY)
                                                    .append(new TextComponent(value + "").withStyle(ChatFormatting.DARK_PURPLE))
                                    );
                        }
                    }
                }

                addLootTableInfoToTooltip(current, event.getToolTip());
                if (event.getFlags().isAdvanced()) {
                    removeVanillaDurabilityLineFromVaultGearTooltip(current, event.getToolTip());
                }
            }
            ci.cancel();
        }


    }

    @Shadow
    private static void addLootTableInfoToTooltip(ItemStack stack, List<Component> tooltip) {
    }

    @Shadow
    private static void removeVanillaDurabilityLineFromVaultGearTooltip(ItemStack stack, List<Component> tooltip) {
    }

}
