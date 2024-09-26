package io.iridium.qolhunters.networking.packet;

import io.iridium.qolhunters.networking.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class HandshakeCheckModIsOnServerC2SPacket {


    public HandshakeCheckModIsOnServerC2SPacket() {}

    public HandshakeCheckModIsOnServerC2SPacket(FriendlyByteBuf buf){}

    public void toBytes(FriendlyByteBuf buf){}

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            // THIS IS ALL ON THE SERVER!!!!
            ModMessages.sendToClient(new HandshakeRespondModIsOnServerS2CPacket(), context.getSender());
        });

        return true;
    }

}
