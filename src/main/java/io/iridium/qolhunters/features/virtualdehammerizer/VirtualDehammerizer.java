package io.iridium.qolhunters.features.virtualdehammerizer;

import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.item.tool.ToolItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.iridium.qolhunters.util.SharedFunctions.displayMessageOnScreen;

@Mod.EventBusSubscriber(modid= QOLHunters.MOD_ID, value = Dist.CLIENT)
public class VirtualDehammerizer {


    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getPlayer();
        if(player.level.dimension() != Level.OVERWORLD) return;

        ItemStack heldItem = player.getMainHandItem();
        Item item = heldItem.getItem();
        if (item != Items.WARPED_FUNGUS_ON_A_STICK) return;

        if(!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LCONTROL)) return;

        QOLHuntersClientConfigs.VIRTUAL_DEHAMMERIZER_X.set(player.blockPosition().getX());
        QOLHuntersClientConfigs.VIRTUAL_DEHAMMERIZER_Y.set(player.blockPosition().getY());
        QOLHuntersClientConfigs.VIRTUAL_DEHAMMERIZER_Z.set(player.blockPosition().getZ());
        displayMessageOnScreen(new TextComponent("Virtual Dehammerizer set to: " + player.blockPosition().getX() + ", " + player.blockPosition().getY() + ", " + player.blockPosition().getZ()).withStyle(ChatFormatting.GREEN));


    }

    @SubscribeEvent
    public static void onBlockStartBreak(InputEvent.ClickInputEvent event) {
        Player player = Minecraft.getInstance().player;
        if(player.level.dimension() != Level.OVERWORLD) return;

        ItemStack heldItem = player.getMainHandItem();
        if (!(heldItem.getItem() instanceof ToolItem)) return;

        VaultGearData data = VaultGearData.read(heldItem);
        if(!data.has(ModGearAttributes.HAMMERING)) return;

        Integer CenterX = QOLHuntersClientConfigs.VIRTUAL_DEHAMMERIZER_X.get();
        Integer CenterY = QOLHuntersClientConfigs.VIRTUAL_DEHAMMERIZER_Y.get();
        Integer CenterZ = QOLHuntersClientConfigs.VIRTUAL_DEHAMMERIZER_Z.get();
        BlockPos hammerizerPos = new BlockPos(CenterX, CenterY, CenterZ);
        Integer radius = QOLHuntersClientConfigs.VIRTUAL_DEHAMMERIZER_RANGE.get();
        double distance = player.blockPosition().distSqr(hammerizerPos);

        if (distance < radius * radius) {
            event.setCanceled(true);
        }



    }

}
