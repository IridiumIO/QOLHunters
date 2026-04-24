package io.iridium.qolhunters.integration;

import com.jaquadro.minecraft.storagedrawers.inventory.DrawerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.fml.ModList;

public class DrawerIntegration {
    public static boolean isDrawer(AbstractContainerScreen<?> screen) {
        if (ModList.get().isLoaded("storagedrawers")) {
            return Unsafe.isDrawer(screen);
        }
        return false;
    }

    private static class Unsafe {
        private static boolean isDrawer(AbstractContainerScreen<?> screen){
            return screen instanceof DrawerScreen;
        }
    }
}
