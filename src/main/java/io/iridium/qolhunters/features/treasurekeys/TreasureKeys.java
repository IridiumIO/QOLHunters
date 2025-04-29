package io.iridium.qolhunters.features.treasurekeys;

import io.iridium.qolhunters.QOLHunters;
import iskallia.vault.block.TreasureDoorBlock;
import iskallia.vault.item.ItemVaultKey;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.iridium.qolhunters.util.SharedFunctions.DataSlotToNetworkSlot;

@Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
public class TreasureKeys {

    @SubscribeEvent
    public static void onRightClickDoorWithKey(PlayerInteractEvent.RightClickBlock event) {

        BlockHitResult hitResult = event.getHitVec();
        BlockPos blockPos = hitResult.getBlockPos();
        if (!(event.getWorld().getBlockState(blockPos).getBlock() instanceof TreasureDoorBlock)) return;

        BlockState blockState = event.getWorld().getBlockState(blockPos);
        Item targetKey = blockState.getValue(TreasureDoorBlock.TYPE).getKey();

        Player player = event.getPlayer();

        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof ItemVaultKey)) return;
        if (heldItem.getItem() == targetKey) return;

        for (ItemStack stack : player.getInventory().items) {
            if (!(stack.getItem() instanceof ItemVaultKey) || stack.getItem() != targetKey) continue;

            int doorMatchedKeySlot = DataSlotToNetworkSlot(player.getInventory().findSlotMatchingItem(stack));
            int currentKeySlot = DataSlotToNetworkSlot(player.getInventory().selected);

            Minecraft.getInstance().gameMode.handleInventoryMouseClick(player.containerMenu.containerId, doorMatchedKeySlot, 0, ClickType.PICKUP, player);
            Minecraft.getInstance().gameMode.handleInventoryMouseClick(player.containerMenu.containerId, currentKeySlot, 0, ClickType.PICKUP, player);
            Minecraft.getInstance().gameMode.handleInventoryMouseClick(player.containerMenu.containerId, doorMatchedKeySlot, 0, ClickType.PICKUP, player);

            Minecraft.getInstance().gameMode.useItem(player, player.level, InteractionHand.MAIN_HAND);
            break;

        }


    }

}
