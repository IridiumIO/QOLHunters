package io.iridium.qolhunters.mixin.keybinds;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.searchablevaultstations.SearchableScreen;
import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.block.entity.base.ForgeRecipeTileEntity;
import iskallia.vault.client.gui.framework.element.ButtonElement;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.screen.block.base.ForgeRecipeContainerScreen;
import iskallia.vault.container.spi.ForgeRecipeContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ForgeRecipeContainerScreen.class, remap = false)
public abstract class MixinForgeRecipeContainerScreen<V extends ForgeRecipeTileEntity, T extends ForgeRecipeContainer<V>> extends AbstractElementContainerScreen<T> {

    @Inject(method="keyPressed", at=@At(value="HEAD"), cancellable=true)
    public void keyPressed(int pKeyCode, int pScanCode, int pModifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof SearchableScreen searchableScreen){
            var searchBox = searchableScreen.getSearchBox();
            if (searchBox != null && searchBox.isFocused()) {
                return;
            }
        }
        if(pKeyCode == KeyBindings.FORGE_ITEM.getKey().getValue() && Boolean.TRUE.equals(QOLHuntersClientConfigs.VAULT_INTERFACE_KEYBINDS.get())) {
            this.onCraftClick();
            cir.setReturnValue(true);
        }
    }


    @Inject(method = "onCraftClick", at = @At(value = "HEAD"), remap = false)
    public void onCraftClick(CallbackInfo ci) {
        if (this.getMenu().getResultSlot().hasItem() && Screen.hasShiftDown() && Boolean.TRUE.equals(QOLHuntersClientConfigs.VAULT_INTERFACE_KEYBINDS.get())) {
            this.slotClicked(this.getMenu().getResultSlot(), this.getMenu().getResultSlot().index, 0, ClickType.QUICK_MOVE);
        }
    }


    @Shadow @Final protected ButtonElement<?> craftButton;

    @Shadow protected abstract void onCraftClick();

    @Inject(method="containerTick", at=@At("TAIL"))
    protected void qol$containerTick(CallbackInfo ci) {
        MutableComponent text = new TextComponent("Hold ").withStyle(ChatFormatting.DARK_GRAY).append(new TextComponent("<SHIFT>").withStyle(ChatFormatting.GRAY)).append(" to Quick Move");
        if(!craftButton.isDisabled()) craftButton.tooltip(() -> text);
    }


    protected MixinForgeRecipeContainerScreen(T container, Inventory inventory, Component title, IElementRenderer elementRenderer, ITooltipRendererFactory<AbstractElementContainerScreen<T>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }
}