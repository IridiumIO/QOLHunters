package io.iridium.qolhunters.features.consumeall;

import io.iridium.qolhunters.QOLHunters;
import iskallia.vault.item.GatedLootableItem;
import iskallia.vault.item.ItemKnowledgeStar;
import iskallia.vault.item.LootableItem;
import iskallia.vault.item.RegretOrbItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
public class ConsumeAll {

    private static boolean isConsuming = false;

    @SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.RightClickItem event) {

        if(isConsuming || !Screen.hasShiftDown()) return;

        ItemStack stack = event.getItemStack();

        if(stack.getItem() instanceof ItemKnowledgeStar || stack.getItem() instanceof RegretOrbItem || stack.getItem() instanceof LootableItem || stack.getItem() instanceof GatedLootableItem){
            if (event.getPlayer().getAbilities().instabuild) {
                event.getPlayer().displayClientMessage(new TextComponent("Cannot consume all while in creative mode!").withStyle(ChatFormatting.RED), true);
                return;
            }
            int count = stack.getCount();
            isConsuming = true;
            for(int i = 0; i < count; i++){
                Minecraft.getInstance().gameMode.useItem(event.getPlayer(), event.getPlayer().level, event.getHand());
            }
            isConsuming = false;
        }

    }
}
