package io.iridium.qolhunters.mixin.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.screen.block.ModifierWorkbenchScreen;
import iskallia.vault.container.ModifierWorkbenchContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModifierWorkbenchScreen.class)
public abstract class MixinModifierWorkbenchScreen extends AbstractElementContainerScreen<ModifierWorkbenchContainer> {


    protected MixinModifierWorkbenchScreen(ModifierWorkbenchContainer container, Inventory inventory, Component title, IElementRenderer elementRenderer, ITooltipRendererFactory<AbstractElementContainerScreen<ModifierWorkbenchContainer>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }

    @Inject(method="keyPressed", at=@At("HEAD"), cancellable=true)
    public void keyPressed(int pKeyCode, int pScanCode, int pModifiers, CallbackInfoReturnable<Boolean> cir)  {
        InputConstants.Key key = InputConstants.getKey(pKeyCode, pScanCode);

        if (key.equals(KeyBindings.FORGE_ITEM.getKey()) && QOLHuntersClientConfigs.VAULT_INTERFACE_KEYBINDS.get()) {

            this.tryCraft();

            cir.setReturnValue(true);
        }

    }


    @Shadow(remap = false)
    private void tryCraft(){}
}
