package io.iridium.qolhunters;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.logging.LogUtils;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.config.SkillAltarConfig;
import io.iridium.qolhunters.customimplementations.Scavenger;
import io.iridium.qolhunters.interfaces.SuperCakeObjective;
import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.core.data.key.ThemeKey;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.VaultRegistry;
import iskallia.vault.core.vault.WorldManager;
import iskallia.vault.core.vault.objective.Objectives;
import iskallia.vault.event.event.VaultJoinEvent;
import iskallia.vault.event.event.VaultLeaveEvent;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.item.CardDeckItem;
import iskallia.vault.item.MagnetItem;
import iskallia.vault.network.message.ServerboundMagnetToggleMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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

    public static SkillAltarConfig SKILL_ALTAR_CONFIG;

    public QOLHunters() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, QOLHuntersClientConfigs.CLIENT_SPEC, "qolhunters-client.toml");

    }

    private void clientSetup(final FMLClientSetupEvent event) {
        SKILL_ALTAR_CONFIG = SkillAltarConfig.load();
        KeyBindings.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }


    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.KeyInputEvent event) {

            if (KeyBindings.TOGGLE_CAKE_OVERLAY_COLOR.consumeClick()) {
                SuperCakeObjective.qol$colorIndex = QOLHuntersClientConfigs.CakeVaultOverlayColor.values()[(SuperCakeObjective.qol$colorIndex.ordinal() + 1) % QOLHuntersClientConfigs.CakeVaultOverlayColor.values().length];

                Style style = Style.EMPTY.withColor(SuperCakeObjective.qol$colorIndex.getColorCode());
                displayMessageOnScreen(new TextComponent("Changed Cake Overlay Color").withStyle(style));
                QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_COLOR.set(SuperCakeObjective.qol$colorIndex);
            }

            if (KeyBindings.TOGGLE_CAKE_OVERLAY_STYLE.consumeClick()) {
                SuperCakeObjective.qol$overlayStyle = SuperCakeObjective.qol$overlayStyle == QOLHuntersClientConfigs.CakeVaultOverlayStyle.VIGNETTE ? QOLHuntersClientConfigs.CakeVaultOverlayStyle.RADAR : QOLHuntersClientConfigs.CakeVaultOverlayStyle.VIGNETTE;
                Style style = Style.EMPTY.withColor(SuperCakeObjective.qol$colorIndex.getColorCode());
                String styleText = SuperCakeObjective.qol$overlayStyle == QOLHuntersClientConfigs.CakeVaultOverlayStyle.VIGNETTE ? "Vignette" : "Radar";
                displayMessageOnScreen(new TextComponent("Changed Cake Overlay Style: " + styleText).withStyle(style));
                QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_STYLE.set(SuperCakeObjective.qol$overlayStyle);
            }

            if (event.getKey() == KeyBindings.TOGGLE_MAGNET_GUI.getKey().getValue() && event.getAction() == GLFW.GLFW_PRESS && Minecraft.getInstance().screen != null ) {
                ModNetwork.CHANNEL.sendToServer(ServerboundMagnetToggleMessage.INSTANCE);
            }

            if (event.getKey() == GLFW.GLFW_KEY_Q && event.getAction() == GLFW.GLFW_PRESS &&
                    (event.getModifiers() & GLFW.GLFW_MOD_SHIFT) != 0 &&
                    (event.getModifiers() & GLFW.GLFW_MOD_CONTROL) != 0 &&
                    (event.getModifiers() & GLFW.GLFW_MOD_ALT) != 0) {
                Scavenger.ScavengerItems.clear();
                QOLHunters.LOGGER.info("Scavenger items cleared");
            }

            if (event.getKey() == GLFW.GLFW_KEY_O && event.getAction() == GLFW.GLFW_PRESS &&
                    (event.getModifiers() & GLFW.GLFW_MOD_SHIFT) != 0 &&
                    (event.getModifiers() & GLFW.GLFW_MOD_CONTROL) != 0 &&
                    (event.getModifiers() & GLFW.GLFW_MOD_ALT) != 0) {
                displayMessageOnScreen(new TextComponent("OBS MODE ENABLED").withStyle(ChatFormatting.RED));
            }


        }

        @SubscribeEvent
        public static void onScreenRender(ScreenEvent.DrawScreenEvent.Post event) {

            Player player = Minecraft.getInstance().player;
            if (!(QOLHuntersClientConfigs.SHOW_GEAR_COOLDOWN_TIME.get()) || !(event.getScreen() instanceof AbstractContainerScreen<?> screen)) return;

            ItemCooldowns cooldowns = player.getCooldowns();

            for (Slot slot : screen.getMenu().slots){
                ItemStack itemStack = slot.getItem();
                Item item = itemStack.getItem();
                if (cooldowns.isOnCooldown(item) && (item instanceof VaultGearItem || item instanceof CardDeckItem || item instanceof MagnetItem)) {
                    float cooldownPercent = cooldowns.getCooldownPercent(item, 0F);
                    QOLHunters.LOGGER.info("Cooldown Percent: " + cooldownPercent);
                    int absoluteCooldownSeconds = Math.round(cooldownPercent * 20.0F);
                    PoseStack poseStack = event.getPoseStack();
                    int x = slot.x + screen.getGuiLeft();
                    int y = slot.y + screen.getGuiTop();

                    RenderSystem.disableDepthTest();
                    poseStack.pushPose();
                    poseStack.translate(x, y, 400);

                    poseStack.scale(0.6F, 0.6F, 1.0F);
                    Tesselator tesselator = Tesselator.getInstance();
                    BufferBuilder bufferBuilder = tesselator.getBuilder();
                    MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(bufferBuilder);

                    Font font = Minecraft.getInstance().font;
                    font.drawInBatch(new TextComponent(absoluteCooldownSeconds + "s").withStyle(ChatFormatting.GREEN), 0, (int)(16/0.6)-font.lineHeight, 0xFFFFFFFF, true, poseStack.last().pose(), bufferSource, false, 0, 15728880);
                    bufferSource.endBatch();

                    poseStack.popPose();
                }
            }
        }


        private static Component vaultTitle;
        private static Component vaultSubtitle;

        @SubscribeEvent
        public static void onVaultJoin(VaultJoinEvent event) {

            ResourceLocation theme = event.getVault().get(Vault.WORLD).get(WorldManager.THEME);
            ThemeKey themeKey = VaultRegistry.THEME.getKey(theme);
            vaultSubtitle = new TextComponent(themeKey.getName()).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(themeKey.getColor()))).withStyle(ChatFormatting.ITALIC);
            String obj = getVaultObjective(event.getVault().get(Vault.OBJECTIVES).get(Objectives.KEY));
            vaultTitle = new TextComponent(obj + " Vault").withStyle((Style.EMPTY.withColor(TextColor.fromRgb(14536734))));


            // Register ClientTickEvent listener
            MinecraftForge.EVENT_BUS.addListener(new java.util.function.Consumer<TickEvent.ClientTickEvent>() {
                @Override
                public void accept(TickEvent.ClientTickEvent tickEvent) {
                    if (vaultTitle != null && Minecraft.getInstance().player != null && Minecraft.getInstance().screen == null) {
                        displayTitleOnScreen(vaultTitle);
                        displaySubtitleOnScreen(vaultSubtitle);
                        vaultTitle = null;
                        vaultSubtitle = null;
                        // Unregister the listener after displaying the message
                        MinecraftForge.EVENT_BUS.unregister(this);
                    }
                }
            });
        }

        @SubscribeEvent
        public static void onVaultLeave(VaultLeaveEvent event){
            Scavenger.ScavengerItems.clear();
        }

        @SubscribeEvent
        public static void onVaultComplete(PlayerEvent.PlayerChangedDimensionEvent event){
            Scavenger.ScavengerItems.clear();
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




