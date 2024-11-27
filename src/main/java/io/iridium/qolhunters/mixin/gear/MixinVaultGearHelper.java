package io.iridium.qolhunters.mixin.gear;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.util.Cacheable;
import iskallia.vault.config.gear.VaultGearTypeConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VaultGearTypeConfig.RollType.class)
public class MixinVaultGearHelper {


    @Shadow(remap = false) private String name;


    @Unique
    private static Cacheable<Integer> SCRAPPY = new Cacheable<>(2500, QOLHuntersClientConfigs.GEAR_ROLL_COLOR_SCRAPPY);
    private static Cacheable<Integer> COMMON = new Cacheable<>(2500, QOLHuntersClientConfigs.GEAR_ROLL_COLOR_COMMON);
    private static Cacheable<Integer> RARE = new Cacheable<>(2500, QOLHuntersClientConfigs.GEAR_ROLL_COLOR_RARE);
    private static Cacheable<Integer> EPIC = new Cacheable<>(2500, QOLHuntersClientConfigs.GEAR_ROLL_COLOR_EPIC);
    private static Cacheable<Integer> OMEGA = new Cacheable<>(2500, QOLHuntersClientConfigs.GEAR_ROLL_COLOR_OMEGA);

    @Inject(method="getColor", at=@At("HEAD"), cancellable = true, remap = false)
    private void getGearColor(CallbackInfoReturnable<Integer> cir) {

        switch (this.name) {
            case "Scrappy+" -> cir.setReturnValue(SCRAPPY.get());
            case "Common+" -> cir.setReturnValue(COMMON.get());
            case "Rare+" -> cir.setReturnValue(RARE.get());
            case "Epic+" -> cir.setReturnValue(EPIC.get());
            case "Omega" -> cir.setReturnValue(OMEGA.get());
        }


    }

    @Unique
    private static Integer hexToInteger(String hex) {
        return Integer.parseInt(hex, 16);
    }

}
