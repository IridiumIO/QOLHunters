package io.iridium.qolhunters.mixin.vaultmodifiers;

import io.iridium.qolhunters.features.vaultmodifiertextoverlays.VaultModifierOverlays;
import iskallia.vault.core.vault.modifier.spi.VaultModifier;
import iskallia.vault.core.vault.overlay.ModifiersRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ModifiersRenderer.class)
public abstract class MixinModifiersRenderer {


    @Redirect(method = "renderVaultModifiers(Ljava/util/Map;Lcom/mojang/blaze3d/vertex/PoseStack;ZFLiskallia/vault/util/Alignment;Z)V", at = @At(value = "INVOKE", target = "Liskallia/vault/core/vault/modifier/spi/VaultModifier;getIcon()Ljava/util/Optional;"), remap = false)
    private static Optional<ResourceLocation> getIconRedirect(VaultModifier<?> instance) {

        return VaultModifierOverlays.getModifierIcon(instance);
    }


}
