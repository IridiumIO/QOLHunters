package io.iridium.qolhunters.mixin.transmogtable;

import iskallia.vault.client.gui.framework.element.DiscoveredModelSelectElement;
import iskallia.vault.dynamodel.DynamicModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DiscoveredModelSelectElement.TransmogModelEntry.class)
public interface AccessorTransmogModelEntry {
    @Accessor DynamicModel getModel();
}
