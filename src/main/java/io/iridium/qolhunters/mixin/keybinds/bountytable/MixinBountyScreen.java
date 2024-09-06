package io.iridium.qolhunters.mixin.keybinds.bountytable;

import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.screen.bounty.BountyScreen;
import iskallia.vault.client.gui.screen.bounty.element.BountyTableContainerElement;
import iskallia.vault.container.BountyContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(BountyScreen.class)
public abstract class MixinBountyScreen extends AbstractElementContainerScreen<BountyContainer> {

    protected MixinBountyScreen(BountyContainer container, Inventory inventory, Component title, IElementRenderer elementRenderer, ITooltipRendererFactory<AbstractElementContainerScreen<BountyContainer>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }

    @Shadow(remap = false) @Final
    private BountyTableContainerElement bountyTableContainerElement;

    @Inject(method="keyPressed", at=@At("HEAD"), cancellable=true)
    public void keyPressed(int pKeyCode, int pScanCode, int pModifiers, CallbackInfoReturnable<Boolean> cir) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        InputConstants.Key key = InputConstants.getKey(pKeyCode, pScanCode);

        if (key.equals(KeyBindings.FORGE_ITEM.getKey())) {

            //I used reflection. Sue me.
            Method handleRerollMethod = bountyTableContainerElement.getClass().getDeclaredMethod("handleReroll");
            handleRerollMethod.setAccessible(true);
            handleRerollMethod.invoke(bountyTableContainerElement);

            cir.setReturnValue(true);
        }

    }

}
