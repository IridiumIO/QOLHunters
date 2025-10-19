package io.iridium.qolhunters.mixin.betterlootstats;

import iskallia.vault.client.gui.screen.summary.VaultExitContainerScreenData;
import iskallia.vault.core.vault.stat.StatCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = VaultExitContainerScreenData.class, remap = false)
public abstract class MixinVaultExitContainerScreenData {

    /**
     * @author radimous
     * @reason show count of all chest types
     */
    @Overwrite
    public static int getTotalChests(StatCollector statCollector) {
        return statCollector.get(StatCollector.CHESTS).size();
    }

}
