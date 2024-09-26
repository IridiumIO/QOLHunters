package io.iridium.qolhunters.events;

import com.simibubi.create.foundation.config.ui.ConfigHelper;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.networking.ModMessages;
import io.iridium.qolhunters.networking.packet.HandshakeCheckModIsOnServerC2SPacket;
import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.ServerboundMagnetToggleMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static io.iridium.qolhunters.QOLHunters.MOD_ID;
import static io.iridium.qolhunters.util.SharedFunctions.displayMessageOnScreen;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public  class ClientForgeEvents {
    public static QOLHunters.ModMode MOD_MODE = QOLHunters.ModMode.CLIENTONLY;

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {

        if (event.getKey() == KeyBindings.TOGGLE_MAGNET_GUI.getKey().getValue() && event.getAction() == GLFW.GLFW_PRESS && Minecraft.getInstance().screen != null) {
            ModNetwork.CHANNEL.sendToServer(ServerboundMagnetToggleMessage.INSTANCE);
        }

        if (KeyBindings.OPEN_CONFIG.consumeClick()) {
            SubMenuConfigScreen screen = SubMenuConfigScreen.find(ConfigHelper.ConfigPath.parse("qolhunters:client.Client-Only Extensions"));
            Minecraft.getInstance().setScreen(screen);
        }

        if (event.getKey() == GLFW.GLFW_KEY_O && event.getAction() == GLFW.GLFW_PRESS &&
                (event.getModifiers() & GLFW.GLFW_MOD_SHIFT) != 0 &&
                (event.getModifiers() & GLFW.GLFW_MOD_CONTROL) != 0 &&
                (event.getModifiers() & GLFW.GLFW_MOD_ALT) != 0) {
            displayMessageOnScreen(new TextComponent("OBS MODE ENABLED").withStyle(ChatFormatting.RED));
        }


    }


    @SubscribeEvent
    public static void onJoinServer(ClientPlayerNetworkEvent.LoggedInEvent event) {

        MOD_MODE = QOLHunters.ModMode.CLIENTONLY;
        ModMessages.sendToServer(new HandshakeCheckModIsOnServerC2SPacket());


    }
}
