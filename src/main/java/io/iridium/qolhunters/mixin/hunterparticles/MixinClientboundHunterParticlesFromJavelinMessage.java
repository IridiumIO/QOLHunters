package io.iridium.qolhunters.mixin.hunterparticles;

import iskallia.vault.network.message.ClientboundHunterParticlesFromJavelinMessage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.iridium.qolhunters.features.hunterparticles.HunterParticles.qOLHunters$canSpawnParticles;

@Mixin(value = ClientboundHunterParticlesFromJavelinMessage.class, remap = false)
public class MixinClientboundHunterParticlesFromJavelinMessage {


    @Inject(method="createParticles", at=@At("HEAD"), cancellable = true)
    @OnlyIn(Dist.CLIENT)
    private static void createParticles(double x, double y, double z, double sightDuration, String type, CallbackInfo ci) {
        if (type !=null && !qOLHunters$canSpawnParticles(type)) ci.cancel();
    }

}
