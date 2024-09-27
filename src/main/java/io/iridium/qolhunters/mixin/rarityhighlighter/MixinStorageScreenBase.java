package io.iridium.qolhunters.mixin.rarityhighlighter;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.util.SharedFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StorageScreenBase.class)
public abstract class MixinStorageScreenBase<S extends StorageContainerMenuBase<?>> {

    @Inject(method = "renderStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderAndDecorateItem(Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.AFTER))
        public void renderStack(PoseStack poseStack, int i, int j, ItemStack itemstack, boolean flag, String stackCountText, CallbackInfo ci) {
        if (!QOLHuntersClientConfigs.RARITY_HIGHLIGHTER.get() ) return;

        if((InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT))){
            SharedFunctions.renderSlotRarityHighlight(poseStack, itemstack, i, j);
        }
        }

}
