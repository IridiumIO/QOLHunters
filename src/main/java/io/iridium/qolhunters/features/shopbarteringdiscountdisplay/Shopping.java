package io.iridium.qolhunters.features.shopbarteringdiscountdisplay;

import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.util.SharedFunctions;
import iskallia.vault.block.ShopPedestalBlock;
import iskallia.vault.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawSelectionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= QOLHunters.MOD_ID, value = Dist.CLIENT)
public class Shopping {

    public static boolean isLookingAtShopPedestal = false;
    public static int invGoldCount = 0;


    @SubscribeEvent
    public static void onBlockHover(DrawSelectionEvent.HighlightBlock event) {

        Player player = Minecraft.getInstance().player;
        if (player == null || !player.level.dimension().location().toString().startsWith("the_vault:vault")) return;

        BlockHitResult hitResult = event.getTarget();
        Block block = Minecraft.getInstance().level.getBlockState(hitResult.getBlockPos()).getBlock();

        if(!(block instanceof ShopPedestalBlock)) {
            if(isLookingAtShopPedestal) isLookingAtShopPedestal = false;
            return;
        }
        isLookingAtShopPedestal = true;
        ItemStack bronzeItem = new ItemStack(ModBlocks.VAULT_BRONZE.asItem());
        ItemStack silverItem = new ItemStack(ModBlocks.VAULT_SILVER.asItem());
        ItemStack goldItem = new ItemStack(ModBlocks.VAULT_GOLD.asItem());

        //TODO: Optimize this so it doesn't run a separate inventory search for each item
        int BronzeCount = SharedFunctions.GetPlayerInventoryItemCount(Minecraft.getInstance().player, bronzeItem, 200);
        int SilverCount = SharedFunctions.GetPlayerInventoryItemCount(Minecraft.getInstance().player, silverItem, 200) + (BronzeCount/9);
        int GoldCount = SharedFunctions.GetPlayerInventoryItemCount(Minecraft.getInstance().player, goldItem, 200) + (SilverCount/9);
        invGoldCount = GoldCount;

    }


}
