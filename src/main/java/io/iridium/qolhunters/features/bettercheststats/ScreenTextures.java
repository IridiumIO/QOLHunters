package io.iridium.qolhunters.features.bettercheststats;

import iskallia.vault.VaultMod;
import iskallia.vault.client.atlas.TextureAtlasRegion;
import iskallia.vault.init.ModTextureAtlases;

public class ScreenTextures {
    public static final TextureAtlasRegion ENIGMA_CHEST_SMALL_ICON = TextureAtlasRegion.of(ModTextureAtlases.SCREEN, VaultMod.id("gui/screen/icon/enigma"));
    public static final TextureAtlasRegion FLESH_CHEST_SMALL_ICON = TextureAtlasRegion.of(ModTextureAtlases.SCREEN, VaultMod.id("gui/screen/icon/flesh"));
    public static final TextureAtlasRegion HARDENED_CHEST_SMALL_ICON = TextureAtlasRegion.of(ModTextureAtlases.SCREEN, VaultMod.id("gui/screen/icon/hardened"));
}
