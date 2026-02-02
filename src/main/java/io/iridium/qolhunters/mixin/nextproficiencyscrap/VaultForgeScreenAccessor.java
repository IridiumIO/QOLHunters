package io.iridium.qolhunters.mixin.nextproficiencyscrap;

import iskallia.vault.client.gui.framework.element.VerticalSegmentedBarElement;
import iskallia.vault.client.gui.screen.block.VaultForgeScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = VaultForgeScreen.class, remap = false)
public interface VaultForgeScreenAccessor {
    @Accessor
    VerticalSegmentedBarElement<?, VaultForgeScreen.ProficiencySegment> getProficiencyBar();
}
