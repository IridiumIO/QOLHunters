package io.iridium.qolhunters.mixin.bingo;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.task.renderer.BingoRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BingoRenderer.Leaf.class, remap = false)
public class MixinBingoRendererLeaf {

    @Shadow public String name;

   @Redirect(method="onRender(Liskallia/vault/task/Task;Liskallia/vault/task/renderer/context/TaskRendererContext;)V",
   at = @At(value = "FIELD", target = "Liskallia/vault/task/renderer/BingoRenderer$Leaf;name:Ljava/lang/String;", opcode = Opcodes.GETFIELD))
    private String onRenderRedirectName(iskallia.vault.task.renderer.BingoRenderer.Leaf leaf) {

        if(this.name.contains("Silently") && QOLHuntersClientConfigs.BETTER_BINGO_DESCRIPTIONS.get()) {
            this.name = this.name.replace("Silently", "Peacefully");
        }

       return this.name;
    }


}
