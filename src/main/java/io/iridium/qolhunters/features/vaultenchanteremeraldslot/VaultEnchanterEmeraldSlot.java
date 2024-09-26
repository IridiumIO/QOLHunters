package io.iridium.qolhunters.features.vaultenchanteremeraldslot;

import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.events.ClientForgeEvents;
import io.iridium.qolhunters.networking.ModMessages;
import io.iridium.qolhunters.networking.packet.HandshakeRespondModIsOnClientC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class VaultEnchanterEmeraldSlot {

    @Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
    public static class EnchanterEvents {

        private static Boolean isVaultEnchanterEmeraldSlotEnabledClient = null;
        private static long LastCheckedTime = 0;

        @SubscribeEvent
        public static void CheckIfVaultEnchanterEmeraldSlotChanged(TickEvent.PlayerTickEvent event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            mc.execute(() -> {
                if (System.currentTimeMillis() < LastCheckedTime + 2000 ||
                        (isVaultEnchanterEmeraldSlotEnabledClient == QOLHuntersClientConfigs.VAULT_ENCHANTER_EMERALDS_SLOT.get())) {
                    return;
                }
//                QOLHunters.LOGGER.info("Updating Client config!");
                isVaultEnchanterEmeraldSlotEnabledClient = QOLHuntersClientConfigs.VAULT_ENCHANTER_EMERALDS_SLOT.get();
                ModMessages.sendToServer(new HandshakeRespondModIsOnClientC2SPacket(isVaultEnchanterEmeraldSlotEnabledClient));
                LastCheckedTime = System.currentTimeMillis();
            });
        }

    }


    public static boolean isSlotEnabled(Player player) {
        if(!(Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)) {
//            QOLHunters.LOGGER.error("isSlotEnabled called on client side");
            return isSlotEnabledClient();
        } else {
//            QOLHunters.LOGGER.error("isSlotEnabled called on server side");

            ServerPlayer serverPlayer = (ServerPlayer) player;
            if (!(player instanceof ServerPlayer)) {
//                QOLHunters.LOGGER.error("Player is not a ServerPlayer!");
                return false;
            }
            return isSlotEnabledServer(serverPlayer);
        }
    }

    public static boolean isSlotEnabledClient() {
        return QOLHuntersClientConfigs.VAULT_ENCHANTER_EMERALDS_SLOT.get() && ClientForgeEvents.MOD_MODE == QOLHunters.ModMode.CLIENTANDSERVER;
    }

    public static boolean isSlotEnabledServer(ServerPlayer player) {
        return playerHasMod.getOrDefault(player, false);
    }

    public static HashMap<ServerPlayer, Boolean> playerHasMod = new HashMap<>();


    public static boolean qol$canAddItemToSlot(@Nullable Slot slot, @Nonnull ItemStack stack, boolean stackSizeMatters) {
        boolean flag = slot == null || !slot.hasItem();
        if (slot != null) {
            ItemStack slotStack = slot.getItem();
            if (!flag && stack.sameItem(slotStack) && ItemStack.tagMatches(slotStack, stack)) {
                return slotStack.getCount() + (stackSizeMatters ? 0 : stack.getCount()) <= slot.getMaxStackSize(slotStack);
            }
        }

        return flag;
    }


    public static boolean qol$moveOverSizedItemStackTo(ItemStack sourceStack, @Nullable Slot sourceSlot, int startIndex, int endIndex, boolean reverseDirection, NonNullList<Slot> slots) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }

        for (; !sourceStack.isEmpty() && (reverseDirection ? i >= startIndex : i < endIndex); i += reverseDirection ? -1 : 1) {
            Slot slot = slots.get(i);
            ItemStack slotStack = slot.getItem();
            if (!slotStack.isEmpty()
                    && slotStack.getItem() == sourceStack.getItem()
                    && ItemStack.tagMatches(sourceStack, slotStack)
                    && slot.mayPlace(sourceStack)) {
                int j = slotStack.getCount() + sourceStack.getCount();
                int maxSize = slot.getMaxStackSize(slotStack);
                if (j <= maxSize) {
                    sourceStack.setCount(0);
                    if (sourceSlot != null) {
                        sourceSlot.set(sourceStack);
                    }

                    slotStack.setCount(j);
                    slot.set(slotStack);
                    slot.setChanged();
                    flag = true;
                } else if (slotStack.getCount() < maxSize) {
                    sourceStack.shrink(maxSize - slotStack.getCount());
                    if (sourceSlot != null) {
                        sourceSlot.set(sourceStack);
                    }

                    slotStack.setCount(maxSize);
                    slot.set(slotStack);
                    slot.setChanged();
                    flag = true;
                }
            }
        }

        if (!sourceStack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (reverseDirection ? i >= startIndex : i < endIndex) {
                Slot slot1 = slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(sourceStack)) {
                    if (sourceStack.getCount() > slot1.getMaxStackSize(sourceStack)) {
                        slot1.set(sourceStack.split(slot1.getMaxStackSize(sourceStack)));
                    } else {
                        slot1.set(sourceStack.split(sourceStack.getCount()));
                    }

                    if (sourceSlot != null) {
                        sourceSlot.set(sourceStack);
                    }

                    slot1.setChanged();
                    flag = true;
                    break;
                }

                i += reverseDirection ? -1 : 1;
            }
        }

        return flag;
    }


}
