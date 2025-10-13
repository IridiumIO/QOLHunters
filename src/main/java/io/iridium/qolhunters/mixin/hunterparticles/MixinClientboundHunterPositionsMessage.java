package io.iridium.qolhunters.mixin.hunterparticles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import iskallia.vault.client.render.hunter.HunterParticleDensityOption;
import iskallia.vault.network.message.ClientboundHunterPositionsMessage;
import iskallia.vault.skill.ability.effect.spi.HunterAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static io.iridium.qolhunters.features.hunterparticles.HunterParticles.qOLHunters$canSpawnParticles;

@Mixin(value = ClientboundHunterPositionsMessage.class, remap = false)
public class MixinClientboundHunterPositionsMessage {


    @WrapOperation(method = "handle", at = @At(value = "INVOKE", target = "Liskallia/vault/client/render/hunter/HunterParticleDensityOption;getDensity()D"))
    private static double spawnZeroHunterHighlights(HunterParticleDensityOption instance, Operation<Double> original,
                                                    @Local(name = "position") HunterAbility.HighlightPosition position) {
        if (!qOLHunters$canSpawnParticles(position.target())) {
            return 0;
        }
        return original.call(instance);
    }

}
