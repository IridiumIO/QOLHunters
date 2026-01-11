package io.iridium.qolhunters.mixin.jewelselection;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.screen.JewelPouchSelectionScreen;
import iskallia.vault.client.gui.screen.block.UnboxingStationScreen;
import iskallia.vault.gear.GearScoreHelper;
import iskallia.vault.item.JewelPouchItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.List;

@Mixin(value = UnboxingStationScreen.class, remap = false)
public abstract class MixinUnboxingStationScreen extends Screen {

    @Shadow public abstract void render(PoseStack matrixStack, int pMouseX, int pMouseY, float pPartialTick);

    @Shadow @Final private List<UnboxingStationScreen.CardSection> cardSections;

    protected MixinUnboxingStationScreen(Component pTitle) {
        super(pTitle);
    }

    @Inject(method = "rebuildWidgets", at = @At(value = "NEW", target = "(Liskallia/vault/client/gui/screen/block/UnboxingStationScreen;ILnet/minecraft/world/item/ItemStack;IIII)Liskallia/vault/client/gui/screen/block/UnboxingStationScreen$CardSection;", ordinal = 1))
    private void renderAutochosen(CallbackInfo ci, @Local(name = "outcome") ItemStack outcome, @Local(name = "x") float x, @Local(name = "currentPack") List<ItemStack> currentPack) {
        if (!QOLHuntersClientConfigs.AUTOCHOSEN_JEWEL.get()) return;
        var max = currentPack.stream().max(Comparator.comparingInt(GearScoreHelper::getWeight)).orElse(null);
        if (max == null) return;
        if (max == outcome) {
            this.addRenderableOnly((poseStack, mouseX, mouseY, partialTick) -> {
                int scale = 4;
                int texSize = 16;
                int offset = texSize / 2 * scale + 2 * scale;
                String symbol = "AUTO";
                float symbolWidth = Minecraft.getInstance().font.width(symbol) * 0.5f;
                poseStack.pushPose();
                poseStack.translate(x + 28 - symbolWidth / 2, (double) height / 2 - offset, 0);
                poseStack.scale(0.5f, 0.5f, 0.5f);
                Minecraft.getInstance().font.draw(poseStack, symbol, 0, 0, ChatFormatting.GRAY.getColor());
                poseStack.popPose();
            });
        }
    }
}
