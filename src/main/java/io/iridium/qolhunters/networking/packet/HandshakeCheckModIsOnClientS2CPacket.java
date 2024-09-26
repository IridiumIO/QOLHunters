package io.iridium.qolhunters.networking.packet;

import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.networking.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class HandshakeCheckModIsOnClientS2CPacket {


    public HandshakeCheckModIsOnClientS2CPacket() {}

    public HandshakeCheckModIsOnClientS2CPacket(FriendlyByteBuf buf){}

    public void toBytes(FriendlyByteBuf buf){}

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // THIS IS ALL ON THE CLIENT!!!!

            ModMessages.sendToServer(new HandshakeRespondModIsOnClientC2SPacket(QOLHuntersClientConfigs.VAULT_ENCHANTER_EMERALDS_SLOT.get()));


        });

        return true;
    }

}
