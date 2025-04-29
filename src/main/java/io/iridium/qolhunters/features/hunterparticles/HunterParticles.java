package io.iridium.qolhunters.features.hunterparticles;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;

public class HunterParticles {

    public static boolean qOLHunters$canSpawnParticles(String hunterSpec) {
        return switch (hunterSpec) {
            case "blocks" -> QOLHuntersClientConfigs.HUNTER_PARTICLES_BLOCKS.get();
            case "gilded" -> QOLHuntersClientConfigs.HUNTER_PARTICLES_GILDED.get();
            case "living" -> QOLHuntersClientConfigs.HUNTER_PARTICLES_LIVING.get();
            case "ornate" -> QOLHuntersClientConfigs.HUNTER_PARTICLES_ORNATE.get();
            case "coins" -> QOLHuntersClientConfigs.HUNTER_PARTICLES_COINS.get();
            case "wooden" -> QOLHuntersClientConfigs.HUNTER_PARTICLES_WOODEN.get();
            default -> QOLHuntersClientConfigs.HUNTER_PARTICLES_OTHER.get();
        };
    }

}
