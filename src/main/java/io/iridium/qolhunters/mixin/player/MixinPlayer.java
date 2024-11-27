package io.iridium.qolhunters.mixin.player;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value = Player.BedSleepingProblem.class)
public class MixinPlayer {

    @Shadow @Final @Nullable private Component message;

    @Inject(method="getMessage", at=@At("HEAD"), cancellable = true)
    private void getMessage(CallbackInfoReturnable<Component> cir) {

        if( ((Player.BedSleepingProblem)(Object)this) == Player.BedSleepingProblem.NOT_SAFE) {

            String playerName = Minecraft.getInstance().player.getName().getString();
            if(Minecraft.getInstance().hasSingleplayerServer() && playerName.equals("HrryBrry")){
                cir.setReturnValue(new TextComponent("You may not rest now; there is a landlord waiting for rent."));
            }

        }

    }
}
