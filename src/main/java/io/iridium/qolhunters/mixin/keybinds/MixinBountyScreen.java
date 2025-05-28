package io.iridium.qolhunters.mixin.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.client.gui.screen.bounty.BountyScreen;
import iskallia.vault.client.gui.screen.bounty.element.BountyElement;
import iskallia.vault.client.gui.screen.bounty.element.BountyTableContainerElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BountyScreen.class, remap = false)
public abstract class MixinBountyScreen {


    @Shadow public abstract BountyTableContainerElement getBountyTableElement();

    @Inject(method = "keyPressed", at = @At("HEAD"), remap = true)
    public void keyPressed(int pKeyCode, int pScanCode, int pModifiers, CallbackInfoReturnable<Boolean> cir) {
        InputConstants.Key key = InputConstants.getKey(pKeyCode, pScanCode);

        if (key.equals(KeyBindings.FORGE_ITEM.getKey()) && Boolean.TRUE.equals(QOLHuntersClientConfigs.VAULT_INTERFACE_KEYBINDS.get())) {
            var bountyContainer = ((AccessorBountyTableContainerElement) this.getBountyTableElement());
            if (bountyContainer == null) {
                return;
            }
            if (bountyContainer.getBountyElement() == null) {
                return;
            }
            if (bountyContainer.getBountyElement().getSelectedBounty() == null) {
                return;
            }
            if (bountyContainer.getBountyElement().getStatus() != BountyElement.Status.AVAILABLE) {
                return;
            }
            bountyContainer.invokeHandleReroll();
        }
    }

}
