package io.iridium.qolhunters.mixin.betterdescriptions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(iskallia.vault.config.SkillDescriptionsConfig.class)
public class MixinSkillDescriptionsConfig {

    @Inject(at = @At("HEAD"), method = "getName()Ljava/lang/String;", cancellable = true, remap = false)
    private void getName(CallbackInfoReturnable<String> cir) {

        //if(QOLHuntersClientConfigs.BETTER_TALENTS_EXPERTISE_RESEARCH_DESCRIPTIONS.get()) cir.setReturnValue("iridium/skill_descriptions");
    }

}
