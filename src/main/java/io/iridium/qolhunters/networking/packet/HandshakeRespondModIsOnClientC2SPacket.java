package io.iridium.qolhunters.networking.packet;

import io.iridium.qolhunters.features.vaultenchanteremeraldslot.VaultEnchanterEmeraldSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class HandshakeRespondModIsOnClientC2SPacket {


    private boolean isEnabled;

    public HandshakeRespondModIsOnClientC2SPacket(boolean isEnabled) { this.isEnabled = isEnabled; }

    public HandshakeRespondModIsOnClientC2SPacket(FriendlyByteBuf buf){
        this.isEnabled = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeBoolean(this.isEnabled);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            // THIS IS ALL ON THE SERVER!!!!
//            QOLHunters.LOGGER.info("Mod is enabled on client: " + isEnabled);
            VaultEnchanterEmeraldSlot.playerHasMod.put(context.getSender(), isEnabled);
        });

        return true;
    }

}
