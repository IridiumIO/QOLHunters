package io.iridium.qolhunters.mixin.compassspinout;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.core.event.common.CompassPropertyEvent;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "iskallia/vault/init/ModModels$ItemProperty$CompassPropertyFunction", remap = false)
public class MixinModModels {

    @WrapOperation(method = "getCompassTarget", at = @At(value = "INVOKE", target = "Liskallia/vault/core/event/common/CompassPropertyEvent$Data;getTarget()Lnet/minecraft/core/BlockPos;"))
    private BlockPos dontShowPortalWhenTargetIsNotFound(CompassPropertyEvent.Data instance, Operation<BlockPos> original){
        if (QOLHuntersClientConfigs.COMPASS_SPIN_OUT.get()) {
            return null;
        }
        return original.call(instance);
    }
}
