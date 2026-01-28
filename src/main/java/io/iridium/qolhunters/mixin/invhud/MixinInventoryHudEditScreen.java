package io.iridium.qolhunters.mixin.invhud;

import com.mojang.blaze3d.platform.InputConstants;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementScreen;
import iskallia.vault.client.gui.screen.accessibility.InventoryHudEditScreen;
import iskallia.vault.client.render.HudPosition;
import iskallia.vault.client.render.InventoryHudRenderer;
import iskallia.vault.client.render.hud.module.AbstractHudModule;
import iskallia.vault.client.render.hud.module.InventoryHudModule;
import iskallia.vault.client.render.hud.module.context.IModuleRenderContext;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = InventoryHudEditScreen.class, remap = false)
public abstract class MixinInventoryHudEditScreen extends AbstractElementScreen {

    @Shadow private InventoryHudModule<?> selected;

    @Shadow protected abstract void updateSettingsPanel();

    public MixinInventoryHudEditScreen(Component title,
                                       IElementRenderer elementRenderer,
                                       ITooltipRendererFactory<AbstractElementScreen> tooltipRendererFactory) {
        super(title, elementRenderer, tooltipRendererFactory);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true, remap = true)
    private void rightClickToEdit(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir){
            if (button == InputConstants.MOUSE_BUTTON_RIGHT) {
                for (InventoryHudModule mod : InventoryHudRenderer.getModules()) {
                    IModuleRenderContext context = mod.createRenderContext(mod.getScale(), this.getScreenSize().width(), this.getScreenSize().height(), mouseX, mouseY, true, this.selected == mod, null);

                    if (qolhunters$isHoveringOverModule(mod, context, mouseX, mouseY)) {
                        if (mod.hasSettings()) {
                            this.selected = mod;
                            this.updateSettingsPanel();
                            cir.setReturnValue(true);
                            return;
                        }
                    }
                }
            }
        }


    @SuppressWarnings("unchecked")
    @Unique private boolean qolhunters$isHoveringOverModule(InventoryHudModule module, IModuleRenderContext context, double mouseX, double mouseY) {
        if (module instanceof AbstractHudModule) {
            return module.isHovered(context);
        }

        HudPosition pos = module.getPosition();
        if (pos != null) {
            int left = pos.getScaledX(this.width);
            int top = pos.getScaledY(this.height);
            int right = left + module.getWidth(context);
            int bottom = top + module.getHeight(context);
            return mouseX >= left - 2 && mouseX < right + 4 && mouseY >= top - 2 && mouseY < bottom + 4;
        }

        return false;
    }

}
