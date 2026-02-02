package io.iridium.qolhunters.mixin.nextproficiencyscrap;

import iskallia.vault.client.gui.screen.block.VaultForgeScreen;
import iskallia.vault.config.gear.VaultGearCraftingConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = VaultForgeScreen.ProficiencySegment.class, remap = false)
public interface ProficiencySegmentAccessor {
    @Accessor
    VaultGearCraftingConfig.ProficiencyStep getStep();
}
