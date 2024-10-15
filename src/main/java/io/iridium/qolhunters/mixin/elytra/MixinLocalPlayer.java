package io.iridium.qolhunters.mixin.elytra;

import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static io.iridium.qolhunters.features.elytra.ToggleElytra.elytraEnabled;

@Mixin(value = LocalPlayer.class)
public class MixinLocalPlayer {

    @ModifyVariable(method="aiStep", at=@At(value="FIELD", target="Lnet/minecraft/client/player/Input;jumping:Z", ordinal = 2, opcode = Opcodes.GETFIELD), ordinal=5)
    private boolean selectivelyDisableElytra(boolean jumping) {
        return !elytraEnabled || jumping;
    }

}
