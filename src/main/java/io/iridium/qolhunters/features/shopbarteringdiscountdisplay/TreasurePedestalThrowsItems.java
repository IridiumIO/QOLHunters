package io.iridium.qolhunters.features.shopbarteringdiscountdisplay;

import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.block.TreasurePedestalBlock;
import iskallia.vault.block.entity.TreasurePedestalTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.iridium.qolhunters.util.SharedFunctions.DataSlotToNetworkSlot;

@Mod.EventBusSubscriber(modid= QOLHunters.MOD_ID, value = Dist.CLIENT)
public class TreasurePedestalThrowsItems {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getPlayer();
        if(!player.level.dimension().location().toString().startsWith("the_vault:vault")) return;

        if(!QOLHuntersClientConfigs.ENABLE_TREASURE_PED_THROW_ITEMS.get()) return;

        BlockState block = event.getWorld().getBlockState(event.getPos());

        if (!(block.getBlock() instanceof TreasurePedestalBlock)) return;

        TreasurePedestalTileEntity tile = (TreasurePedestalTileEntity) event.getWorld().getBlockEntity(event.getPos());

        if (tile == null || tile.getContained().isEmpty()) return;

        for(int i = 0; i < player.getInventory().getContainerSize(); i++) {
            snapshotInventoryList.put(i, player.getInventory().getItem(i).getItem());
        }

        pedestalPosition = event.getPos();
        lastPurchasedItem = tile.getContained().copy();
        MinecraftForge.EVENT_BUS.register(PlayerTickHandler.class);


        // Schedule a task to unregister the PlayerTickHandler after 1 second
        scheduler.schedule(TreasurePedestalThrowsItems::resetState, 1, TimeUnit.SECONDS);

    }

    private static BlockPos pedestalPosition = null;
    private static ItemStack lastPurchasedItem = ItemStack.EMPTY;
    private static final Map<Integer, Item> snapshotInventoryList = new HashMap<>();


    private static void resetState() {
        MinecraftForge.EVENT_BUS.unregister(PlayerTickHandler.class);
        lastPurchasedItem = ItemStack.EMPTY;
        pedestalPosition = null;
        snapshotInventoryList.clear();
    }


    @OnlyIn(Dist.CLIENT)
    public static class PlayerTickHandler {

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

            if (lastPurchasedItem.isEmpty() || pedestalPosition == null) return;

            Player player = event.player;
            Inventory newInventory = player.getInventory();

            for(int slot = 0; slot < newInventory.getContainerSize(); slot++) {
                Item oldItem = snapshotInventoryList.get(slot);
                Item newItem = newInventory.getItem(slot).getItem();

                if (oldItem == newItem) continue;

                Minecraft.getInstance().gameMode.handleInventoryMouseClick(player.containerMenu.containerId, DataSlotToNetworkSlot(slot), 0, ClickType.THROW, player);
                resetState();
                break;
            }
        }
    }


}
