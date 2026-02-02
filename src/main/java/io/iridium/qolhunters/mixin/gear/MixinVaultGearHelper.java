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


    @Unique private static Cacheable<Integer> qolhunters$SCRAPPY = new Cacheable<>(2500, QOLHuntersClientConfigs.GEAR_ROLL_COLOR_SCRAPPY);
    @Unique private static Cacheable<Integer> qolhunters$COMMON = new Cacheable<>(2500, QOLHuntersClientConfigs.GEAR_ROLL_COLOR_COMMON);
    @Unique private static Cacheable<Integer> qolhunters$RARE = new Cacheable<>(2500, QOLHuntersClientConfigs.GEAR_ROLL_COLOR_RARE);
    @Unique private static Cacheable<Integer> qolhunters$EPIC = new Cacheable<>(2500, QOLHuntersClientConfigs.GEAR_ROLL_COLOR_EPIC);
    @Unique private static Cacheable<Integer> qolhunters$OMEGA = new Cacheable<>(2500, QOLHuntersClientConfigs.GEAR_ROLL_COLOR_OMEGA);

    @Inject(method="getColor", at=@At("HEAD"), cancellable = true, remap = false)
    private void getGearColor(CallbackInfoReturnable<Integer> cir) {

        switch (this.name) {
            case "Scrappy+" -> cir.setReturnValue(qolhunters$SCRAPPY.get());
            case "Common+" -> cir.setReturnValue(qolhunters$COMMON.get());
            case "Rare+" -> cir.setReturnValue(qolhunters$RARE.get());
            case "Epic+" -> cir.setReturnValue(qolhunters$EPIC.get());
            case "Omega" -> cir.setReturnValue(qolhunters$OMEGA.get());
        }
    }

}
