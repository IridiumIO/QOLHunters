package io.iridium.qolhunters.features.vault_scavenger;

import io.iridium.qolhunters.QOLHunters;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class Scavenger {

    public static Map<String, Integer> ScavengerItems = new HashMap<>();


    @Mod.EventBusSubscriber(modid= QOLHunters.MOD_ID, value = Dist.CLIENT)
    public static class ScavengerEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.KeyInputEvent event) {

            if (event.getKey() == GLFW.GLFW_KEY_Q && event.getAction() == GLFW.GLFW_PRESS &&
                    (event.getModifiers() & GLFW.GLFW_MOD_SHIFT) != 0 &&
                    (event.getModifiers() & GLFW.GLFW_MOD_CONTROL) != 0 &&
                    (event.getModifiers() & GLFW.GLFW_MOD_ALT) != 0)
            {
                Scavenger.ScavengerItems.clear();
                QOLHunters.LOGGER.info("Scavenger items cleared");
            }

        }

    }

}
