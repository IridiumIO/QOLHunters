package io.iridium.qolhunters;

import com.mojang.logging.LogUtils;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.interfaces.SuperCakeObjective;
import io.iridium.qolhunters.util.KeyBindings;
import io.iridium.qolhunters.util.SharedFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.BackpackScreen;
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

            if(KeyBindings.TOGGLE_CAKE_OVERLAY_COLOR.consumeClick()) {
                    SuperCakeObjective.qol$colorIndex = (SuperCakeObjective.qol$colorIndex + 1) % SuperCakeObjective.COLORMAP.size();
                    Style style = Style.EMPTY.withColor(SuperCakeObjective.COLORMAP.get(SuperCakeObjective.qol$colorIndex));
                    displayMessageOnScreen(new TextComponent("Changed Cake Overlay Color").withStyle(style));
                    QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_COLOR.set(SuperCakeObjective.qol$colorIndex);
            }

            if(KeyBindings.TOGGLE_CAKE_OVERLAY_STYLE.consumeClick()) {
                SuperCakeObjective.qol$overlayStyle = (SuperCakeObjective.qol$overlayStyle + 1) % 2;
                Style style = Style.EMPTY.withColor(SuperCakeObjective.COLORMAP.get(SuperCakeObjective.qol$colorIndex));
                String styleText = SuperCakeObjective.qol$overlayStyle == 0 ? "Vignette" : "Radar";
                displayMessageOnScreen(new TextComponent("Changed Cake Overlay Style: " + styleText).withStyle(style));
                QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_STYLE.set(SuperCakeObjective.qol$overlayStyle);
            }
        }

//        @SubscribeEvent
//        public static void onItemPickup(EntityItemPickupEvent event) {
//            ItemStack stack = event.getItem().getItem();
//            QOLHunters.LOGGER.info("Item Pickup Event: " + event.getItem().getItem().getItem().getRegistryName());
//            QOLHunters.LOGGER.info("Item Pickup Event: " + stack.getCount());
//            if (SharedFunctions.isScavengerItem(stack)) {
//                int currentCount = SharedFunctions.ScavengerItems.getOrDefault(stack.getItem(), 0);
//                SharedFunctions.ScavengerItems.put(stack.getItem(), currentCount + stack.getCount());
//            }
//        }
//
//        @SubscribeEvent
//        public static void onItemDrop(ItemTossEvent event) {
//            ItemStack stack = event.getEntityItem().getItem();
//            if (SharedFunctions.isScavengerItem(stack)) {
//                int currentCount = SharedFunctions.ScavengerItems.getOrDefault(stack.getItem(), 0);
//                int newCount = Math.max(0, currentCount - stack.getCount());
//                SharedFunctions.ScavengerItems.put(stack.getItem(), newCount);
//            }
//        }
//
//
//        @SubscribeEvent
//        public static void onScreenOpen(ScreenOpenEvent event) {
//            if (event.getScreen() instanceof BackpackScreen) {
//                SharedFunctions.OverWriteScavengerItems(Minecraft.getInstance().player);
//                displayMessageOnScreen(new TextComponent("Backpack Item Opened"));
//            }
//        }


    }


    private static void displayMessageOnScreen(Component message) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> {
            mc.gui.setOverlayMessage(message, false);
        });
    }

}




