package io.iridium.qolhunters.networking.packet;

import io.iridium.qolhunters.events.ClientForgeEvents;
import io.iridium.qolhunters.QOLHunters;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class HandshakeRespondModIsOnServerS2CPacket {


    public HandshakeRespondModIsOnServerS2CPacket() {}

    public HandshakeRespondModIsOnServerS2CPacket(FriendlyByteBuf buf){}

    public void toBytes(FriendlyByteBuf buf){}

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // THIS IS ALL ON THE CLIENT!!!!
            QOLHunters.LOGGER.info("Mod is enabled on server: true");
            ClientForgeEvents.MOD_MODE = QOLHunters.ModMode.CLIENTANDSERVER;

        });

        return true;
    }

}
