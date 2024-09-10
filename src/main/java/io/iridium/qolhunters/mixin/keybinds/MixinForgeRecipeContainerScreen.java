package io.iridium.qolhunters.mixin.keybinds;

import io.iridium.qolhunters.mixin.accessors.AccessorAbstractContainerScreen;
import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.block.entity.base.ForgeRecipeTileEntity;
import iskallia.vault.client.gui.framework.element.ButtonElement;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.screen.CatalystInfusionTableScreen;
import iskallia.vault.client.gui.screen.block.InscriptionTableScreen;
import iskallia.vault.client.gui.screen.block.ToolStationScreen;
import iskallia.vault.client.gui.screen.block.VaultForgeScreen;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Mixin(ForgeRecipeContainerScreen.class)
public abstract class MixinForgeRecipeContainerScreen<V extends ForgeRecipeTileEntity, T extends ForgeRecipeContainer<V>> extends AbstractElementContainerScreen<T> {

    //Store a list of all screens that are instances of the VaultForgeScreen class
    private static final List<Class> ForgeRecipeContainerScreens = Arrays.asList(
            VaultForgeScreen.class,
            ToolStationScreen.class,
            CatalystInfusionTableScreen.class,
            InscriptionTableScreen.class
    );

    private static boolean isInstanceOfForgeRecipeContainerScreen(Object screen) {
        for (Class<?> clazz : ForgeRecipeContainerScreens) {
            if (clazz.isInstance(screen)) {
                return true;
            }
        }
        return false;
    }


    @Inject(method="keyPressed", at=@At(value="HEAD"), cancellable=true)
    public void keyPressed(int pKeyCode, int pScanCode, int pModifiers, CallbackInfoReturnable<Boolean> cir) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        if((isInstanceOfForgeRecipeContainerScreen((ForgeRecipeContainerScreen<V,T>)(Object)this))) {

            if(pKeyCode == KeyBindings.FORGE_ITEM.getKey().getValue()) {

                Method onCraftClickMethod = ForgeRecipeContainerScreen.class.getDeclaredMethod("onCraftClick");
                onCraftClickMethod.setAccessible(true);
                onCraftClickMethod.invoke((ForgeRecipeContainerScreen<V, T>) (Object) this);
                cir.setReturnValue(true);
            }
        }

    }


    @Inject(method = "onCraftClick", at = @At(value = "HEAD"), remap = false)
    public void onCraftClick(CallbackInfo ci) {
        ForgeRecipeContainer<?> forgeRecipeContainer = ((ForgeRecipeContainerScreen<V, T>) (Object) this).getMenu();

        if (forgeRecipeContainer.getResultSlot().hasItem()) {
            if (Screen.hasShiftDown()) {
                ((AccessorAbstractContainerScreen) this).invokeSlotClicked(forgeRecipeContainer.getResultSlot(), forgeRecipeContainer.getResultSlot().index, 0, ClickType.QUICK_MOVE);
            }
        }
    }


    @Shadow(remap = false) @Final
    private ButtonElement<?> craftButton;

    @Inject(method="containerTick", at=@At("TAIL"))
    protected void qol$containerTick(CallbackInfo ci) {
        MutableComponent text = new TextComponent("Hold ").withStyle(ChatFormatting.DARK_GRAY).append(new TextComponent("<SHIFT>").withStyle(ChatFormatting.GRAY)).append(" to Quick Move");
        if(!craftButton.isDisabled()) craftButton.tooltip(() -> text);
    }


    protected MixinForgeRecipeContainerScreen(T container, Inventory inventory, Component title, IElementRenderer elementRenderer, ITooltipRendererFactory<AbstractElementContainerScreen<T>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }
}