package io.iridium.qolhunters.mixin.cardessenceextractor;

import com.mojang.blaze3d.vertex.PoseStack;
import iskallia.vault.block.entity.CardEssenceExtractorTileEntity;
import iskallia.vault.client.gui.framework.render.Tooltips;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.screen.block.CardEssenceExtractorScreen;
import iskallia.vault.config.CardEssenceExtractorConfig;
import iskallia.vault.container.inventory.CardEssenceExtractorContainer;
import iskallia.vault.core.card.Card;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.CardItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CardEssenceExtractorScreen.class)
public abstract class MixinCardEssenceExtractorScreen extends AbstractElementContainerScreen<CardEssenceExtractorContainer> {


    protected MixinCardEssenceExtractorScreen(CardEssenceExtractorContainer container, Inventory inventory, Component title, IElementRenderer elementRenderer, ITooltipRendererFactory<AbstractElementContainerScreen<CardEssenceExtractorContainer>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }

    @Shadow(remap = false)
    protected abstract boolean canUpgrade();



    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    private boolean upgradeCardTooltip(ITooltipRenderer tooltipRenderer, @NotNull PoseStack poseStack, int mouseX, int mouseY, TooltipFlag tooltipFlag) {
        if (true) {
            CardEssenceExtractorTileEntity tile = this.getMenu().getTileEntity();
            if (tile != null && !tile.isRemoved()) {
                ItemStack upgradeable = tile.getCardUpgradeStack();
                if (!upgradeable.isEmpty() && upgradeable.getItem() instanceof CardItem) {
                    Card card = CardItem.getCard(upgradeable);
                    int tier = card.getTier();
                    CardEssenceExtractorConfig.TierConfig cfg = ModConfigs.CARD_ESSENCE_EXTRACTOR.getConfig(tier).orElse(null);
                    if (cfg == null) {
                        return false;
                    } else if (tile.getEssence() < cfg.getEssencePerUpgrade()) {
                        return Tooltips.single(() -> new TextComponent("Required Essence: " + cfg.getEssencePerUpgrade()).withStyle(ChatFormatting.RED))
                                .onHoverTooltip(tooltipRenderer, poseStack, mouseX, mouseY, tooltipFlag);
                    }else {
                        return !card.canUpgrade()
                                ? Tooltips.single(() -> new TextComponent("Maximum Tier Reached").withStyle(ChatFormatting.RED))
                                .onHoverTooltip(tooltipRenderer, poseStack, mouseX, mouseY, tooltipFlag)
                                : Tooltips.single(() -> new TextComponent("Required Essence: " + cfg.getEssencePerUpgrade()).withStyle(ChatFormatting.GREEN))
                                .onHoverTooltip(tooltipRenderer, poseStack, mouseX, mouseY, tooltipFlag);
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}
