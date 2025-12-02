package io.iridium.qolhunters.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import io.iridium.qolhunters.features.vault_scavenger.NamedItem;
import io.iridium.qolhunters.features.vault_scavenger.Scavenger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.GuiUtils;

public class SharedFunctions {

    public static String formatNumber(int number) {
        if (number >= 10_000_000) {
            return String.format("%.1fM", number / 1_000_000_000.0);
        } else
        if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 100_000) {
                return String.format("%.0fK", number / 1_000.0);
        } else if (number >= 10_000) {
            return String.format("%.1fK", number / 1_000.0);
        } else if (number >= 1_000) {
            return String.format("%.2fK", number / 1_000.0);
        } else {
            return String.valueOf(number);
        }
    }

    public static String formatNumberWithDecimal(double number) {
        if (number >= 10_000_000) {
            return String.format("%.1fM", number / 1_000_000_000.0);
        } else
        if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 100_000) {
            return String.format("%.0fK", number / 1_000.0);
        } else if (number >= 10_000) {
            return String.format("%.1fK", number / 1_000.0);
        } else if (number >= 1_000) {
            return String.format("%.2fK", number / 1_000.0);
        } else if (number >= 100){
            return String.format("%.0f", number);
        }else {
            return String.format("%.2f", number);
        }
    }


    public static void renderSlotHighlight(PoseStack poseStack, ItemStack itemStack, int x, int y){

        var scavItem = NamedItem.of(itemStack);
        if (!Scavenger.scavengerColors.containsKey(scavItem)) return;


        RenderSystem.disableDepthTest();
        poseStack.pushPose();
        poseStack.translate(0, 0, 100);
        Matrix4f matrix = poseStack.last().pose();

        int color = 0xDD000000 | Scavenger.scavengerColors.get(scavItem);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(bufferBuilder);

        if(true){
            GuiUtils.drawGradientRect(matrix, -1, x,y + 4,x + 16,y + 16, 0x00000000, color);
        }
        else {
            GuiUtils.drawGradientRect(matrix, -1, x, y + 15,x + 16,y + 16, color, color);
        }

        bufferSource.endBatch();
        poseStack.popPose();


    }

    public static int DataSlotToNetworkSlot(int index) {
        if(index == 100)
            index = 8;
        else if(index == 101)
            index = 7;
        else if(index == 102)
            index = 6;
        else if(index == 103)
            index = 5;
        else if(index == -106)
            index = 45;
        else if(index <= 8)
            index += 36;
        else if(index >= 80 && index <= 83)
            index -= 79;
        return index;
    }


    @OnlyIn(Dist.CLIENT)
    public static void displayMessageOnScreen(Component message) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> mc.gui.setOverlayMessage(message, false));
    }

}
