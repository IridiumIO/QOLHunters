package io.iridium.qolhunters.features.abilitykeybinds;

import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.QOLHunters;
import iskallia.vault.client.gui.screen.player.AbilitiesElementContainerScreen;
import iskallia.vault.skill.base.SpecializedSkill;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
public class AbilityTabKeyBinding {

    public static boolean isAbilityHovered = false;

    public static SpecializedSkill node = null;
    public static KeyMapping keyMapping = null;

    private static boolean keyPressed = false;
    private static boolean mousePressed = false;


    private static long lastRefresh = 0;

    public static void refresh() {
        lastRefresh = System.currentTimeMillis();
    }

    private static boolean isOutDated() {
        return System.currentTimeMillis() - lastRefresh > 100;
    }

    @SubscribeEvent
    public static void onClientTick(ScreenEvent.DrawScreenEvent event) {
        if(!(Minecraft.getInstance().screen instanceof AbilitiesElementContainerScreen)) return;
        if(!isAbilityHovered || node == null || keyMapping == null) return;
        if(isOutDated()) resetClass();
    }

    @SubscribeEvent
    public static void onKeyInput(ScreenEvent.KeyboardKeyPressedEvent.Pre event) {
        if(!isAbilityHovered || node == null || keyMapping == null || keyPressed) return;
        if(!(Minecraft.getInstance().screen instanceof AbilitiesElementContainerScreen)) return;

        keyPressed = true;

        if(event.getKeyCode() == GLFW.GLFW_KEY_ESCAPE) {
            setKeyMapping(InputConstants.UNKNOWN);
        }else{
            setKeyMapping(InputConstants.getKey(event.getKeyCode(), 0));
        }
        resetClass();
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onMouseInput(ScreenEvent.MouseClickedEvent.Pre event) {
        if(!isAbilityHovered || node == null || keyMapping == null || mousePressed) return;
        if(!(Minecraft.getInstance().screen instanceof AbilitiesElementContainerScreen)) return;

        mousePressed = true;

        if(event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT || event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) return;
        setKeyMapping(InputConstants.Type.MOUSE.getOrCreate(event.getButton()));
        resetClass();
        event.setCanceled(true);
    }

    private static void setKeyMapping(InputConstants.Key key){
        keyMapping.setKey(key);
        KeyMapping.resetMapping();
    }

    private static void resetClass() {
        keyMapping = null;
        node = null;
        isAbilityHovered = false;
    }


    @SubscribeEvent
    public static void onKeyRelease(InputEvent.KeyInputEvent event) {
        keyPressed = false;
    }

    @SubscribeEvent
    public static void onMouseRelease(InputEvent.MouseInputEvent event) {
        mousePressed = false;
    }
}
