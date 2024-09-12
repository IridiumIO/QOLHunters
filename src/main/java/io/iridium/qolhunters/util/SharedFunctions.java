package io.iridium.qolhunters.util;

import io.iridium.qolhunters.QOLHunters;
import iskallia.vault.item.BasicScavengerItem;
import iskallia.vault.util.InventoryUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackContainer;
import net.p3pp3rf1y.sophisticatedbackpacks.network.RequestBackpackInventoryContentsMessage;
import net.p3pp3rf1y.sophisticatedbackpacks.network.SBPPacketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

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



}
