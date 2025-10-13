package io.iridium.qolhunters.mixin.hunterparticles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import iskallia.vault.client.render.HunterOutlineRenderer;
import iskallia.vault.skill.ability.effect.spi.HunterAbility;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Map;

import static io.iridium.qolhunters.features.hunterparticles.HunterParticles.qOLHunters$canSpawnParticles;

@Mixin(value = HunterOutlineRenderer.class, remap = false)
public class MixinHunterOutlineRenderer {
    @Shadow @Final private static Map<BlockPos, ?> POSITIONS;

    @WrapOperation(method = "addHighlightPositions", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private static boolean hehe(List<?> instance, Object pos, Operation<Boolean> original, @Local(name = "vhPosition") HunterAbility.HighlightPosition vhPosition) {
        if (!qOLHunters$canSpawnParticles(vhPosition.target())) {
            // don't add to batch list and remove from hashmap
            POSITIONS.remove(vhPosition.blockPos());
            return true;
        }
        return original.call(instance, pos);
    }
}
