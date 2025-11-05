package io.iridium.qolhunters.mixin.voidcrucible;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import iskallia.vault.VaultMod;
import iskallia.vault.client.gui.framework.element.VerticalScrollClipContainer;
import iskallia.vault.client.gui.framework.spatial.Spatials;
import iskallia.vault.client.gui.framework.spatial.spi.ISpatial;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.screen.void_stone.elements.ThemeButtonElement;
import iskallia.vault.client.gui.screen.void_stone.elements.ThemeListElement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(value = ThemeListElement.class, remap = false)
public abstract class MixinThemeListElement extends VerticalScrollClipContainer<ThemeListElement> {
    @Shadow @Final private Consumer<ResourceLocation> selectTheme;

    protected MixinThemeListElement(ISpatial spatial) {
        super(spatial);
    }

    @Inject(method = "initializeThemes", at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"))
    private void addAllThemeButton(CallbackInfo ci,
                                   @Local(name = "buttonWidth") int buttonWidth,
                                   @Local(name = "buttonHeight") int buttonHeight,
                                   @Local(name = "x") int x,
                                   @Local(name = "y") LocalIntRef y){
        Component themeName = new TextComponent("All Themes");
        List<FormattedText> themeNameLines = UIHelper.getLines(themeName, 125).stream().filter((line) -> !line.getString().isEmpty()).toList();
        ThemeButtonElement
            button = (new ThemeButtonElement(Spatials.positionXY(x, y.get()).size(buttonWidth, buttonHeight * themeNameLines.size()), Map.entry(VaultMod.id("all_themes"),"All Themes"), () -> {
            if (this.selectTheme != null) {
                this.selectTheme.accept(VaultMod.id("all_themes"));
            }

        }));
        this.addElement(button);

        y.set(y.get() + buttonHeight * themeNameLines.size());
    }


}
