package io.iridium.qolhunters;

import com.mojang.logging.LogUtils;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.interfaces.SuperCakeObjective;
import io.iridium.qolhunters.util.KeyBindings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;


@Mod(QOLHunters.MOD_ID)
public class QOLHunters {

    public static final String MOD_ID = "qolhunters";
    public static final Logger LOGGER = LogUtils.getLogger();

    public QOLHunters() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, QOLHuntersClientConfigs.CLIENT_SPEC, "qolhunters-client.toml");

    }

    private void clientSetup(final FMLClientSetupEvent event) {
        KeyBindings.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.KeyInputEvent event) {
            LOGGER.info("Key input event:  " + event.getKey());

            if(event.getModifiers() == GLFW.GLFW_MOD_CONTROL && event.getKey() == GLFW.GLFW_KEY_EQUAL && event.getAction() == GLFW.GLFW_RELEASE) {
                    SuperCakeObjective.qol$colorIndex = (SuperCakeObjective.qol$colorIndex + 1) % 3;

//                    //send player message in chat
//                    Player player = Minecraft.getInstance().player;
//                    if(player != null) {
//                        Style style = Style.EMPTY.withColor(SuperCakeObjective.qOLHunters$colorMap.get(SuperCakeObjective.qol$colorIndex));
//                        player.displayClientMessage(new TextComponent("Changed Cake Overlay Color").withStyle(style), false);
//                      }

            }
        }
    }

}




