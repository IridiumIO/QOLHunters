package io.iridium.qolhunters.features.vaultmodifiertextoverlays;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.VaultMod;
import iskallia.vault.client.atlas.ITextureAtlas;
import iskallia.vault.core.vault.modifier.spi.VaultModifier;
import iskallia.vault.init.ModTextureAtlases;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class VaultModifierOverlays {
    private static final ConcurrentHashMap<String, VaultModifierOverlayCacheEntry> cache = new ConcurrentHashMap<>();

    public static Optional<ResourceLocation> getModifierIcon(VaultModifier<?> instance) {
        Optional<ResourceLocation> icon = instance.getIcon();

        if (icon.isPresent() && QOLHuntersClientConfigs.VAULT_MODIFIER_TEXT_OVERLAYS.get()) {
            String modifierID = instance.getId().getPath();
            String iconPath = icon.get().getPath();

            int lastSlash = iconPath.lastIndexOf("/");
            String newPath = iconPath.substring(0, lastSlash + 1) + "described/" + modifierID;

            String cacheKey = VaultMod.id(newPath).toString();
            long currentTime = System.currentTimeMillis();

            VaultModifierOverlayCacheEntry cacheEntry = cache.get(cacheKey);
            if (cacheEntry != null && (currentTime - cacheEntry.timestamp) < 5000) return cacheEntry.resourceLocation;

            Optional<ResourceLocation> res = Optional.of(VaultMod.id(newPath));

            ITextureAtlas textureAtlas = ModTextureAtlases.MODIFIERS.get();
            TextureAtlasSprite sprite = textureAtlas.getSprite(res.get());

            if (sprite instanceof MissingTextureAtlasSprite) {
                cache.put(cacheKey, new VaultModifierOverlayCacheEntry(icon, currentTime));
                return icon;
            }

            cache.put(cacheKey, new VaultModifierOverlayCacheEntry(res, currentTime));
            return res;

        }

        return icon;
    }
}
