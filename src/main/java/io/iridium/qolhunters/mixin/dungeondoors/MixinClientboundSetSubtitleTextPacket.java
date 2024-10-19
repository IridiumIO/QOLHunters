package io.iridium.qolhunters.mixin.dungeondoors;

import io.iridium.qolhunters.features.dungeondoors.DungeonDifficultyWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundSetSubtitleTextPacket.class)
public abstract class MixinClientboundSetSubtitleTextPacket {

    @Inject(method="handle(Lnet/minecraft/network/protocol/game/ClientGamePacketListener;)V", at=@At("RETURN"))
    public void handle(ClientGamePacketListener pHandler, CallbackInfo ci) {

        String text = this.getText().getString();
        if(text.startsWith("Difficulty:")) {
            DungeonDifficultyWidget.difficulty = "Dungeon " + text;
            DungeonDifficultyWidget.dungeonPos = Minecraft.getInstance().player.getOnPos();
            DungeonDifficultyWidget.isDungeonActive = true;
            DungeonDifficultyWidget.difficultyColor = this.getText().getStyle().getColor().getValue();
        }

    }

    @Shadow
    public abstract Component getText();

}
