package io.iridium.qolhunters.interfaces;

import iskallia.vault.client.gui.framework.element.DynamicLabelElement;
import iskallia.vault.client.gui.framework.spatial.spi.IPosition;
import iskallia.vault.client.gui.framework.spatial.spi.ISize;
import iskallia.vault.client.gui.framework.text.LabelTextStyle;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.function.Supplier;

public interface IVaultModifiersElement {


     class ChestStringElement extends DynamicLabelElement<Component, ChestStringElement> {
        public ChestStringElement(IPosition position, ISize size, Supplier<Component> valueSupplier, LabelTextStyle.Builder labelTextStyle) {
            super(position, size, valueSupplier, labelTextStyle);
        }

        protected void onValueChanged(Component value) {
            this.set(value);
        }
    }

     class ValueElement extends DynamicLabelElement<String, ValueElement> {
        public ValueElement(IPosition position, ISize size, String string, LabelTextStyle.Builder labelTextStyle) {
            super(position, size, () -> {
                return string;
            }, labelTextStyle);
        }

        protected void onValueChanged(String value) {
            this.set(new TextComponent(value));
        }
    }

}
