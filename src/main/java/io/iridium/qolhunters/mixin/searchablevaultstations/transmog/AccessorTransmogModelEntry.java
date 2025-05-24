package io.iridium.qolhunters.mixin.searchablevaultstations.transmog;

import iskallia.vault.client.gui.framework.element.DiscoveredModelSelectElement;
import iskallia.vault.dynamodel.DynamicModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = DiscoveredModelSelectElement.TransmogModelEntry.class, remap = false)
public interface AccessorTransmogModelEntry {
    @Accessor DynamicModel getModel();
}
