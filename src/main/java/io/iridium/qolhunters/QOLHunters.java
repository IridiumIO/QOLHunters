package io.iridium.qolhunters;

import com.mojang.logging.LogUtils;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.interfaces.SuperCakeObjective;
import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.core.data.key.ThemeKey;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.VaultRegistry;
import iskallia.vault.core.vault.WorldManager;
import iskallia.vault.core.vault.objective.Objectives;
import iskallia.vault.event.event.VaultJoinEvent;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.ServerboundMagnetToggleMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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

            if (KeyBindings.TOGGLE_CAKE_OVERLAY_COLOR.consumeClick()) {
                SuperCakeObjective.qol$colorIndex = (SuperCakeObjective.qol$colorIndex + 1) % SuperCakeObjective.COLORMAP.size();
                Style style = Style.EMPTY.withColor(SuperCakeObjective.COLORMAP.get(SuperCakeObjective.qol$colorIndex));
                displayMessageOnScreen(new TextComponent("Changed Cake Overlay Color").withStyle(style));
                QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_COLOR.set(SuperCakeObjective.qol$colorIndex);
            }

            if (KeyBindings.TOGGLE_CAKE_OVERLAY_STYLE.consumeClick()) {
                SuperCakeObjective.qol$overlayStyle = (SuperCakeObjective.qol$overlayStyle + 1) % 2;
                Style style = Style.EMPTY.withColor(SuperCakeObjective.COLORMAP.get(SuperCakeObjective.qol$colorIndex));
                String styleText = SuperCakeObjective.qol$overlayStyle == 0 ? "Vignette" : "Radar";
                displayMessageOnScreen(new TextComponent("Changed Cake Overlay Style: " + styleText).withStyle(style));
                QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_STYLE.set(SuperCakeObjective.qol$overlayStyle);
            }

            if (KeyBindings.TOGGLE_MAGNET_GUI.consumeClick()) {
                ModNetwork.CHANNEL.sendToServer(ServerboundMagnetToggleMessage.INSTANCE);
            }

        }

        private static Component vaultTitle;
        private static Component vaultSubtitle;

        @SubscribeEvent
        public static void onVaultJoin(VaultJoinEvent event) {
            QOLHunters.LOGGER.info("Vault Join Event");
            ResourceLocation theme = event.getVault().get(Vault.WORLD).get(WorldManager.THEME);
            ThemeKey themeKey = VaultRegistry.THEME.getKey(theme);
            vaultSubtitle = new TextComponent(themeKey.getName()).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(themeKey.getColor()))).withStyle(ChatFormatting.ITALIC);
            String obj = getVaultObjective(event.getVault().get(Vault.OBJECTIVES).get(Objectives.KEY));
            vaultTitle = new TextComponent(obj + " Vault").withStyle((Style.EMPTY.withColor(TextColor.fromRgb(14536734))));

        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (vaultTitle !=null  && Minecraft.getInstance().player != null && Minecraft.getInstance().screen == null) {
                QOLHunters.LOGGER.info("Displaying Vault Title");
                displayTitleOnScreen(vaultTitle);
                displaySubtitleOnScreen(vaultSubtitle);
                vaultTitle = null; // Clear the stored textComponent
                vaultSubtitle = null;
            }
        }

        public static String getVaultObjective(String key) {
            String var2 = key == null ? "" : key.toLowerCase();

            return switch (var2) {
                case "boss" -> "Hunt the Guardians";
                case "monolith" -> "Brazier";
                case "empty", "" -> "";
                default -> key.substring(0, 1).toUpperCase() + key.substring(1);
            };
        }


    }

    @OnlyIn(Dist.CLIENT)
    private static void displayTitleOnScreen(Component message) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> mc.gui.setTitle(message));
    }

    @OnlyIn(Dist.CLIENT)
    private static void displaySubtitleOnScreen(Component message) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> mc.gui.setSubtitle(message));
    }

    @OnlyIn(Dist.CLIENT)
    private static void displayMessageOnScreen(Component message) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> {
            mc.gui.setOverlayMessage(message, false);
        });
    }


}




