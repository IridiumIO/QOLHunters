package io.iridium.qolhunters.mixin.jewelselection;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.jewelselection.AutoChosenTextWidget;
import iskallia.vault.client.gui.screen.JewelPouchSelectionScreen;
import iskallia.vault.gear.GearScoreHelper;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.JewelPouchItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.List;

@Mixin(value = JewelPouchSelectionScreen.class, remap = false)
public abstract class MixinJewelSelectionScreen  extends Screen {
    @Shadow @Final private List<JewelPouchItem.RolledJewel> jewels;

    @Shadow public abstract void render(PoseStack matrixStack, int pMouseX, int pMouseY, float pPartialTick);

    protected MixinJewelSelectionScreen(Component pTitle) {
        super(pTitle);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Liskallia/vault/client/gui/screen/JewelPouchSelectionScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"), remap = true)
    private void renderAutochosen(CallbackInfo ci, @Local(name = "outcome") JewelPouchItem.RolledJewel outcome, @Local(name = "x") float x) {
        if (!QOLHuntersClientConfigs.AUTOCHOSEN_JEWEL.get()) return;
        var max = this.jewels.stream().max(Comparator.comparingInt((JewelPouchItem.RolledJewel jewel) -> GearScoreHelper.getWeight(jewel.stack()))).orElse(null);
        if (max == null) return;
        if (max == outcome && max.stack().getItem() == ModItems.JEWEL) {
            this.addRenderableOnly(new AutoChosenTextWidget(x, height));
        }
    }
}
