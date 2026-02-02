package io.iridium.qolhunters.mixin.configs;

import io.iridium.qolhunters.ConfigBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(iskallia.vault.init.ModConfigs.class)
public class MixinModConfigs {

    @Inject(at = @At("HEAD"), method = "register", remap = false)
    private static void qolhunters$register(CallbackInfo ci) {

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ConfigBuilder.buildConfigs();
        }

    }

}
