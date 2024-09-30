package io.iridium.qolhunters.mixin.rarityhighlighter;

import appeng.client.gui.AEBaseScreen;
import appeng.menu.AEBaseMenu;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.util.SharedFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AEBaseScreen.class)
public abstract class MixinAEBaseScreen<T extends AEBaseMenu> extends AbstractContainerScreen<T> {


    protected MixinAEBaseScreen(T pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Inject(method="renderSlot", at= @At(value = "TAIL"))
    private void renderSlot(PoseStack poseStack, Slot s, CallbackInfo ci) {
        if (QOLHuntersClientConfigs.RARITY_HIGHLIGHTER.get()) {
            if ((InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT))) {
                SharedFunctions.renderSlotRarityHighlight(poseStack, s.getItem(), s.x, s.y);
            }
        }

    }

}
