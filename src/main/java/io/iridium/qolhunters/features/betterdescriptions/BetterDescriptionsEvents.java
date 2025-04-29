package io.iridium.qolhunters.features.betterdescriptions;

import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.config.AbilitiesDescriptionsConfig;
import iskallia.vault.config.BingoConfig;
import iskallia.vault.config.MenuPlayerStatDescriptionConfig;
import iskallia.vault.config.SkillDescriptionsConfig;
import iskallia.vault.init.ModConfigs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= QOLHunters.MOD_ID, value = Dist.CLIENT)
public class BetterDescriptionsEvents {


    private static Boolean isBetterAbilitiesDescriptionEnabled = null;
    private static Boolean isBetterStatsDescriptionEnabled = null;
    private static Boolean isBetterSkillDescriptionEnabled = null;
    private static Boolean isBetterBingoEnabled = null;
    private static long LastCheckedTime = 0;

    @SubscribeEvent
    public static void CheckIfMixinConfigsAreChanged(TickEvent.ClientTickEvent event) {
        if (isBetterStatsDescriptionEnabled == null) {
            //isBetterAbilitiesDescriptionEnabled = QOLHuntersClientConfigs.BETTER_ABILITIES_DESCRIPTIONS.get();
            isBetterStatsDescriptionEnabled = QOLHuntersClientConfigs.BETTER_STATS_DESCRIPTIONS.get();
            //isBetterSkillDescriptionEnabled = QOLHuntersClientConfigs.BETTER_TALENTS_EXPERTISE_RESEARCH_DESCRIPTIONS.get();
            isBetterBingoEnabled = QOLHuntersClientConfigs.BETTER_BINGO_DESCRIPTIONS.get();
        }

        if (System.currentTimeMillis() < LastCheckedTime + 5000 ||
                (//isBetterAbilitiesDescriptionEnabled == QOLHuntersClientConfigs.BETTER_ABILITIES_DESCRIPTIONS.get() &&
                        isBetterStatsDescriptionEnabled == QOLHuntersClientConfigs.BETTER_STATS_DESCRIPTIONS.get() &&
                        //isBetterSkillDescriptionEnabled == QOLHuntersClientConfigs.BETTER_TALENTS_EXPERTISE_RESEARCH_DESCRIPTIONS.get() &&
                        isBetterBingoEnabled == QOLHuntersClientConfigs.BETTER_BINGO_DESCRIPTIONS.get()
                ))
            return;

        //isBetterAbilitiesDescriptionEnabled = QOLHuntersClientConfigs.BETTER_ABILITIES_DESCRIPTIONS.get();
        isBetterStatsDescriptionEnabled = QOLHuntersClientConfigs.BETTER_STATS_DESCRIPTIONS.get();
        //isBetterSkillDescriptionEnabled = QOLHuntersClientConfigs.BETTER_TALENTS_EXPERTISE_RESEARCH_DESCRIPTIONS.get();
        isBetterBingoEnabled = QOLHuntersClientConfigs.BETTER_BINGO_DESCRIPTIONS.get();

        ModConfigs.MENU_PLAYER_STAT_DESCRIPTIONS = (new MenuPlayerStatDescriptionConfig()).readConfig();
        ModConfigs.ABILITIES_DESCRIPTIONS = (new AbilitiesDescriptionsConfig()).readConfig();
        ModConfigs.SKILL_DESCRIPTIONS = (new SkillDescriptionsConfig()).readConfig();
        ModConfigs.BINGO = (new BingoConfig()).readConfig();

        LastCheckedTime = System.currentTimeMillis();

    }

}
