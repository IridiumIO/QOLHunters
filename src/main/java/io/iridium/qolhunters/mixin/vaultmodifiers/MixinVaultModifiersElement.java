package io.iridium.qolhunters.mixin.vaultmodifiers;

import io.iridium.qolhunters.interfaces.IVaultModifiersElement;
import iskallia.vault.VaultMod;
import iskallia.vault.client.atlas.TextureAtlasRegion;
import iskallia.vault.client.gui.framework.ScreenTextures;
import iskallia.vault.client.gui.framework.element.ContainerElement;
import iskallia.vault.client.gui.framework.element.NineSliceElement;
import iskallia.vault.client.gui.framework.element.TextureAtlasElement;
import iskallia.vault.client.gui.framework.spatial.Spatials;
import iskallia.vault.client.gui.framework.spatial.spi.IMutableSpatial;
import iskallia.vault.client.gui.framework.spatial.spi.IPosition;
import iskallia.vault.client.gui.framework.spatial.spi.ISpatial;
import iskallia.vault.client.gui.framework.text.LabelTextStyle;
import iskallia.vault.client.gui.framework.text.TextBorder;
import iskallia.vault.client.gui.screen.summary.element.VaultModifiersElement;
import iskallia.vault.core.vault.modifier.spi.VaultModifier;
import iskallia.vault.init.ModTextureAtlases;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(VaultModifiersElement.class)
public abstract class MixinVaultModifiersElement<E extends VaultModifiersElement<E>> extends ContainerElement<E>  implements IVaultModifiersElement {


    protected MixinVaultModifiersElement(ISpatial spatial) {
        super(spatial);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void VaultModifiersElement(IPosition position, TextureAtlasRegion icon, int width, int height, Component name, Map supplier, CallbackInfo ci) {

        this.removeAllElements();

        this.addElement((NineSliceElement)(new NineSliceElement(Spatials.positionXYZ(5, 0, 3).size(24, 24), ScreenTextures.VAULT_EXIT_ELEMENT_ICON)).layout((screen, gui, parent, world) -> {
            world.size(24, 24);
        }));
        this.addElement((NineSliceElement)(new NineSliceElement(Spatials.positionXYZ(0, 2, 2).size(width, 20), ScreenTextures.VAULT_EXIT_ELEMENT_TITLE)).layout((screen, gui, parent, world) -> {
            world.size(width, 20);
        }));
        if (supplier.size() > 0) {
            this.addElement((NineSliceElement)(new NineSliceElement(Spatials.positionXYZ(4, 20, 1).size(width - 8, height - 20 + supplier.size() * 18), ScreenTextures.VAULT_EXIT_ELEMENT_BG)).layout((screen, gui, parent, world) -> {
                world.size(width - 8, height - 20 + supplier.size() * 18);
            }));
        }

        this.addElement(new TextureAtlasElement(Spatials.positionXYZ(8, 4, 5), icon));


        this.addElement(new ChestStringElement(Spatials.positionXYZ(32, 8, 4), Spatials.size(16, 7), () -> {
            return name;
        }, LabelTextStyle.shadow().left()));
        AtomicInteger iterator = new AtomicInteger();
        supplier.forEach((vaultModifier, integer) -> {

            int textWidth = ((Font) TextBorder.DEFAULT_FONT.get()).width(((VaultModifier<?>) vaultModifier).getDisplayNameFormatted((Integer) integer));
            int textWidth2 = ((Font)TextBorder.DEFAULT_FONT.get()).width("x" + integer);
            IMutableSpatial var10003 = Spatials.positionXYZ(30, 30 + iterator.get() * 18, 2);
            Objects.requireNonNull((Font)TextBorder.DEFAULT_FONT.get());
            this.addElement((ValueElement)(new ValueElement(var10003, Spatials.size(textWidth, 9), ((VaultModifier<?>) vaultModifier).getDisplayName(), LabelTextStyle.shadow())).tooltip(() -> {
                return new TextComponent(((VaultModifier<?>) vaultModifier).getDisplayDescriptionFormatted(((Integer) integer)));
            }));
            var10003 = Spatials.positionXYZ(width - 12 - textWidth2, 30 + iterator.get() * 18, 2);
            Objects.requireNonNull((Font)TextBorder.DEFAULT_FONT.get());
            this.addElement((ValueElement)(new ValueElement(var10003, Spatials.size(textWidth2, 9), "x" + integer, LabelTextStyle.shadow())).tooltip(() -> {
                return new TextComponent(((VaultModifier<?>) vaultModifier).getDisplayDescriptionFormatted(((Integer) integer)));
            }));
            Optional<ResourceLocation> icon_loc = ((VaultModifier<?>) vaultModifier).getIcon();
            icon_loc.ifPresent((resourceLocation) -> {

                String path = resourceLocation.getPath();
                int lastSlash = path.lastIndexOf("/");
                String newPath = path.substring(0, lastSlash + 1) + "16x/" + path.substring(lastSlash + 1);

                TextureAtlasRegion region = new TextureAtlasRegion(ModTextureAtlases.MODIFIERS, VaultMod.id(newPath));
                TextureAtlasElement atlasElement = new TextureAtlasElement(Spatials.positionXYZ(10, 26 + iterator.get() * 18, 2),
                        Spatials.size(16, 16),
                        region);

                this.addElement(atlasElement).tooltip(() -> {
                    return new TextComponent(((VaultModifier<?>) vaultModifier).getDisplayDescriptionFormatted(((Integer) integer)));
                });


            });
            iterator.getAndIncrement();
        });
    }


}
