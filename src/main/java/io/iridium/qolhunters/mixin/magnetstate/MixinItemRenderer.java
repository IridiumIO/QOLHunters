package io.iridium.qolhunters.mixin.magnetstate;


import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.VaultMod;
import iskallia.vault.client.gui.helper.ScreenDrawHelper;
import iskallia.vault.gear.VaultGearState;
import iskallia.vault.gear.data.GearDataCache;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.MagnetItem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(ItemRenderer.class)
public class MixinItemRenderer {


    @Inject(
        method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z")
    )
    public void renderMagnetStateOverlay(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {

        if (stack.getItem() == ModItems.MAGNET && QOLHuntersClientConfigs.MAGNET_STATE_OVERLAY.get() && GearDataCache.of(stack).getState() == VaultGearState.IDENTIFIED) {
            var pickupState = MagnetItem.getPickupState(stack);
            ResourceLocation overlayIcon = switch (pickupState) {
                case VOID -> new ResourceLocation(QOLHunters.MOD_ID, "textures/gui/magnet_state/void.png");
                case PICKUP_ONLY -> new ResourceLocation(QOLHunters.MOD_ID, "textures/gui/magnet_state/pickup.png");
                case OFF -> new ResourceLocation(QOLHunters.MOD_ID, "textures/gui/magnet_state/off.png");
                default -> null;
            };
            if (overlayIcon != null) {
                RenderSystem.setShaderTexture(0, overlayIcon);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableDepthTest();
                ScreenDrawHelper.drawTexturedQuads(buf -> {
                    PoseStack pose = new PoseStack();
                    ScreenDrawHelper.rect(buf, pose).at(x, y).dim(16.0F, 16.0F).draw();
                });
                RenderSystem.enableDepthTest();
                RenderSystem.disableBlend();
            }
        }
    }

}
