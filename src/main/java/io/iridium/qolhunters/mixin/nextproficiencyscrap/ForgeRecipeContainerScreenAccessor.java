package io.iridium.qolhunters.mixin.nextproficiencyscrap;

import iskallia.vault.client.gui.screen.block.base.ForgeRecipeContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ForgeRecipeContainerScreen.class, remap = false)
public interface ForgeRecipeContainerScreenAccessor {
    @Invoker("getCraftedLevel")
    int qolhunters$getCraftedLevel();
}
