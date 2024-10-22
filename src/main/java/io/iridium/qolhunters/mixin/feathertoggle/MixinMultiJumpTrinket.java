package io.iridium.qolhunters.mixin.feathertoggle;

import iskallia.vault.gear.trinket.effects.MultiJumpTrinket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.iridium.qolhunters.features.feather.ToggleFeather.featherEnabled;

@Mixin(value = MultiJumpTrinket.class, remap = false)
public class MixinMultiJumpTrinket {

    @Inject(method="onClientTick", at=@At("HEAD"), cancellable = true)
    public void onClientTick(CallbackInfo ci) {
        if(!featherEnabled) ci.cancel();
    }

}
