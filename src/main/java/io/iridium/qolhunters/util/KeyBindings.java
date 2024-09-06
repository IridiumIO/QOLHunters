package io.iridium.qolhunters.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String KEY_CATEGORY = "key.categories.qolhunters";
    public static final String KEY_FORGE = "key.qolhunters.forge";

    public static final KeyMapping FORGE_ITEM = new KeyMapping(KEY_FORGE, KeyConflictContext.GUI, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_SPACE, KEY_CATEGORY);

    public static void init() {
        ClientRegistry.registerKeyBinding(FORGE_ITEM);
    }

}
