package io.iridium.qolhunters.mixin.searchablevaultstations.transmog;

import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.searchablevaultstations.QOLSearchElement;
import io.iridium.qolhunters.features.searchablevaultstations.SearchableScreen;
import iskallia.vault.client.gui.framework.element.ScrollableItemStackSelectorElement;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.screen.block.TransmogTableScreen;
import iskallia.vault.container.TransmogTableContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TransmogTableScreen.class, remap = false)
public class MixinTransmogTableScreen extends AbstractElementContainerScreen<TransmogTableContainer> implements SearchableScreen {
    private MixinTransmogTableScreen(TransmogTableContainer container, Inventory inventory,
                                     Component title,
                                     IElementRenderer elementRenderer,
                                     ITooltipRendererFactory<AbstractElementContainerScreen<TransmogTableContainer>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }

    @Unique private QOLSearchElement qolhunters$searchBox;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (!Boolean.TRUE.equals(QOLHuntersClientConfigs.SEARCHABLE_VAULT_STATIONS.get())) {
            return;
        }
        this.qolhunters$searchBox = QOLSearchElement.createRight(this, 80, -4, -22);

        elementStore.getGuiEventElementList().stream()
            .filter(e -> e instanceof ScrollableItemStackSelectorElement<?, ?>)
            .map(e -> (ScrollableItemStackSelectorElement<?, ?>) e)
            .findFirst()
            .ifPresent(scrollableItemStackSelectorElement ->
                this.qolhunters$searchBox.onTextChanged(text ->
                    scrollableItemStackSelectorElement.refreshElements()
                )
            );

        this.addElement(this.qolhunters$searchBox);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true, remap = true)
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
            }
        } else if (pKeyCode == InputConstants.KEY_TAB) {
            this.qolhunters$searchBox.adjustEditBox(box -> box.setFocus(true));
            this.setFocused(this.qolhunters$searchBox);
            cir.setReturnValue(true);
        }
    }

    @Override public QOLSearchElement getSearchBox() {
        return qolhunters$searchBox;
    }
}
