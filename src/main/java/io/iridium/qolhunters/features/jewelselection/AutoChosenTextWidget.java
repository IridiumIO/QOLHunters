package io.iridium.qolhunters.features.jewelselection;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;

public class AutoChosenTextWidget implements Widget {

    private final float x;
    private final int height;
    public AutoChosenTextWidget(float x, int height) {
        this.x = x;
        this.height = height;
    }

    @Override public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        int scale = 4; // hardcoded in the jewel renderer
        int texSize = 16;
        double screenVerticalCenter = (double) height / 2;
        String symbol = "⌄";
        float symbolWidth = Minecraft.getInstance().font.width(symbol);
        float symbolHeight = Minecraft.getInstance().font.lineHeight;

        int offset = texSize / 2 * scale + 2 * scale;


        poseStack.pushPose();
        poseStack.translate(x + 27 - symbolWidth / 2, screenVerticalCenter - offset - symbolHeight/2, 0);
        poseStack.scale(2,2,0);
        Minecraft.getInstance().font.draw(poseStack, symbol, 0, 0, ChatFormatting.DARK_GRAY.getColor());
        poseStack.popPose();
    }
}
