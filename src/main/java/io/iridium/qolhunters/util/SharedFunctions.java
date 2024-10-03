package io.iridium.qolhunters.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.vault_scavenger.Scavenger;
import iskallia.vault.gear.VaultGearRarity;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.GuiUtils;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.network.RequestBackpackInventoryContentsMessage;
import net.p3pp3rf1y.sophisticatedbackpacks.network.SBPPacketHandler;

import java.util.HashMap;
import java.util.Map;

public class SharedFunctions {

    public static Map<String, Integer> CachedInventoryItems = new HashMap<>();
    public static long lastCheckedTime = 0;

    public static Map<String, Integer> GetPlayerInventoryItems(LocalPlayer player, Integer cacheTimeout) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckedTime < cacheTimeout) return new HashMap<>(CachedInventoryItems);

        Map<String, Integer> InventoryItems = new HashMap<>();

        for (InventoryUtil.ItemAccess items : InventoryUtil.findAllItems(player)) {
            ItemStack stack = items.getStack();
            if (stack.isEmpty()) continue;

            Item key = stack.getItem();
            InventoryItems.put(stack.getHoverName().getString(), InventoryItems.getOrDefault(stack.getHoverName().getString(), 0) + stack.getCount());
            if (!(key instanceof BackpackItem)) continue;

            stack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance()).ifPresent((w) ->
                    w.getContentsUuid().ifPresent((uuid) ->
                            SBPPacketHandler.INSTANCE.sendToServer(new RequestBackpackInventoryContentsMessage(uuid))));


        }

        CachedInventoryItems = InventoryItems;
        lastCheckedTime = currentTime;
        return new HashMap<>(CachedInventoryItems);

    }


    public static Integer GetPlayerInventoryItemCount(LocalPlayer player, ItemStack itemStack, Integer cacheTimeout) {
        return GetPlayerInventoryItems(player, cacheTimeout).getOrDefault(itemStack.getHoverName().getString(), 0);
    }


    public static String formatNumber(int number) {
        if (number >= 10_000_000) {
            return String.format("%.1fM", number / 1_000_000_000.0);
        } else
        if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 100_000) {
                return String.format("%.0fK", number / 1_000.0);
        } else if (number >= 10_000) {
            return String.format("%.1fK", number / 1_000.0);
        } else if (number >= 1_000) {
            return String.format("%.2fK", number / 1_000.0);
        } else {
            return String.valueOf(number);
        }
    }

    public static String formatNumberWithDecimal(double number) {
        if (number >= 10_000_000) {
            return String.format("%.1fM", number / 1_000_000_000.0);
        } else
        if (number >= 1_000_000) {
            return String.format("%.1fM", number / 1_000_000.0);
        } else if (number >= 100_000) {
            return String.format("%.0fK", number / 1_000.0);
        } else if (number >= 10_000) {
            return String.format("%.1fK", number / 1_000.0);
        } else if (number >= 1_000) {
            return String.format("%.2fK", number / 1_000.0);
        } else if (number >= 100){
            return String.format("%.0f", number);
        }else {
            return String.format("%.2f", number);
        }
    }


    public static void renderSlotHighlight(PoseStack poseStack, ItemStack itemStack, int x, int y){

        if (!Scavenger.ScavengerItems.containsKey(itemStack.getHoverName().getString())) return;


        RenderSystem.disableDepthTest();
        poseStack.pushPose();
        poseStack.translate(0, 0, 100);
        Matrix4f matrix = poseStack.last().pose();

        int color = 0xDD000000 | Scavenger.ScavengerItems.get(itemStack.getHoverName().getString());

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(bufferBuilder);

        if(true){
            GuiUtils.drawGradientRect(matrix, -1, x,y + 4,x + 16,y + 16, 0x00000000, color);
        }
        else {
            GuiUtils.drawGradientRect(matrix, -1, x, y + 15,x + 16,y + 16, color, color);
        }

        bufferSource.endBatch();
        poseStack.popPose();


    }

    public static void renderSlotRarityHighlight(PoseStack poseStack, ItemStack itemStack, int x, int y){

        if (!(itemStack.getItem() instanceof VaultGearItem)) return;

        VaultGearData gearData = VaultGearData.read(itemStack);

        VaultGearRarity rarity = gearData.getRarity();
        if (rarity == VaultGearRarity.SCRAPPY || rarity == VaultGearRarity.COMMON) return;

        if (rarity == VaultGearRarity.RARE && !QOLHuntersClientConfigs.RARITY_HIGHLIGHTER_RARE.get()) return;
        if (rarity == VaultGearRarity.EPIC && !QOLHuntersClientConfigs.RARITY_HIGHLIGHTER_EPIC.get()) return;
        if (rarity == VaultGearRarity.OMEGA && !QOLHuntersClientConfigs.RARITY_HIGHLIGHTER_OMEGA.get()) return;
        if (rarity == VaultGearRarity.UNIQUE && !QOLHuntersClientConfigs.RARITY_HIGHLIGHTER_UNIQUE.get()) return;

        int color = 0xFF000000 | rarity.getColor().getValue();


        RenderSystem.disableDepthTest();
        poseStack.pushPose();
        poseStack.translate(0, 0, 100);
        Matrix4f matrix = poseStack.last().pose();


        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(bufferBuilder);

        if(QOLHuntersClientConfigs.RARITY_HIGHLIGHTER_MODE.get() == QOLHuntersClientConfigs.RarityHighlighterMode.GRADIENT){
            GuiUtils.drawGradientRect(matrix, -99, x,y + 1,x + 16,y + 16, 0x00000000, color);
        }
        else {
            GuiUtils.drawGradientRect(matrix, -99, x, y + 14,x + 16,y + 16, color, color);
        }

        bufferSource.endBatch();
        poseStack.popPose();


    }



    public static int DataSlotToNetworkSlot(int index) {
        if(index == 100)
            index = 8;
        else if(index == 101)
            index = 7;
        else if(index == 102)
            index = 6;
        else if(index == 103)
            index = 5;
        else if(index == -106)
            index = 45;
        else if(index <= 8)
            index += 36;
        else if(index >= 80 && index <= 83)
            index -= 79;
        return index;
    }



    @OnlyIn(Dist.CLIENT)
    public static void displayTitleOnScreen(Component message) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> mc.gui.setTitle(message));
    }

    @OnlyIn(Dist.CLIENT)
    public static void displaySubtitleOnScreen(Component message) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> mc.gui.setSubtitle(message));
    }

    @OnlyIn(Dist.CLIENT)
    public static void displayMessageOnScreen(Component message) {
        Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> {
            mc.gui.setOverlayMessage(message, false);
        });
    }



}
