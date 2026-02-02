package io.iridium.qolhunters.features.itemchaining;

import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.QOLHunters;
import iskallia.vault.antique.Antique;
import iskallia.vault.config.AntiquesConfig;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.AntiqueItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ClickType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.iridium.qolhunters.util.SharedFunctions.DataSlotToNetworkSlot;

@Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
public class AntiqueChaining {
    private static boolean wasAntiqueOpened = false;
    private static ResourceLocation lastAntiqueId = null;

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (event.getSide().isServer()) {
            // handling server events in singleplayer would open pouch in your inventory even if machine opened another pouch
            return;
        }
        if (event.getItemStack().getItem() == ModItems.ANTIQUE) {
            var ant = AntiqueItem.getAntique(event.getItemStack());
            if (ant != null)
                lastAntiqueId = ant.getRegistryName();
            wasAntiqueOpened = true;
            scheduler.schedule(() -> MinecraftForge.EVENT_BUS.register(CursedTicker.class), 1, TimeUnit.SECONDS);
        }
    }


    private static class CursedTicker{

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_ESCAPE) ){
                wasAntiqueOpened = false;
                MinecraftForge.EVENT_BUS.unregister(CursedTicker.class);
                return;
            }
            if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().screen == null) {
                if (wasAntiqueOpened) {

                    wasAntiqueOpened = false;

                    LocalPlayer player = Minecraft.getInstance().player;

                    if (player != null) {
                        for (int i = 0;  i < player.getInventory().items.size(); i++){
                            var stack = player.getInventory().items.get(i);
                            if (stack.getItem() == ModItems.ANTIQUE){
                                var ant = AntiqueItem.getAntique(stack);
                                int currCount = stack.getCount();
                                int requiredCount = Optional.ofNullable(ant).map(Antique::getConfig).map(
                                    AntiquesConfig.Entry::getInfo).map(AntiquesConfig.Info::getRequiredCount).orElse(1);

                                if (ant == null
                                    || lastAntiqueId == null
                                    || !lastAntiqueId.equals(ant.getRegistryName())
                                    || currCount < requiredCount) {
                                    continue;
                                }

                                int currentBoosterSlot = DataSlotToNetworkSlot(player.getInventory().selected);
                                int nextBoosterSlot = DataSlotToNetworkSlot(i);
                                if (currentBoosterSlot == nextBoosterSlot || currentBoosterSlot == -1) continue;

                                Minecraft.getInstance().gameMode.handleInventoryMouseClick(player.containerMenu.containerId, nextBoosterSlot, 0, ClickType.PICKUP, player);
                                Minecraft.getInstance().gameMode.handleInventoryMouseClick(player.containerMenu.containerId, currentBoosterSlot, 0, ClickType.PICKUP, player);
                                Minecraft.getInstance().gameMode.handleInventoryMouseClick(player.containerMenu.containerId, nextBoosterSlot, 0, ClickType.PICKUP, player);

                                break;
                            }

                        }
                    }

                    MinecraftForge.EVENT_BUS.unregister(CursedTicker.class);
                    lastAntiqueId = null;
                }
            }
        }

    }



}
