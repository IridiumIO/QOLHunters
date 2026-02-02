package io.iridium.qolhunters.mixin.nextproficiencyscrap;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import iskallia.vault.client.data.ClientProficiencyData;
import iskallia.vault.client.gui.framework.element.VerticalSegmentedBarElement;
import iskallia.vault.client.gui.screen.block.VaultForgeScreen;
import iskallia.vault.config.gear.VaultGearCraftingConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = VerticalSegmentedBarElement.class, remap = false)
public abstract class MixinVerticalSegmentedBarElement {

    @WrapOperation(method = "lambda$onHoverTooltip$0", at = @At(value = "INVOKE", target = "Liskallia/vault/client/gui/framework/element/VerticalSegmentedBarElement$BarSegment;getNextComponentDescriptor()Lnet/minecraft/network/chat/Component;"))
    private static Component addAbsoluteValue(VerticalSegmentedBarElement.BarSegment instance, Operation<Component> original) {
        var rv = original.call(instance);
        if (rv instanceof MutableComponent mc && Minecraft.getInstance().screen instanceof VaultForgeScreen forgeScreen) {

            int absProficiency = ClientProficiencyData.getProficiency();
            int playerLevel = ((ForgeRecipeContainerScreenAccessor)forgeScreen).qolhunters$getCraftedLevel();
            int proficiencyCap = VaultGearCraftingConfig.getProficiencyCap(playerLevel);


            var profBar = ((VaultForgeScreenAccessor)forgeScreen).getProficiencyBar();
            ProficiencySegmentAccessor nextSeg = (ProficiencySegmentAccessor)profBar.getNextSegment().orElse(null);
            if (nextSeg == null) return rv;

            // might be off by one
            int toNextLevelCeil =  (int)Math.ceil(proficiencyCap*nextSeg.getStep().getMinProficiency()) - absProficiency;

            if (toNextLevelCeil > 0) {
                mc.append(new TextComponent(" " + toNextLevelCeil + " scrap").withStyle(Style.EMPTY.withBold(false).withColor(ChatFormatting.DARK_GRAY)));
            }
        }
        return rv;
    }
}
