package io.iridium.qolhunters.mixin.invhud;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.mixin.abilities.MixinAbilitiesOverlay;
import iskallia.vault.client.render.hud.module.AbilitiesCooldownModule;
import iskallia.vault.skill.ability.effect.spi.core.Cooldown;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * {@link MixinAbilitiesOverlay} ported to invhud module
 */
@Mixin(value = AbilitiesCooldownModule.class, remap = false)
public class MixinAbilitiesCooldownModule {

    // show duration for abilities with delayed cooldown (totem, storm arrow, mana barrier, etc.)
    // abilities with duration that don't have delayed cooldown aren't supported yet (requires 2 timers)
    @WrapOperation(method = "renderModule(Liskallia/vault/client/render/hud/module/context/ModuleRenderContext;)V", at = @At(value = "INVOKE", target = "Liskallia/vault/skill/ability/effect/spi/core/Cooldown;getRemainingTicks()I", ordinal = 3))
    private static int modifyTimeOverlay(Cooldown instance, Operation<Integer> original){
        int delayTicks = instance.getRemainingDelayTicks();
        if (delayTicks > 0 && QOLHuntersClientConfigs.ABILITY_DURATION_OVERLAY.get()){
            return delayTicks;
        }
        return original.call(instance);
    }

    @WrapOperation(method = "renderModule(Liskallia/vault/client/render/hud/module/context/ModuleRenderContext;)V", at = @At(value = "INVOKE", target = "Liskallia/vault/skill/ability/effect/spi/core/Cooldown;getRemainingTicks()I", ordinal = 2))
    private static int removeGrayscaleIfUsing(Cooldown instance, Operation<Integer> original){
        int delayTicks = instance.getRemainingDelayTicks();
        if (delayTicks > 0 && QOLHuntersClientConfigs.ABILITY_DURATION_OVERLAY.get()){
            return 0;
        }
        return original.call(instance);
    }

    // 0 is full cooldown, 16 is no cooldown
    @WrapOperation(method = "renderModule(Liskallia/vault/client/render/hud/module/context/ModuleRenderContext;)V", at = @At(value = "INVOKE", target = "Liskallia/vault/client/render/hud/module/AbilitiesCooldownModule;getCooldownHeight(FI)I"))
    private static int removeCdHeightOverlayIfUsing(float cooldownRemaining, int cooldownMax, Operation<Integer> original, @Local(name = "cooldown") Cooldown cooldown){
        int delayTicks = cooldown.getRemainingDelayTicks();
        if (delayTicks > 0 && QOLHuntersClientConfigs.ABILITY_DURATION_OVERLAY.get()){
            return 16;
        }
        return original.call(cooldownRemaining, cooldownMax);
    }
}
