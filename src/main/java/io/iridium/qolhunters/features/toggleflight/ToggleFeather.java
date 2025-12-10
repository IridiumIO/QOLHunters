package io.iridium.qolhunters.features.toggleflight;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.util.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
public class ToggleFeather {

    public static Minecraft mc = Minecraft.getInstance();
    public static boolean featherEnabled = true;

    private static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(QOLHunters.MOD_ID, "textures/hud/feather_disabled.png");

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.TOGGLE_FEATHER.consumeClick()) {
            featherEnabled = !featherEnabled;
        }
    }


    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL && !featherEnabled) {
            renderIcon(event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight(), event.getMatrixStack());
        }
    }

    private static void renderIcon(int screenWidth, int screenHeight, PoseStack poseStack) {
        int iconSize = 16;
        int x = 78;
        int y = screenHeight - iconSize - 2;

        RenderSystem.setShaderTexture(0, ICON);
        GuiComponent.blit(poseStack, x, y, 0, 0, iconSize, iconSize, iconSize, iconSize);

    }

}
