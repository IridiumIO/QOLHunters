package io.iridium.qolhunters.features.absolutelycursedjewelpouchopener;

import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.QOLHunters;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.iridium.qolhunters.util.SharedFunctions.DataSlotToNetworkSlot;

@Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
public class AbsolutelyCursedJewelPouchOpener {


    private static boolean wasJewelPouchOpened = false;

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        if (event.getSide().isServer()) {
            // handling server events in singleplayer would open pouch in your inventory even if machine opened another pouch
            return;
        }
        if (event.getItemStack().getHoverName().getString().contains("Jewel Pouch")) {
            wasJewelPouchOpened = true;
            scheduler.schedule(() -> MinecraftForge.EVENT_BUS.register(CursedTicker.class), 1, TimeUnit.SECONDS);
        }
    }


    private static class CursedTicker{

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_ESCAPE) ){
                wasJewelPouchOpened = false;
                MinecraftForge.EVENT_BUS.unregister(CursedTicker.class);
                return;
            }
            if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().screen == null) {
                if (wasJewelPouchOpened) {

                    wasJewelPouchOpened = false;

                    LocalPlayer player = Minecraft.getInstance().player;

                    for (ItemStack stack: player.getInventory().items){
                        if (stack.getHoverName().getString().contains("Jewel Pouch")){

                            int currentBoosterSlot = DataSlotToNetworkSlot(player.getInventory().selected);
                            int nextBoosterSlot = DataSlotToNetworkSlot(player.getInventory().findSlotMatchingItem(stack));
                            if (currentBoosterSlot == nextBoosterSlot || nextBoosterSlot == -1 || currentBoosterSlot == -1) continue;

                            Minecraft.getInstance().gameMode.handleInventoryMouseClick(player.containerMenu.containerId, nextBoosterSlot, 0, ClickType.PICKUP, player);
                            Minecraft.getInstance().gameMode.handleInventoryMouseClick(player.containerMenu.containerId, currentBoosterSlot, 0, ClickType.PICKUP, player);
                            Minecraft.getInstance().gameMode.handleInventoryMouseClick(player.containerMenu.containerId, nextBoosterSlot, 0, ClickType.PICKUP, player);

                            Minecraft.getInstance().gameMode.useItem(player, player.level, InteractionHand.MAIN_HAND);

                            break;
                        }

                    }

                    MinecraftForge.EVENT_BUS.unregister(CursedTicker.class);

                }
            }
        }

    }



}
