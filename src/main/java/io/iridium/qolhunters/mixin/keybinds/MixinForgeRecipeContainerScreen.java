package io.iridium.qolhunters.mixin.keybinds;

import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.block.entity.base.ForgeRecipeTileEntity;
import iskallia.vault.client.gui.screen.CatalystInfusionTableScreen;
import iskallia.vault.client.gui.screen.block.InscriptionTableScreen;
import iskallia.vault.client.gui.screen.block.ToolStationScreen;
import iskallia.vault.client.gui.screen.block.VaultForgeScreen;
import iskallia.vault.client.gui.screen.block.base.ForgeRecipeContainerScreen;
import iskallia.vault.container.spi.ForgeRecipeContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Mixin(ForgeRecipeContainerScreen.class)
public abstract class MixinForgeRecipeContainerScreen<V extends ForgeRecipeTileEntity, T extends ForgeRecipeContainer<V>>  {


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
                onCraftClickMethod.invoke((ForgeRecipeContainerScreen<V,T>)(Object)this);


                cir.setReturnValue(true);
            }
        }

    }

}
