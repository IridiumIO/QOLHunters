package io.iridium.qolhunters.features.gearcooldowntimer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.config.Config;
import iskallia.vault.config.gear.VaultGearCommonConfig;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.CardDeckItem;
import iskallia.vault.item.MagnetItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
public class GearCooldownTimer {

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.DrawScreenEvent.Post event) {

        Player player = Minecraft.getInstance().player;
        if (!(QOLHuntersClientConfigs.SHOW_GEAR_COOLDOWN_TIME.get()) || (event.getScreen() instanceof CreativeModeInventoryScreen) || !(event.getScreen() instanceof AbstractContainerScreen<?> screen))
            return;

        ItemCooldowns cooldowns = player.getCooldowns();

        for (Slot slot : screen.getMenu().slots) {
            ItemStack itemStack = slot.getItem();
            Item item = itemStack.getItem();
            if (cooldowns.isOnCooldown(item) && (item instanceof VaultGearItem || item instanceof CardDeckItem || item instanceof MagnetItem || item instanceof ElytraItem)) {
                float cooldownPercent = cooldowns.getCooldownPercent(item, 0F);

                float maxTime = ModConfigs.VAULT_GEAR_COMMON.getSwapCooldown() / 20f;
                int absoluteCooldownSeconds = Math.round(cooldownPercent * maxTime);
                PoseStack poseStack = event.getPoseStack();
                int x = slot.x + screen.getGuiLeft();
                int y = slot.y + screen.getGuiTop();

                RenderSystem.disableDepthTest();
                poseStack.pushPose();
                poseStack.translate(x, y, 400);

                poseStack.scale(0.6F, 0.6F, 1.0F);
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder bufferBuilder = tesselator.getBuilder();
                MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(bufferBuilder);

                Font font = Minecraft.getInstance().font;
                font.drawInBatch(new TextComponent(absoluteCooldownSeconds + "s").withStyle(ChatFormatting.GREEN), 0, (int) (16 / 0.6) - font.lineHeight, 0xFFFFFFFF, true, poseStack.last().pose(), bufferSource, false, 0, 15728880);
                bufferSource.endBatch();

                poseStack.popPose();
            }
        }
    }


}
