package io.iridium.qolhunters.mixin.voidcrucible;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.features.searchablevaultstations.QOLSearchElement;
import io.iridium.qolhunters.features.searchablevaultstations.SearchableScreen;
import iskallia.vault.VaultMod;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.framework.spatial.Spatials;
import iskallia.vault.client.gui.screen.void_stone.VoidCrucibleScreen;
import iskallia.vault.client.gui.screen.void_stone.elements.ThemeListElement;
import iskallia.vault.container.VoidCrucibleContainer;
import iskallia.vault.core.data.key.VersionedKey;
import iskallia.vault.core.util.ThemeBlockRetriever;
import iskallia.vault.core.vault.VaultRegistry;
import iskallia.vault.init.ModConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = VoidCrucibleScreen.ThemeSelect.class, remap = false)
public abstract class MixinVoidCrucibleScreen extends AbstractElementContainerScreen<VoidCrucibleContainer.ThemeSelect> implements SearchableScreen {

    @Shadow protected abstract void selectTheme(ResourceLocation newTheme);

    @Shadow private ResourceLocation selectedTheme;

    @Shadow protected abstract void removeElements();

    @Shadow private ThemeListElement listElement;

    @Shadow protected abstract Map<ResourceLocation, String> getThemes();

    @Unique private QOLSearchElement qolhunters$searchBox;

    public MixinVoidCrucibleScreen(VoidCrucibleContainer.ThemeSelect container, Inventory inventory, Component title,
                                   IElementRenderer elementRenderer,
                                   ITooltipRendererFactory<AbstractElementContainerScreen<VoidCrucibleContainer.ThemeSelect>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }

    @Inject(method = "initializeViewTheme", at = @At(value = "INVOKE", target = "Liskallia/vault/config/crucible/VoidCrucibleCustomItemConfig;getItems()Ljava/util/Map;", ordinal = 0))
    private void addAllThemesLabel(ResourceLocation theme, CallbackInfo ci, @Local(name = "themeName") LocalRef<String> themeName) {
        if (selectedTheme.equals(VaultMod.id("all_themes"))) {
            themeName.set("All Themes");
        }
    }


    @Redirect(method = "initializeViewTheme", at = @At(value = "INVOKE", target = "Liskallia/vault/container/VoidCrucibleContainer$ThemeSelect;getBlocksInTheme(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/List;"))
    private List<ResourceLocation> addAllThemes(VoidCrucibleContainer.ThemeSelect instance, ResourceLocation selectedTheme) {
        if (selectedTheme.equals(VaultMod.id("all_themes"))) {
            var roomConfig = ModConfigs.VOID_CRUCIBLE_CUSTOM_ROOMS.getAllItems();
            Set<ResourceLocation> themeBlocks = new HashSet<>();
            VaultRegistry.THEME.getKeys().stream().map(VersionedKey::getId).map(ThemeBlockRetriever::getBlocksForTheme).forEach(themeBlocks::addAll);
            themeBlocks.addAll(roomConfig);
            var blockList = new ArrayList<>(themeBlocks);
            blockList.sort(ResourceLocation::compareNamespaced);
            return blockList;
        }
        return instance.getBlocksInTheme(selectedTheme);
    }

    @Redirect(method = "initializeViewTheme", at = @At(value = "NEW", target = "()Ljava/util/HashMap;"))
    private HashMap<ResourceLocation, Boolean> sortedBlocks() {
        return new LinkedHashMap<>();
    }


    // search
    @Inject(method = "<init>", at = @At("TAIL"))
    public void addSearchBox(CallbackInfo ci) {
        this.qolhunters$searchBox = QOLSearchElement.createRight(this, 80, -9, 43);
        this.qolhunters$searchBox.onTextChanged(text -> {
            if (selectedTheme == null) {
                return;
            }
            this.selectTheme(selectedTheme);
        });
        this.addElement(this.qolhunters$searchBox);
        qolhunters$searchBox.setVisible(selectedTheme != null);
    }

    @Inject(method = "selectTheme", at = @At("TAIL"))
    private void makeVisible(ResourceLocation newTheme, CallbackInfo ci) {
        this.qolhunters$searchBox.setVisible(this.selectedTheme != null);
    }

    @Inject(method = "lambda$initializeViewTheme$3", at = @At("TAIL"))
    private void makeInvisible(CallbackInfo ci) {
        this.qolhunters$searchBox.setVisible(this.selectedTheme != null);
    }

    @Intrinsic
    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Dynamic(mixin = MixinVoidCrucibleScreen.class) @SuppressWarnings("target")
    @Inject(method = "keyPressed(III)Z", at = @At("HEAD"), cancellable = true, remap = true)
    public void keyPressed(int pKeyCode, int pScanCode, int pModifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.qolhunters$searchBox == null) {
            return;
        }
        if (this.qolhunters$searchBox.isFocused()) {
            if (pKeyCode == InputConstants.KEY_ESCAPE) {
                this.qolhunters$searchBox.adjustEditBox(box -> box.setFocus(false));
                this.setFocused(null);
                cir.setReturnValue(true);
                return;
            }

            InputConstants.Key key = InputConstants.getKey(pKeyCode, pScanCode);
            if (Minecraft.getInstance().options.keyInventory.isActiveAndMatches(key)){
                cir.setReturnValue(true);
                return;
            }
        } else {
            if (pKeyCode == InputConstants.KEY_TAB) {
                this.qolhunters$searchBox.adjustEditBox(box -> box.setFocus(true));
                this.setFocused(this.qolhunters$searchBox);
                cir.setReturnValue(true);
                return;
            }
            if (pKeyCode == InputConstants.KEY_ESCAPE && this.selectedTheme != null) {
                this.selectedTheme = null; // back button functionality
                this.removeElements();
                this.addElement(this.listElement = new ThemeListElement(
                    Spatials.positionXY(8, 18).size(this.getGuiSpatial().width() - 16, this.getGuiSpatial().height() - 24), this.getThemes(), this::selectTheme).layout(this.translateWorldSpatial()));
                qolhunters$searchBox.setVisible(false);
                cir.setReturnValue(true);
                return;
            }
        }
    }

    @Override public QOLSearchElement getSearchBox() {
        return qolhunters$searchBox;
    }
}
