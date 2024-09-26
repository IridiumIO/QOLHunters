package io.iridium.qolhunters.features.vault_cake;

import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.util.KeyBindings;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.iridium.qolhunters.util.SharedFunctions.displayMessageOnScreen;

@Mod.EventBusSubscriber(modid= QOLHunters.MOD_ID, value = Dist.CLIENT)
public class CakeVaultEvents {

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

    }

}
