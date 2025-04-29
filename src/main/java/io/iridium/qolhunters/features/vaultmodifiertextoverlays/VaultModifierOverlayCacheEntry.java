package io.iridium.qolhunters.features.vaultmodifiertextoverlays;

import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class VaultModifierOverlayCacheEntry {

    public final Optional<ResourceLocation> resourceLocation;
    public final long timestamp;

    public VaultModifierOverlayCacheEntry(Optional<ResourceLocation> resourceLocation, long timestamp) {
        this.resourceLocation = resourceLocation;
        this.timestamp = timestamp;
    }
}
