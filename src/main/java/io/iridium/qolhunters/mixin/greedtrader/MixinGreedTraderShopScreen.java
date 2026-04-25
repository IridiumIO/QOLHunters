package io.iridium.qolhunters.mixin.greedtrader;

import com.llamalad7.mixinextras.sugar.Local;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.greedtrader.GreedTraderScreenTweaks;
import iskallia.vault.client.gui.screen.GreedTraderScreen;
import iskallia.vault.container.GreedTraderContainer;
import iskallia.vault.world.data.PlayerGreedTraderData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.List;

@Mixin(value = GreedTraderScreen.Shop.class, remap = false)
public abstract class MixinGreedTraderShopScreen extends GreedTraderScreen{

    @Shadow private ScrollList shopScrollList;

    protected MixinGreedTraderShopScreen(GreedTraderContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }

    @ModifyConstant(method = "buildContent", constant = @Constant(intValue = 38))
    private int changeButtonHeight(int original, @Local(name = "offers") List<PlayerGreedTraderData.TradeOffer> offers) {
        if (!QOLHuntersClientConfigs.RESPONSIVE_GREED_TRADER_OFFERS.get()) return original;

        return GreedTraderScreenTweaks.getShopOfferHeight(offers.size(), this.shopScrollList.height());
    }

    @ModifyConstant(method = "buildContent", constant = @Constant(intValue = 11))
    private int changeStackHeight(int original, @Local(name = "offers") List<PlayerGreedTraderData.TradeOffer> offers) {
        if (!QOLHuntersClientConfigs.RESPONSIVE_GREED_TRADER_OFFERS.get()) return original;

        return (GreedTraderScreenTweaks.getShopOfferHeight(offers.size(), this.shopScrollList.height()) - 16) / 2;
    }

    @ModifyConstant(method = "buildContent", constant = @Constant(intValue = 34))
    private int changeCorderHeight(int original, @Local(name = "offers") List<PlayerGreedTraderData.TradeOffer> offers) {
        if (!QOLHuntersClientConfigs.RESPONSIVE_GREED_TRADER_OFFERS.get()) return original;

        return GreedTraderScreenTweaks.getShopOfferHeight(offers.size(), this.shopScrollList.height()) - 4;
    }
}
