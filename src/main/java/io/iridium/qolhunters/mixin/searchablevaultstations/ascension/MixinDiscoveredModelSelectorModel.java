package io.iridium.qolhunters.mixin.searchablevaultstations.ascension;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.searchablevaultstations.SearchableScreen;
import iskallia.vault.client.gui.framework.element.AscensionForgeSelectElement;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = AscensionForgeSelectElement.DiscoveredModelSelectorModel.class, remap = false)
public class MixinDiscoveredModelSelectorModel {
    @Inject(at = @At("RETURN"), method = "getEntries")
    public void filterSearch(CallbackInfoReturnable<List<AscensionForgeSelectElement.AscencionForgeModelEntry>> cir) {
        if (!Boolean.TRUE.equals(QOLHuntersClientConfigs.SEARCHABLE_VAULT_STATIONS.get())) {
            return;
        }

        if (Minecraft.getInstance().screen instanceof SearchableScreen searchableScreen){
            String filterText = searchableScreen.getSearchText().toLowerCase();
            String[] filters = filterText.split(" ");
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
}


