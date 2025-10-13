package io.iridium.qolhunters.features.hunterparticles;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.skill.ability.effect.spi.HunterAbility;

public class HunterParticles {

    public static boolean qOLHunters$canSpawnParticles(HunterAbility.Target hunterSpec) {
        if (hunterSpec == null) {
            return QOLHuntersClientConfigs.HUNTER_PARTICLES_OTHER.get();
        }
        return switch (hunterSpec) {
            case WOODEN ->  QOLHuntersClientConfigs.HUNTER_PARTICLES_WOODEN.get();
            case GILDED -> QOLHuntersClientConfigs.HUNTER_PARTICLES_GILDED.get();
            case ORNATE -> QOLHuntersClientConfigs.HUNTER_PARTICLES_ORNATE.get();
            case LIVING -> QOLHuntersClientConfigs.HUNTER_PARTICLES_LIVING.get();
            case COINS -> QOLHuntersClientConfigs.HUNTER_PARTICLES_COINS.get();
            case HARDENED -> QOLHuntersClientConfigs.HUNTER_PARTICLES_HARDENED.get();
            case ENIGMA -> QOLHuntersClientConfigs.HUNTER_PARTICLES_ENIGMA.get();
            case FLESH -> QOLHuntersClientConfigs.HUNTER_PARTICLES_FLESH.get();
            case OBJECTIVES -> QOLHuntersClientConfigs.HUNTER_PARTICLES_OBJECTIVES.get();
            case GOD_ALTARS -> QOLHuntersClientConfigs.HUNTER_PARTICLES_GOD_ALTARS.get();
            case DUNGEONS -> QOLHuntersClientConfigs.HUNTER_PARTICLES_DUNGEONS.get();
            case VENDOORS -> QOLHuntersClientConfigs.HUNTER_PARTICLES_VENDOORS.get();
            case TREASURE_DOORS -> QOLHuntersClientConfigs.HUNTER_PARTICLES_TREASURE_DOORS.get();
            case PYLONS -> QOLHuntersClientConfigs.HUNTER_PARTICLES_PYLONS.get();
            default -> QOLHuntersClientConfigs.HUNTER_PARTICLES_OTHER.get();
        };
    }

}
