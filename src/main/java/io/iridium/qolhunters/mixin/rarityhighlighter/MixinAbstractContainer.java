package io.iridium.qolhunters.mixin.rarityhighlighter;

import com.jaquadro.minecraft.storagedrawers.inventory.DrawerScreen;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.util.SharedFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainer{


    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderAndDecorateItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;III)V", shift = At.Shift.AFTER))
    public void renderSlot(PoseStack pPoseStack, Slot pSlot, CallbackInfo ci) {
        if (!QOLHuntersClientConfigs.RARITY_HIGHLIGHTER.get() ) return;
        if(((AbstractContainerScreen<?>)(Object)this) instanceof DrawerScreen) return;

        if((InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT))){
            SharedFunctions.renderSlotRarityHighlight(pPoseStack, pSlot.getItem(), pSlot.x, pSlot.y);
        }
    }
}
