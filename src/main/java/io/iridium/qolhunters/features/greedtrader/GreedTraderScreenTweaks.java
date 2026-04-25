package io.iridium.qolhunters.features.greedtrader;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import iskallia.vault.client.gui.framework.ScreenTextures;
import iskallia.vault.client.gui.screen.GreedTraderScreen;
import iskallia.vault.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class GreedTraderScreenTweaks {
    public static void renderCoins(GreedTraderScreen traderScreen, PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (!(traderScreen instanceof GreedTraderScreen.Shop)) return;

        int coins = traderScreen.getMenu().getPlayerCoinCount();
        Minecraft minecraft = Minecraft.getInstance();
        Component numberComponent = (new TextComponent(String.valueOf(coins))).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x744f2c)));

        int restockBtnWidth = ScreenTextures.CYCLE.width();
        int restockBtnX = 147 - restockBtnWidth - 7;
        int numberWidth = minecraft.font.width(numberComponent);
        int guiLeft = traderScreen.getGuiLeft();
        int x = guiLeft + restockBtnX - numberWidth - 5;
        int y = traderScreen.getGuiTop() + 7;
        minecraft.font.draw(poseStack, numberComponent, x, y, 16777215);

        var renderStack = RenderSystem.getModelViewStack();
        renderStack.pushPose();
        renderStack.translate(x - 9 , y - 0.5, 0);
        renderStack.scale(0.5F, 0.5F, 1);
        RenderSystem.applyModelViewMatrix();
        minecraft.getItemRenderer().renderAndDecorateItem(new ItemStack(ModItems.GREED_COIN), 0, 0);
        renderStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }


    public static int getShopOfferHeight(int offerCount, int scrollListHeight) {
        int offersInCol =offerCount / 2 + offerCount % 2;
        int offerHeight =  scrollListHeight / offersInCol ;
        offerHeight -= offerHeight % 2;
        offerHeight = Mth.clamp(offerHeight, 18, 38);
        return offerHeight;
    }
}
