package io.iridium.qolhunters.features.elytra;

import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.util.KeyBindings;
import io.iridium.qolhunters.util.SharedFunctions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
public class ToggleElytra {

    public static boolean elytraEnabled = true;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.TOGGLE_ELYTRA.consumeClick()) {
            elytraEnabled = !elytraEnabled;

            Component message = elytraEnabled ? new TextComponent("Elytra enabled") : new TextComponent("Elytra disabled");
            SharedFunctions.displayMessageOnScreen(message);
        }
    }

}
