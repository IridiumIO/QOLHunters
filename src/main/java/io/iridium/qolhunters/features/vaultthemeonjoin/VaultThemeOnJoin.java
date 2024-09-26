package io.iridium.qolhunters.features.vaultthemeonjoin;

import io.iridium.qolhunters.QOLHunters;
import iskallia.vault.core.data.key.ThemeKey;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.VaultRegistry;
import iskallia.vault.core.vault.WorldManager;
import iskallia.vault.core.vault.objective.Objectives;
import iskallia.vault.event.event.VaultJoinEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.iridium.qolhunters.util.SharedFunctions.displaySubtitleOnScreen;
import static io.iridium.qolhunters.util.SharedFunctions.displayTitleOnScreen;

@Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
public class VaultThemeOnJoin {


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
