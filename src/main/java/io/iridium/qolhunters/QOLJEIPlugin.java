package io.iridium.qolhunters;

import iskallia.vault.client.gui.screen.CardDeckScreen;
import iskallia.vault.client.gui.screen.block.VaultJewelApplicationStationScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@JeiPlugin
public class QOLJEIPlugin implements IModPlugin {

    private static IJeiRuntime runtime;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(QOLHunters.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(VaultJewelApplicationStationScreen.class,
                new IGuiContainerHandler<VaultJewelApplicationStationScreen>() {
                    @Override
                    public List<Rect2i> getGuiExtraAreas(VaultJewelApplicationStationScreen screen) {
                        return java.util.Collections.singletonList(new Rect2i(screen.getGuiLeft() + 234, screen.getGuiTop() + 16, 107, 169));
                    }
                });

        registration.addGenericGuiContainerHandler(CardDeckScreen.class,
                new IGuiContainerHandler<CardDeckScreen>() {
                    @Override
                    public List<Rect2i> getGuiExtraAreas(CardDeckScreen screen) {
                        return java.util.Collections.singletonList(new Rect2i(screen.getGuiLeft() - 120, screen.getGuiTop() -5, 107, 189));
                    }
                });

    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        QOLJEIPlugin.runtime = runtime;
    }

    public static IJeiRuntime getRuntime() {
        return runtime;
    }

}