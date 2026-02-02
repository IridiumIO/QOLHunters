package io.iridium.qolhunters.mixin.searchablevaultstations.transmog;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.searchablevaultstations.SearchableScreen;
import iskallia.vault.client.gui.framework.element.DiscoveredModelSelectElement;
import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.gear.VaultGearRarity;
import iskallia.vault.init.ModConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = DiscoveredModelSelectElement.DiscoveredModelSelectorModel.class, remap = false)
public class MixinDiscoveredModelSelectorModel {
    @Inject(at = @At("RETURN"), method = "getEntries", cancellable = true)
    public void filterSearch(CallbackInfoReturnable<List<DiscoveredModelSelectElement.TransmogModelEntry>> cir) {
        if (!Boolean.TRUE.equals(QOLHuntersClientConfigs.SEARCHABLE_VAULT_STATIONS.get())) {
            return;
        }

        if (Minecraft.getInstance().screen instanceof SearchableScreen searchableScreen) {
            String filterText = searchableScreen.getSearchText().toLowerCase();

            String[] filters = filterText.split(" ");

            List<DiscoveredModelSelectElement.TransmogModelEntry> entries = new ArrayList<>(cir.getReturnValue());
            cir.setReturnValue(entries);
            entries.removeIf(entry -> {
                DynamicModel<?> model = ((AccessorTransmogModelEntry) entry).getModel();
                ItemStack itemStack = entry.getDisplayStack();
                VaultGearRarity rarity = ModConfigs.GEAR_MODEL_ROLL_RARITIES.getRarityOf(itemStack, model.getId());
                for (String filter : filters) {
                    if (!rarity.name().toLowerCase().contains(filter)
                        && !model.getId().toString().contains(filter)
                        && !model.getDisplayName().contains(filter)) {
                        return true;
                    }
                }
                return false;
            });
        }
    }
}
