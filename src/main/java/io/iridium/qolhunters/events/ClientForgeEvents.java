package io.iridium.qolhunters.events;

import com.simibubi.create.foundation.config.ui.ConfigHelper;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.ServerboundMagnetToggleMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static io.iridium.qolhunters.QOLHunters.MOD_ID;
import static io.iridium.qolhunters.util.SharedFunctions.displayMessageOnScreen;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public  class ClientForgeEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {

        if (KeyBindings.TOGGLE_MAGNET_GUI.consumeClick()) {
            ModNetwork.CHANNEL.sendToServer(ServerboundMagnetToggleMessage.INSTANCE);
        }

        if (KeyBindings.OPEN_CONFIG.consumeClick()) {
            SubMenuConfigScreen screen = SubMenuConfigScreen.find(ConfigHelper.ConfigPath.parse("qolhunters:client.Client-Only Extensions"));
            Minecraft.getInstance().setScreen(screen);
        }
    }

}
