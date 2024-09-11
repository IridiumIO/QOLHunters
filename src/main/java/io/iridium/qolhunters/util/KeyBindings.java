package io.iridium.qolhunters.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.checkerframework.checker.units.qual.K;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String KEY_CATEGORY = "key.categories.qolhunters";
    public static final String KEY_FORGE = "key.qolhunters.forge";
    public static final String KEY_CAKE_OVERLAY_COLOR = "key.qolhunters.cake_overlay_color";
    public static final String KEY_CAKE_OVERLAY_STYLE = "key.qolhunters.cake_overlay_style";

    public static final KeyMapping FORGE_ITEM = new KeyMapping(KEY_FORGE, KeyConflictContext.GUI, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_SPACE, KEY_CATEGORY);

    public static final KeyMapping TOGGLE_CAKE_OVERLAY_COLOR = new KeyMapping(KEY_CAKE_OVERLAY_COLOR, KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_EQUAL, KEY_CATEGORY);
    public static final KeyMapping TOGGLE_CAKE_OVERLAY_STYLE = new KeyMapping(KEY_CAKE_OVERLAY_STYLE, KeyConflictContext.IN_GAME, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_MINUS, KEY_CATEGORY);

    public static void init() {

        ClientRegistry.registerKeyBinding(FORGE_ITEM);
        ClientRegistry.registerKeyBinding(TOGGLE_CAKE_OVERLAY_COLOR);
        ClientRegistry.registerKeyBinding(TOGGLE_CAKE_OVERLAY_STYLE);
    }

}
