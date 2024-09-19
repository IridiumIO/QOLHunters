package io.iridium.qolhunters.mixin.vaultmodifiers;

import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.VaultMod;
import iskallia.vault.core.vault.modifier.spi.VaultModifier;
import iskallia.vault.core.vault.overlay.ModifiersRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ModifiersRenderer.class)
public abstract class MixinModifiersRenderer {


    @Redirect(method = "renderVaultModifiers(Ljava/util/Map;Lcom/mojang/blaze3d/vertex/PoseStack;Z)V", at = @At(value = "INVOKE", target = "Liskallia/vault/core/vault/modifier/spi/VaultModifier;getIcon()Ljava/util/Optional;"), remap = false)
    private static Optional<ResourceLocation> getIconRedirect(VaultModifier<?> instance) {

        Optional<ResourceLocation> icon = instance.getIcon();

        if (icon.isPresent() && QOLHuntersClientConfigs.VAULT_MODIFIER_TEXT_OVERLAYS.get()) {
            String modifierID = instance.getId().getPath();
            String iconPath = icon.get().getPath();

            int lastSlash = iconPath.lastIndexOf("/");
            String newPath = iconPath.substring(0, lastSlash + 1) + "described/" + modifierID;
            return Optional.of(VaultMod.id(newPath));
        }

        return icon;
    }
}
