package io.iridium.qolhunters.features.betterdescriptions;

import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.config.MenuPlayerStatDescriptionConfig;
import iskallia.vault.init.ModConfigs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= QOLHunters.MOD_ID, value = Dist.CLIENT)
public class BetterDescriptionsEvents {


    private static Boolean isBetterStatsDescriptionEnabled = null;
    private static long LastCheckedTime = 0;

    @SubscribeEvent
    public static void CheckIfMixinConfigsAreChanged(TickEvent.ClientTickEvent event) {
        if (isBetterStatsDescriptionEnabled == null) {
            isBetterStatsDescriptionEnabled = QOLHuntersClientConfigs.BETTER_STATS_DESCRIPTIONS.get();
        }

        if (System.currentTimeMillis() < LastCheckedTime + 5000 ||
                (isBetterStatsDescriptionEnabled == QOLHuntersClientConfigs.BETTER_STATS_DESCRIPTIONS.get()))
            return;

        isBetterStatsDescriptionEnabled = QOLHuntersClientConfigs.BETTER_STATS_DESCRIPTIONS.get();

        ModConfigs.MENU_PLAYER_STAT_DESCRIPTIONS = (new MenuPlayerStatDescriptionConfig()).readConfig();

        LastCheckedTime = System.currentTimeMillis();

    }

}
