package io.iridium.qolhunters.mixin.jewelapplicationtable;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.framework.spatial.Spatials;
import iskallia.vault.client.gui.framework.spatial.spi.ISpatial;
import iskallia.vault.client.gui.screen.block.VaultJewelApplicationStationScreen;
import iskallia.vault.container.VaultJewelApplicationStationContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(VaultJewelApplicationStationScreen.class)
public class MixinVaultJewelApplicationStationScreen extends AbstractElementContainerScreen<VaultJewelApplicationStationContainer> {

    protected MixinVaultJewelApplicationStationScreen(VaultJewelApplicationStationContainer container, Inventory inventory, Component title, IElementRenderer elementRenderer, ITooltipRendererFactory<AbstractElementContainerScreen<VaultJewelApplicationStationContainer>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }

    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Liskallia/vault/client/gui/screen/block/VaultJewelApplicationStationScreen$ScrollableClickableItemStackSelectorElement;<init>(Liskallia/vault/client/gui/screen/block/VaultJewelApplicationStationScreen;Liskallia/vault/client/gui/framework/spatial/spi/ISpatial;ILiskallia/vault/client/gui/screen/block/VaultJewelApplicationStationScreen$ScrollableClickableItemStackSelectorElement$SelectorModel;)V"     ),
            index = 2
    )
    private int modifyJewelContainerSlotColumns(int slotColumns) {

        return QOLHuntersClientConfigs.BETTER_SCREEN_JEWEL_APPLICATION.get() ? 8 : slotColumns;
    }

    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Liskallia/vault/client/gui/screen/block/VaultJewelApplicationStationScreen$ScrollableClickableItemStackSelectorElement;<init>(Liskallia/vault/client/gui/screen/block/VaultJewelApplicationStationScreen;Liskallia/vault/client/gui/framework/spatial/spi/ISpatial;ILiskallia/vault/client/gui/screen/block/VaultJewelApplicationStationScreen$ScrollableClickableItemStackSelectorElement$SelectorModel;)V"     ),
            index = 1
    )
    private ISpatial modifyJewelContainerSpatial(ISpatial spatial) {
      return QOLHuntersClientConfigs.BETTER_SCREEN_JEWEL_APPLICATION.get() ? (Spatials.positionXY(57, 16).height(74)): spatial;
    }


    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Liskallia/vault/client/gui/screen/block/TooltipContainerElement;<init>(Liskallia/vault/client/gui/framework/spatial/spi/ISpatial;Lnet/minecraft/world/item/ItemStack;)V"     ),
            index = 0
    )
    private ISpatial modifyTooltipContainerSpatial(ISpatial spatial) {
        return QOLHuntersClientConfigs.BETTER_SCREEN_JEWEL_APPLICATION.get() ? (Spatials.positionXY(234, 16).height(169).width(107)) : spatial;
    }


    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Liskallia/vault/client/gui/framework/element/ToolItemSlotElement;<init>(Liskallia/vault/client/gui/framework/spatial/spi/ISpatial;Ljava/util/function/Supplier;Ljava/util/function/Supplier;II)V"),
            index = 0
    )
    private ISpatial modifyForgeButtonSpatial(ISpatial spatial) {
        return QOLHuntersClientConfigs.BETTER_SCREEN_JEWEL_APPLICATION.get() ? (Spatials.positionXY(8, 24)): spatial;

    }

}
