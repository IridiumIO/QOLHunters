package io.iridium.qolhunters.mixin.transmogtable;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.framework.element.AscensionForgeSelectElement;
import iskallia.vault.client.gui.framework.element.DiscoveredModelSelectElement;
import iskallia.vault.client.gui.screen.block.TransmogTableScreen;
import iskallia.vault.dynamodel.DynamicModel;
import iskallia.vault.gear.VaultGearRarity;
import iskallia.vault.init.ModConfigs;
import mezz.jei.Internal;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(DiscoveredModelSelectElement.DiscoveredModelSelectorModel.class)
public class MixinDiscoveredModelSelectorModel {
    @Inject(at = @At("RETURN"), method = "getEntries", remap = false)
    public void filterSearch(CallbackInfoReturnable<List<DiscoveredModelSelectElement.TransmogModelEntry>> cir) {
        if (!Boolean.TRUE.equals(QOLHuntersClientConfigs.SEARCHABLE_TRANSMOG_TABLE.get()) || Internal.getRuntime() == null) {
            return;
        }

        String[] filters = Internal.getRuntime().getIngredientFilter().getFilterText().toLowerCase().split(" ");

        cir.getReturnValue().removeIf(entry -> {
            DynamicModel model = ((AccessorTransmogModelEntry) entry).getModel();
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
