package io.iridium.qolhunters.mixin.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.searchablevaultstations.SearchableScreen;
import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.block.entity.base.ForgeRecipeTileEntity;
import iskallia.vault.client.gui.framework.element.ButtonElement;
import iskallia.vault.client.gui.framework.render.Tooltips;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRenderFunction;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.screen.block.base.ForgeRecipeContainerScreen;
import iskallia.vault.container.spi.ForgeRecipeContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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

import java.util.List;

@Mixin(value = ForgeRecipeContainerScreen.class, remap = false)
public abstract class MixinForgeRecipeContainerScreen<V extends ForgeRecipeTileEntity, T extends ForgeRecipeContainer<V>> extends AbstractElementContainerScreen<T> {

    @Inject(method="keyPressed", at=@At(value="HEAD"), cancellable=true, remap = true)
    public void keyPressed(int pKeyCode, int pScanCode, int pModifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof SearchableScreen searchableScreen){
            var searchBox = searchableScreen.qolhunters$getSearchBox();
            if (searchBox != null && searchBox.isFocused()) {
                return;
            }
        }
        if(pKeyCode == KeyBindings.FORGE_ITEM.getKey().getValue() && Boolean.TRUE.equals(QOLHuntersClientConfigs.VAULT_INTERFACE_KEYBINDS.get())) {
            this.onCraftClick();
            cir.setReturnValue(true);
        }
    }


    @Inject(method = "onCraftClick", at = @At(value = "HEAD"))
    public void onCraftClick(CallbackInfo ci) {
        if (this.getMenu().getResultSlot().hasItem() && Screen.hasShiftDown() && Boolean.TRUE.equals(QOLHuntersClientConfigs.VAULT_INTERFACE_KEYBINDS.get())) {
            this.slotClicked(this.getMenu().getResultSlot(), this.getMenu().getResultSlot().index, 0, ClickType.QUICK_MOVE);
        }
        if (this.getMenu().getResultSlot().hasItem() && Screen.hasControlDown() && Boolean.TRUE.equals(QOLHuntersClientConfigs.VAULT_INTERFACE_KEYBINDS.get())) {
            this.slotClicked(this.getMenu().getResultSlot(), this.getMenu().getResultSlot().index, 0, ClickType.THROW);
        }
    }


    @Shadow @Final protected ButtonElement<?> craftButton;

    @Shadow protected abstract void onCraftClick();

    @Inject(method="containerTick", at=@At("TAIL"), remap = true)
    protected void qol$containerTick(CallbackInfo ci) {
        MutableComponent textMove = new TextComponent("Shift")
            .withStyle(Screen.hasShiftDown() ? ChatFormatting.WHITE : ChatFormatting.GRAY)
            .append(new TextComponent(" to Move").withStyle(ChatFormatting.DARK_GRAY));

        MutableComponent textDrop = new TextComponent("Ctrl")
            .withStyle(Screen.hasControlDown() && !Screen.hasShiftDown() ? ChatFormatting.WHITE : ChatFormatting.GRAY)
            .append(new TextComponent(" to Drop").withStyle(ChatFormatting.DARK_GRAY));

        MutableComponent textCraft = KeyBindings.FORGE_ITEM.getTranslatedKeyMessage().copy()
            .withStyle(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), KeyBindings.FORGE_ITEM.getKey().getValue()) ? ChatFormatting.WHITE : ChatFormatting.GRAY)
            .append(new TextComponent(" to Craft").withStyle(ChatFormatting.DARK_GRAY));

        List<Component> tooltip = List.of(textMove, textDrop, textCraft);

        if (!craftButton.isDisabled()) {
            craftButton.tooltip(Tooltips.multi(() -> tooltip));
        } else {
            craftButton.tooltip(ITooltipRenderFunction.NONE);
        }
    }


    protected MixinForgeRecipeContainerScreen(T container, Inventory inventory, Component title, IElementRenderer elementRenderer, ITooltipRendererFactory<AbstractElementContainerScreen<T>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }
}