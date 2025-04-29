package io.iridium.qolhunters.mixin.carddeck;

import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.framework.spatial.Spatials;
import iskallia.vault.client.gui.screen.CardDeckScreen;
import iskallia.vault.client.gui.screen.block.TooltipContainerElement;
import iskallia.vault.container.inventory.CardDeckContainerMenu;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CardDeckScreen.class)
public class MixinCardDeckScreen extends AbstractElementContainerScreen<CardDeckContainerMenu> implements MenuAccess<CardDeckContainerMenu> {

    @Mutable
    @Shadow(remap = false) @Final
    private TooltipContainerElement tooltip;


    protected MixinCardDeckScreen(CardDeckContainerMenu container, Inventory inventory, Component title, IElementRenderer elementRenderer, ITooltipRendererFactory<AbstractElementContainerScreen<CardDeckContainerMenu>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }

    @Inject(method="<init>", at=@At("RETURN"))
    public void init(CardDeckContainerMenu menu, Inventory playerInventory, Component title, CallbackInfo ci) {

        this.elementStore.getGuiEventElementList().stream()
                .filter(element -> element instanceof TooltipContainerElement)
                .findFirst().ifPresent(this::removeElement);

        this.tooltip = this.addElement(
                new TooltipContainerElement(Spatials.positionXY(-116, 0).height(184).width(107), ItemStack.EMPTY)
                        .layout((screen, gui, parent, world) -> world.translateXYZ(gui))
        );

    }

}
