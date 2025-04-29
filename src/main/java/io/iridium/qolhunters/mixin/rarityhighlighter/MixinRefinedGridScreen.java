package io.iridium.qolhunters.mixin.rarityhighlighter;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.helper.GearRarityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BaseScreen.class, remap = false)
public class MixinRefinedGridScreen {


    @Inject(method="renderItem(Lcom/mojang/blaze3d/vertex/PoseStack;IILnet/minecraft/world/item/ItemStack;ZLjava/lang/String;I)V", at=@At("HEAD"))
    private void renderItem(PoseStack poseStack, int x, int y, ItemStack stack, boolean overlay, String text, int textColor, CallbackInfo ci) {
        if (!QOLHuntersClientConfigs.RARITY_HIGHLIGHTER.get() ) return;

        if((InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT))){
            GearRarityRenderer.renderRarityHighlight(poseStack, stack, x, y);
        }
    }

}
