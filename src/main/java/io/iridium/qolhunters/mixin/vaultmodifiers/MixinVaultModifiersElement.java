package io.iridium.qolhunters.mixin.vaultmodifiers;

import iskallia.vault.VaultMod;
import iskallia.vault.client.gui.framework.element.ContainerElement;
import iskallia.vault.client.gui.framework.spatial.spi.ISpatial;
import iskallia.vault.client.gui.screen.summary.element.VaultModifiersElement;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VaultModifiersElement.class)
public abstract class MixinVaultModifiersElement<E extends VaultModifiersElement<E>> extends ContainerElement<E>{


    protected MixinVaultModifiersElement(ISpatial spatial) {
        super(spatial);
    }

    // Thanks, @BONNe, for simplifying this!
    @Redirect(method = "lambda$new$7", at = @At(value = "INVOKE", target ="Liskallia/vault/VaultMod;id(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation set16x16TexturePath(String name)
    {
        int lastSlash = name.lastIndexOf("/");
        String newPath = name.substring(0, lastSlash + 1) + "16x/" + name.substring(lastSlash + 1);
        return VaultMod.id(newPath);
    }


}
