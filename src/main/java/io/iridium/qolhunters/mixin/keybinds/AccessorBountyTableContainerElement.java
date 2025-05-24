package io.iridium.qolhunters.mixin.keybinds;

import iskallia.vault.client.gui.screen.bounty.element.BountyElement;
import iskallia.vault.client.gui.screen.bounty.element.BountyTableContainerElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = BountyTableContainerElement.class, remap = false)
public interface AccessorBountyTableContainerElement {
    @Invoker
    void invokeHandleReroll();

    @Accessor
    BountyElement getBountyElement();
}
