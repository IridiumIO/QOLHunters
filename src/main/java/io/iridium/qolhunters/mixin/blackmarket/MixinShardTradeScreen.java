package io.iridium.qolhunters.mixin.blackmarket;

import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.helper.UIHelper;
import iskallia.vault.client.gui.screen.ShardTradeScreen;
import iskallia.vault.container.inventory.ShardTradeContainer;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.ItemShardPouch;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.iridium.qolhunters.util.SharedFunctions.formatNumber;

@Mixin(ShardTradeScreen.class)
public abstract class MixinShardTradeScreen extends AbstractElementContainerScreen<ShardTradeContainer> {

    protected MixinShardTradeScreen(ShardTradeContainer container, Inventory inventory, Component title, IElementRenderer elementRenderer, ITooltipRendererFactory<AbstractElementContainerScreen<ShardTradeContainer>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }

    @Inject(method="render", at=@At("RETURN"))
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {

        if(!QOLHuntersClientConfigs.BLACK_MARKET_SHARD_POUCH_COUNT.get()) return;

        Integer shardCount = ItemShardPouch.getShardCount(Minecraft.getInstance().player);
        String shardAmount = QOLHuntersClientConfigs.BLACK_MARKET_SHARD_POUCH_COUNT_SHORTHAND.get() ? formatNumber(shardCount) : String.valueOf(shardCount);

        poseStack.pushPose();
        poseStack.translate(this.leftPos + 24.0F, this.topPos + 23.0F, 0.0);
//        poseStack.scale(0.9f, 0.9f, 0.5f);
        MutableComponent c2 = new TextComponent(shardAmount).withStyle(Style.EMPTY.withColor(0x6F09C2));
        UIHelper.renderWrappedText(poseStack, c2, 80, 0);
        poseStack.popPose();

        // Render item icon
        ItemStack itemStack = new ItemStack(ModItems.SHARD_POUCH.asItem());
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(itemStack, this.leftPos + 4, this.topPos + 18);


    }

}
