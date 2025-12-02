package io.iridium.qolhunters.features.vault_scavenger;

import io.iridium.qolhunters.QOLHunters;
import iskallia.vault.util.InventoryUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.network.RequestBackpackInventoryContentsMessage;
import net.p3pp3rf1y.sophisticatedbackpacks.network.SBPPacketHandler;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;


@Mod.EventBusSubscriber(modid= QOLHunters.MOD_ID, value = Dist.CLIENT)
public class Scavenger {

    public static Map<NamedItem, Integer> scavengerColors = new HashMap<>();
    public static Map<NamedItem, Integer> cachedInventoryItems = new HashMap<>();

    public static long lastCheckedTime = 0;

    public static Map<NamedItem, Integer> getPlayerInventoryItems(LocalPlayer player, Integer cacheTimeout) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckedTime < cacheTimeout) return new HashMap<>(cachedInventoryItems);

        Map<NamedItem, Integer> inventoryItems = new HashMap<>();

        for (InventoryUtil.ItemAccess items : InventoryUtil.findAllItems(player)) {
            ItemStack stack = items.getStack();
            if (stack.isEmpty()) continue;

            Item key = stack.getItem();
            var scavItem = NamedItem.of(stack);
            inventoryItems.put(NamedItem.of(stack), inventoryItems.getOrDefault(scavItem, 0) + stack.getCount());
            if (!(key instanceof BackpackItem)) continue;

            stack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance()).ifPresent((w) ->
                w.getContentsUuid().ifPresent((uuid) ->
                    SBPPacketHandler.INSTANCE.sendToServer(new RequestBackpackInventoryContentsMessage(uuid))));


        }

        cachedInventoryItems = inventoryItems;
        lastCheckedTime = currentTime;
        return new HashMap<>(cachedInventoryItems);

    }


    public static Integer getPlayerInventoryItemCount(LocalPlayer player, NamedItem scavItem, Integer cacheTimeout) {
        return getPlayerInventoryItems(player, cacheTimeout).getOrDefault(scavItem, 0);
    }


    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {

        if (event.getKey() == GLFW.GLFW_KEY_Q && event.getAction() == GLFW.GLFW_PRESS &&
                (event.getModifiers() & GLFW.GLFW_MOD_SHIFT) != 0 &&
                (event.getModifiers() & GLFW.GLFW_MOD_CONTROL) != 0 &&
                (event.getModifiers() & GLFW.GLFW_MOD_ALT) != 0)
        {
            Scavenger.scavengerColors.clear();
            QOLHunters.LOGGER.info("Scavenger items cleared");
        }

    }


    @SubscribeEvent
    public static void onVaultComplete(ClientPlayerNetworkEvent.RespawnEvent event) {
        Scavenger.scavengerColors.clear();
    }




}
