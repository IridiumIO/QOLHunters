package io.iridium.qolhunters.mixin.ascenscionforge;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.framework.element.AscensionForgeSelectElement;
import mezz.jei.Internal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AscensionForgeSelectElement.DiscoveredModelSelectorModel.class)
public class MixinDiscoveredModelSelectorModel {
    @Inject(at = @At("RETURN"), method = "getEntries", remap = false)
    public void filterSearch(CallbackInfoReturnable<List<AscensionForgeSelectElement.AscencionForgeModelEntry>> cir) {
        if (!Boolean.TRUE.equals(QOLHuntersClientConfigs.SEARCHABLE_ASCENSION_FORGE.get()) || Internal.getRuntime() == null) {
            return;
        }

        String[] filters = Internal.getRuntime().getIngredientFilter().getFilterText().toLowerCase().split(" ");
        cir.getReturnValue().removeIf(entry -> {
            String tooltip = entry.getTooltip().getString().toLowerCase();
            for (String filter : filters) {
                if (!tooltip.contains(filter)) {
                    return true;
                }
            }
            return false;
        });
    }
}
