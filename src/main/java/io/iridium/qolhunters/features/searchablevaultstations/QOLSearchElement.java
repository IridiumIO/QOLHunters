package io.iridium.qolhunters.features.searchablevaultstations;

import iskallia.vault.client.gui.framework.element.TextInputElement;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.framework.spatial.Spatials;
import iskallia.vault.client.gui.framework.spatial.spi.ISpatial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

public class QOLSearchElement extends TextInputElement<QOLSearchElement> {
    public QOLSearchElement(ISpatial spatial, Font font) {
        super(spatial, font);
        this.adjustEditBox(editBox -> editBox.setMaxLength(75));
    }


    @Override public boolean onMouseClicked(double mouseX, double mouseY, int buttonIndex) {
        if (buttonIndex == 1) { // right
            this.setInput("");
        }
        return super.onMouseClicked(mouseX, mouseY, buttonIndex);
    }

    /**
     * Creates a search element positioned on the right side of the screen.
     * @param abstractElementContainerScreen parent screen
     * @param searchWidth width of the search box
     * @param offsetX offset from the right side of the screen
     * @param offsetY offset from the top of the screen
     * @return a new QOLSearchElement positioned on the right side of the screen
     */
    public static QOLSearchElement createRight(AbstractElementContainerScreen<?> abstractElementContainerScreen, int searchWidth, int offsetX, int offsetY) {
        int right = abstractElementContainerScreen.getGuiLeft() + abstractElementContainerScreen.getXSize();
        return new QOLSearchElement(Spatials.positionXY(right - searchWidth + offsetX, offsetY).size(searchWidth, 10), Minecraft.getInstance().font
        ).layout((screen, gui, parent, world) -> world.translateXY(gui));
    }

    /**
     * Creates a search element positioned on the left side of the screen.
     * @param abstractElementContainerScreen parent screen
     * @param searchWidth width of the search box
     * @param offsetX offset from the left side of the screen
     * @param offsetY offset from the top of the screen
     * @return a new QOLSearchElement positioned on the left side of the screen
     */
    public static QOLSearchElement createLeft(AbstractElementContainerScreen<?> abstractElementContainerScreen, int searchWidth, int offsetX, int offsetY) {
        int left = abstractElementContainerScreen.getGuiLeft();
        return new QOLSearchElement(Spatials.positionXY(left + offsetX, offsetY).size(searchWidth, 10), Minecraft.getInstance().font
        ).layout((screen, gui, parent, world) -> world.translateXY(gui));
    }
}