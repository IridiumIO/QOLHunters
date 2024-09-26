package io.iridium.qolhunters;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.config.ui.ConfigHelper;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.config.SkillAltarConfig;
import io.iridium.qolhunters.features.vault_scavenger.Scavenger;
import io.iridium.qolhunters.features.vaultenchanteremeraldslot.VaultEnchanterEmeraldSlot;
import io.iridium.qolhunters.networking.ModMessages;
import io.iridium.qolhunters.networking.packet.HandshakeCheckModIsOnClientS2CPacket;
import io.iridium.qolhunters.networking.packet.HandshakeCheckModIsOnServerC2SPacket;
import io.iridium.qolhunters.networking.packet.HandshakeRespondModIsOnClientC2SPacket;
import io.iridium.qolhunters.networking.packet.HandshakeRespondModIsOnServerS2CPacket;
import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.block.VaultEnchanterBlock;
import iskallia.vault.config.AbilitiesDescriptionsConfig;
import iskallia.vault.config.BingoConfig;
import iskallia.vault.config.MenuPlayerStatDescriptionConfig;
import iskallia.vault.config.SkillDescriptionsConfig;
import iskallia.vault.core.data.key.ThemeKey;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.VaultRegistry;
import iskallia.vault.core.vault.WorldManager;
import iskallia.vault.core.vault.objective.Objectives;
import iskallia.vault.event.event.VaultJoinEvent;
import iskallia.vault.event.event.VaultLeaveEvent;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.item.CardDeckItem;
import iskallia.vault.item.MagnetItem;
import iskallia.vault.network.message.ServerboundMagnetToggleMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
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
        event.enqueueWork(ModMessages::register);
    }
//
    @Mod.EventBusSubscriber(modid = MOD_ID)
    public static class ServerForgeEvents {

        @SubscribeEvent
        public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
            BlockState blockState = event.getWorld().getBlockState(event.getPos());
            if (blockState.getBlock() instanceof VaultEnchanterBlock) {
                if(!(event.getPlayer() instanceof ServerPlayer)) {
                    QOLHunters.LOGGER.info("PING sent from Client!");
                    ModMessages.sendToServer(new HandshakeRespondModIsOnClientC2SPacket(QOLHuntersClientConfigs.VAULT_ENCHANTER_EMERALDS_SLOT.get()));
                }
                QOLHunters.LOGGER.info("Vault Enchanter Block Right Clicked");
            }
        }

        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (!(event.getPlayer() instanceof ServerPlayer)) return;
            QOLHunters.LOGGER.info("Player Joined: " + ((ServerPlayer) event.getPlayer()).getUUID());
            VaultEnchanterEmeraldSlot.playerHasMod.put((ServerPlayer) event.getPlayer(), false);
            ModMessages.sendToClient(new HandshakeCheckModIsOnClientS2CPacket(), (ServerPlayer) event.getPlayer());
        }

    }


    @Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        public static ModMode MOD_MODE = ModMode.CLIENTONLY;

        @SubscribeEvent
        public static void onKeyInput(InputEvent.KeyInputEvent event) {

            if (KeyBindings.TOGGLE_CAKE_OVERLAY_COLOR.consumeClick()) {
                QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_COLOR.set(
                        QOLHuntersClientConfigs.CakeVaultOverlayColor.values()[
                                (QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_COLOR.get().ordinal() + 1) % QOLHuntersClientConfigs.CakeVaultOverlayColor.values().length
                                ]
                );
                Style style = Style.EMPTY.withColor(QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_COLOR.get().getColorCode());
                displayMessageOnScreen(new TextComponent("Changed Cake Overlay Color").withStyle(style));
                QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_COLOR.set(QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_COLOR.get());
            }

            if (KeyBindings.TOGGLE_CAKE_OVERLAY_STYLE.consumeClick()) {
                QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_STYLE.set(
                        QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_STYLE.get() == QOLHuntersClientConfigs.CakeVaultOverlayStyle.VIGNETTE
                                ? QOLHuntersClientConfigs.CakeVaultOverlayStyle.RADAR
                                : QOLHuntersClientConfigs.CakeVaultOverlayStyle.VIGNETTE
                );
                Style style = Style.EMPTY.withColor(QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_COLOR.get().getColorCode());
                String styleText = QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_STYLE.get() == QOLHuntersClientConfigs.CakeVaultOverlayStyle.VIGNETTE ? "Vignette" : "Radar";
                displayMessageOnScreen(new TextComponent("Changed Cake Overlay Style: " + styleText).withStyle(style));
                QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_STYLE.set(QOLHuntersClientConfigs.CAKE_VAULT_OVERLAY_STYLE.get());
            }

            if (event.getKey() == KeyBindings.TOGGLE_MAGNET_GUI.getKey().getValue() && event.getAction() == GLFW.GLFW_PRESS && Minecraft.getInstance().screen != null ) {
                ModNetwork.CHANNEL.sendToServer(ServerboundMagnetToggleMessage.INSTANCE);
            }

            if (KeyBindings.OPEN_CONFIG.consumeClick()) {
                SubMenuConfigScreen screen = SubMenuConfigScreen.find(ConfigHelper.ConfigPath.parse("qolhunters:client.Client-Only Extensions"));
                Minecraft.getInstance().setScreen(screen);
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
            if (!(QOLHuntersClientConfigs.SHOW_GEAR_COOLDOWN_TIME.get()) || (event.getScreen() instanceof CreativeModeInventoryScreen) || !(event.getScreen() instanceof AbstractContainerScreen<?> screen)) return;

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

        private static Boolean isVaultEnchanterEmeraldSlotEnabledClient = null;

        @SubscribeEvent
        public static void CheckIfVaultEnchanterEmeraldSlotChanged(TickEvent.PlayerTickEvent event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                LastCheckedTime = System.currentTimeMillis();
                return; // Ensure Minecraft instance and player are available
            }

            mc.execute(() -> {
                if (System.currentTimeMillis() < LastCheckedTime + 2000 ||
                        (isVaultEnchanterEmeraldSlotEnabledClient == QOLHuntersClientConfigs.VAULT_ENCHANTER_EMERALDS_SLOT.get())) {
                    return;
                }
                QOLHunters.LOGGER.info("Updating Client config!");
                isVaultEnchanterEmeraldSlotEnabledClient = QOLHuntersClientConfigs.VAULT_ENCHANTER_EMERALDS_SLOT.get();
                ModMessages.sendToServer(new HandshakeRespondModIsOnClientC2SPacket(isVaultEnchanterEmeraldSlotEnabledClient));
                LastCheckedTime = System.currentTimeMillis();
            });
        }


        private static Boolean isBetterAbilitiesDescriptionEnabled = null;
        private static Boolean isBetterStatsDescriptionEnabled = null;
        private static Boolean isBetterSkillDescriptionEnabled = null;
        private static Boolean isBetterBingoEnabled = null;
        private static long LastCheckedTime = 0;

        @SubscribeEvent
        public static void CheckIfMixinConfigsAreChanged(TickEvent.ClientTickEvent event) {
            if(isBetterAbilitiesDescriptionEnabled == null) {
                isBetterAbilitiesDescriptionEnabled = QOLHuntersClientConfigs.BETTER_ABILITIES_DESCRIPTIONS.get();
                isBetterStatsDescriptionEnabled = QOLHuntersClientConfigs.BETTER_STATS_DESCRIPTIONS.get();
                isBetterSkillDescriptionEnabled = QOLHuntersClientConfigs.BETTER_TALENTS_EXPERTISE_RESEARCH_DESCRIPTIONS.get();
                isBetterBingoEnabled = QOLHuntersClientConfigs.BETTER_BINGO_DESCRIPTIONS.get();
            }

            if (System.currentTimeMillis() < LastCheckedTime + 5000 ||
                    (isBetterAbilitiesDescriptionEnabled == QOLHuntersClientConfigs.BETTER_ABILITIES_DESCRIPTIONS.get() &&
                    isBetterStatsDescriptionEnabled == QOLHuntersClientConfigs.BETTER_STATS_DESCRIPTIONS.get() &&
                    isBetterSkillDescriptionEnabled == QOLHuntersClientConfigs.BETTER_TALENTS_EXPERTISE_RESEARCH_DESCRIPTIONS.get() &&
                    isBetterBingoEnabled == QOLHuntersClientConfigs.BETTER_BINGO_DESCRIPTIONS.get()
                    ))
                return;

            QOLHunters.LOGGER.info(MOD_MODE.toString());
            isBetterAbilitiesDescriptionEnabled = QOLHuntersClientConfigs.BETTER_ABILITIES_DESCRIPTIONS.get();
            isBetterStatsDescriptionEnabled = QOLHuntersClientConfigs.BETTER_STATS_DESCRIPTIONS.get();
            isBetterSkillDescriptionEnabled = QOLHuntersClientConfigs.BETTER_TALENTS_EXPERTISE_RESEARCH_DESCRIPTIONS.get();
            isBetterBingoEnabled = QOLHuntersClientConfigs.BETTER_BINGO_DESCRIPTIONS.get();

            ModConfigs.MENU_PLAYER_STAT_DESCRIPTIONS = (new MenuPlayerStatDescriptionConfig()).readConfig();
            ModConfigs.ABILITIES_DESCRIPTIONS = (new AbilitiesDescriptionsConfig()).readConfig();
            ModConfigs.SKILL_DESCRIPTIONS = (new SkillDescriptionsConfig()).readConfig();
            ModConfigs.BINGO = (new BingoConfig()).readConfig();

            LastCheckedTime = System.currentTimeMillis();

        }

        @SubscribeEvent
        public static void onJoinServer(ClientPlayerNetworkEvent.LoggedInEvent event) {

            MOD_MODE = ModMode.CLIENTONLY;
            ModMessages.sendToServer(new HandshakeCheckModIsOnServerC2SPacket());


        }

    }


    public enum ModMode {
        CLIENTONLY,
        CLIENTANDSERVER,
        SERVERONLY
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




