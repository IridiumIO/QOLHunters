package io.iridium.qolhunters.mixin;

import io.iridium.qolhunters.ConfigBuilder;
import io.iridium.qolhunters.QOLHunters;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(iskallia.vault.config.VaultModifiersConfig.class)
public class MixinVaultModifiersConfig {

    @Inject(at = @At("HEAD"), method = "getName()Ljava/lang/String;", cancellable = true, remap = false)
    private void getName(CallbackInfoReturnable<String> cir) {
        QOLHunters.LOGGER.info("QOLHUNTERS: MixinVaultModifiersConfig.getName() called");



        ResourceLocation resourceLocation;

        boolean isWoldsVaultModInstalled = ModList.get().isLoaded("woldsvaults");

        if(isWoldsVaultModInstalled){
            QOLHunters.LOGGER.info("QOLHUNTERS: Wold's Vault Mod is installed");
            resourceLocation = new ResourceLocation("qolhunters", "wolds_modifiers.json");
            QOLHunters.LOGGER.info(resourceLocation.toString());
        }else{
            resourceLocation = new ResourceLocation("qolhunters", "vault_modifiers.json");
        }



        ConfigBuilder.buildConfig(resourceLocation);

        cir.setReturnValue("vault_modifiers_iridium");
    }

}
