package io.iridium.qolhunters.mixin.temporalmodifiertimer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.iridium.qolhunters.features.temporalmodifiertimer.VaultModifierWithTime;
import iskallia.vault.core.vault.Modifiers;
import iskallia.vault.core.vault.modifier.spi.VaultModifier;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = Modifiers.class,remap = false)
public class MixinModifiers {
    @WrapOperation(method = "getDisplayGroup", at = @At(value = "INVOKE", target = "Liskallia/vault/core/vault/Modifiers$Entry;getModifier()Ljava/util/Optional;"))
    private static Optional<VaultModifier<?>> addContext(Modifiers.Entry instance, Operation<Optional<VaultModifier<?>>> original, @Local(name="map") Object2IntMap<VaultModifier<?>> map){
        var ret = original.call(instance);
        var retVal = ret.orElse(null);
        if (retVal instanceof VaultModifierWithTime rvWithTime) {
            if (!map.containsKey(retVal)) { // new iteration => delete to not persist across vaults
                rvWithTime.qOLHunters$setTime(null);
            }
            var ctx = instance.getContext();
            if (ctx != null) {
                var currTime = rvWithTime.qOLHunters$getTime();
                var newTime = ctx.getTimeLeft().orElse(null);
                if (newTime != null && (currTime == null || currTime <= 0 || newTime < currTime)) {
                    // store shortest time
                    rvWithTime.qOLHunters$setTime(newTime);
                }
            }
        }
        return ret;
    }
}
