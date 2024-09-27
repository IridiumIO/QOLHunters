package io.iridium.qolhunters.events;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.vaultenchanteremeraldslot.VaultEnchanterEmeraldSlot;
import io.iridium.qolhunters.networking.ModMessages;
import io.iridium.qolhunters.networking.packet.HandshakeCheckModIsOnClientS2CPacket;
import io.iridium.qolhunters.networking.packet.HandshakeRespondModIsOnClientC2SPacket;
import iskallia.vault.block.VaultEnchanterBlock;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.iridium.qolhunters.QOLHunters.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        BlockState blockState = event.getWorld().getBlockState(event.getPos());
        if (blockState.getBlock() instanceof VaultEnchanterBlock) {
            if(!(event.getPlayer() instanceof ServerPlayer)) {
//                QOLHunters.LOGGER.info("PING sent from Client!");
                ModMessages.sendToServer(new HandshakeRespondModIsOnClientC2SPacket(QOLHuntersClientConfigs.VAULT_ENCHANTER_EMERALDS_SLOT.get()));
            }
//            QOLHunters.LOGGER.info("Vault Enchanter Block Right Clicked");
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer)) return;
//        QOLHunters.LOGGER.info("Player Joined: " + ((ServerPlayer) event.getPlayer()).getUUID());
        VaultEnchanterEmeraldSlot.playerHasMod.put((ServerPlayer) event.getPlayer(), false);
        ModMessages.sendToClient(new HandshakeCheckModIsOnClientS2CPacket(), (ServerPlayer) event.getPlayer());
    }

}
