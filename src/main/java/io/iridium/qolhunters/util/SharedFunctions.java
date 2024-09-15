package io.iridium.qolhunters.util;

import iskallia.vault.util.InventoryUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.network.RequestBackpackInventoryContentsMessage;
import net.p3pp3rf1y.sophisticatedbackpacks.network.SBPPacketHandler;

import java.util.HashMap;
import java.util.Map;

public class SharedFunctions {

    public static Map<Item, Integer> CachedInventoryItems = new HashMap<>();
    public static long lastCheckedTime = 0;

    public static Map<Item, Integer> GetPlayerInventoryItems(LocalPlayer player, Integer cacheTimeout) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckedTime < cacheTimeout) return new HashMap<>(CachedInventoryItems);

        Map<Item, Integer> InventoryItems = new HashMap<>();

        for (InventoryUtil.ItemAccess items : InventoryUtil.findAllItems(player)) {
            ItemStack stack = items.getStack();
            if (stack.isEmpty()) continue;

            Item key = stack.getItem();
            InventoryItems.put(key, InventoryItems.getOrDefault(key, 0) + stack.getCount());
            if (!(key instanceof BackpackItem)) continue;

            stack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance()).ifPresent((w) ->
                    w.getContentsUuid().ifPresent((uuid) ->
                            SBPPacketHandler.INSTANCE.sendToServer(new RequestBackpackInventoryContentsMessage(uuid))));


        }

        CachedInventoryItems = InventoryItems;
        lastCheckedTime = currentTime;
        return new HashMap<>(CachedInventoryItems);

    }


    public static Integer GetPlayerInventoryItemCount(LocalPlayer player, Item item, Integer cacheTimeout) {
        return GetPlayerInventoryItems(player, cacheTimeout).getOrDefault(item, 0);
    }


    public static String formatNumber(int number) {
        if (number >= 1_000_000) {
            return String.format("%.2fM", number / 1_000_000.0);
        } else if (number >= 1_000) {
            return String.format("%.2fK", number / 1_000.0);
        } else {
            return String.valueOf(number);
        }
    }


}
