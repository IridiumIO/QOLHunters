package io.iridium.qolhunters.features.dungeondoors;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.QOLHunters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
public class DungeonDifficultyWidget {

    public static boolean isDungeonActive = false;

    public static BlockPos dungeonPos = null;

    public static String difficulty = "GH";
    public static int difficultyColor = 0x00FF00; // Green

    //TODO: Add a map to store the dungeon doors and their difficulties

    public static AABB getDungeonAABB() {
        if (dungeonPos == null) return null;
        return new AABB(dungeonPos.offset(-30, -15, -30), dungeonPos.offset(30, 20, 30));
    }

    public static boolean isPlayerNearDungeon(BlockPos playerPos) {
        if (dungeonPos == null) return false;
        return getDungeonAABB().contains(playerPos.getX(), playerPos.getY(), playerPos.getZ());
    }


    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent event) {
//        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        if(isPlayerNearDungeon(Minecraft.getInstance().player.blockPosition())) {
            if(isDungeonActive) {
                drawText(event.getMatrixStack(), difficulty);
            }else{
            }
        }

    }

    private static void drawText(PoseStack matrixStack, String text) {
        Minecraft mc = Minecraft.getInstance();
        Font fontRenderer = mc.font;
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        int x = width / 2 - fontRenderer.width(text) / 2;
        int y = height -65;

        RenderSystem.enableBlend();
        fontRenderer.draw(matrixStack, text, x, y, difficultyColor);
        RenderSystem.disableBlend();
    }


}
