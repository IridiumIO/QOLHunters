package io.iridium.qolhunters.mixin.boosterpack;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.screen.BoosterPackSelectionScreen;
import iskallia.vault.item.BoosterPackItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static io.iridium.qolhunters.util.SharedFunctions.DataSlotToNetworkSlot;

@Mixin(value = BoosterPackSelectionScreen.CardSection.class)
public class MixinBoosterPackSelectionScreen {

    @Inject(method="onPress", at= @At(value = "INVOKE", target = "Lnet/minecraftforge/network/simple/SimpleChannel;sendToServer(Ljava/lang/Object;)V", shift = At.Shift.AFTER))
    private void onPress(CallbackInfo ci) {

        if(QOLHuntersClientConfigs.CHAIN_BOOSTER_PACKS.get()) {

            LocalPlayer player = Minecraft.getInstance().player;

            for (ItemStack stack: player.getInventory().items){
                if (stack.getItem() instanceof BoosterPackItem){

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
        }


    }


}
