package io.iridium.qolhunters.mixin.elixirrenderer;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.entity.entity.ElixirOrbEntity;
import iskallia.vault.entity.renderer.ElixirOrbRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ElixirOrbRenderer.class, remap = false)
public abstract class MixinElixirOrbRenderer extends EntityRenderer<ElixirOrbEntity> {


    protected MixinElixirOrbRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Inject(method="render(Liskallia/vault/entity/entity/ElixirOrbEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at=@At("HEAD"))
    private void render(CallbackInfo ci) {

        if(QOLHuntersClientConfigs.ELIXIR_SHADOWLESS_ORBS.get()) {
            this.shadowRadius = 0.0F;
            this.shadowStrength = 0.0F;
            super.shadowRadius = 0.0F;
            super.shadowStrength = 0.0F;
        }else{
            this.shadowRadius = 0.15F;
            this.shadowStrength = 0.75F;
            super.shadowRadius = 0.15F;
            super.shadowStrength = 0.75F;
        }
    }


}
