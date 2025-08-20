package io.iridium.qolhunters.mixin.fixscrollbar;

import io.iridium.qolhunters.features.fixscrollbar.IMouseRelease;
import iskallia.vault.client.gui.component.ScrollableContainer;
import iskallia.vault.client.gui.screen.player.legacy.tab.split.spi.AbstractDialog;
import net.minecraft.client.gui.components.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.Rectangle;

@Mixin(value = AbstractDialog.class, remap = false)
public class MixinAbstractDialog implements IMouseRelease {
    @Shadow protected ScrollableContainer descriptionComponent;

    @Shadow protected Rectangle bounds;

    @Shadow protected Button learnButton;

    @Shadow protected Button regretButton;

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void fixScrollClick(double screenX, double screenY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (this.bounds == null) {
            return;
        }
        if (this.descriptionComponent != null) {
            double containerX = screenX - this.bounds.x - 5.0F;
            double containerY = screenY - this.bounds.y - 5.0F;
            this.descriptionComponent.mouseClicked(containerX, containerY, button);
        }
    }

    @Inject(method = "mouseMoved", at = @At("HEAD"))
    private void fixScrollMove(double screenX, double screenY, CallbackInfo ci) {
        if (this.bounds == null) {
            return;
        }
        if (this.descriptionComponent != null) {
            double containerX = screenX - this.bounds.x - 5.0F;
            double containerY = screenY - this.bounds.y - 5.0F;
            this.descriptionComponent.mouseMoved(containerX, containerY);
        }
    }

    @Override public boolean qOLHunters$mouseReleased(double mouseX, double mouseY, int button) {
        if (this.bounds == null) {
            return false;
        }
        if (this.learnButton != null) {
            double containerX = mouseX - this.bounds.x - 5.0F;
            double containerY = mouseY - this.bounds.y - 5.0F;
            this.learnButton.mouseReleased(containerX, containerY, button);
        }

        if (this.regretButton != null) {
            double containerX = mouseX - this.bounds.x - 5.0F;
            double containerY = mouseY - this.bounds.y - 5.0F;
            this.regretButton.mouseReleased(containerX, containerY, button);
        }

        if (this.descriptionComponent != null) {
            double containerX = mouseX - this.bounds.x - 5.0F;
            double containerY = mouseY - this.bounds.y - 5.0F;
            this.descriptionComponent.mouseReleased(containerX, containerY, button);
        }

        return false;

    }
}
