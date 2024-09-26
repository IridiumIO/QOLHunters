package io.iridium.qolhunters.networking;

import com.mojang.serialization.Decoder;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.networking.packet.HandshakeCheckModIsOnClientS2CPacket;
import io.iridium.qolhunters.networking.packet.HandshakeCheckModIsOnServerC2SPacket;
import io.iridium.qolhunters.networking.packet.HandshakeRespondModIsOnClientC2SPacket;
import io.iridium.qolhunters.networking.packet.HandshakeRespondModIsOnServerS2CPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {

    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() { return packetId++; }


    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(QOLHunters.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        
        INSTANCE = net;

        net.messageBuilder(HandshakeCheckModIsOnServerC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(HandshakeCheckModIsOnServerC2SPacket::new)
                .encoder(HandshakeCheckModIsOnServerC2SPacket::toBytes)
                .consumer(HandshakeCheckModIsOnServerC2SPacket::handle)
                .add();

        net.messageBuilder(HandshakeRespondModIsOnServerS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(HandshakeRespondModIsOnServerS2CPacket::new)
                .encoder(HandshakeRespondModIsOnServerS2CPacket::toBytes)
                .consumer(HandshakeRespondModIsOnServerS2CPacket::handle)
                .add();

        net.messageBuilder(HandshakeCheckModIsOnClientS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(HandshakeCheckModIsOnClientS2CPacket::new)
                .encoder(HandshakeCheckModIsOnClientS2CPacket::toBytes)
                .consumer(HandshakeCheckModIsOnClientS2CPacket::handle)
                .add();

        net.messageBuilder(HandshakeRespondModIsOnClientC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(HandshakeRespondModIsOnClientC2SPacket::new)
                .encoder(HandshakeRespondModIsOnClientC2SPacket::toBytes)
                .consumer(HandshakeRespondModIsOnClientC2SPacket::handle)
                .add();


    }


    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
