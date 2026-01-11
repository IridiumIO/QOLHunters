package io.iridium.qolhunters.features.zerousesalert;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.core.vault.ClientVaults;
import iskallia.vault.item.gear.IVaultUsesItem;
import iskallia.vault.item.gear.VaultUsesHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
public class ZeroUsesAlert {

    private static int alertColor = 0xFF0000; // Red
    private static int firstBrokenTick = 0; // only show alert after 20 ticks (1 second) - prevent flashing when traveling from/to vaults where the check logic changes
    private static Component lastText = new TextComponent("");
    private static boolean shouldDraw = false;
    private static int lastTick = 0;

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {return;}
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator() || ClientVaults.getActive().isPresent()) {return;}
        if (!(QOLHuntersClientConfigs.ZERO_USES_ALERT.get())) {return;}

        if (lastTick == Minecraft.getInstance().player.tickCount) {
            if (shouldDraw) {
                drawText(event.getMatrixStack(), lastText);
            }
            return;
        }
        var zeroUseCurios = getZeroUseCurios(player);

        lastTick = player.tickCount;
        shouldDraw = false;
        if (zeroUseCurios.isEmpty()) {
            firstBrokenTick = 0; // reset the tick counter if no broken curios
            return;
        }

        if (firstBrokenTick == 0 || player.tickCount < firstBrokenTick) {
            firstBrokenTick = player.tickCount;
            return;
        }
        if (player.tickCount - firstBrokenTick < 20) {
            return; // only show alert after 20 ticks (1 second)
        }

        MutableComponent text = new TextComponent("Zero uses: ");
        boolean first = true;
        for (var curio: zeroUseCurios) {
            if (!first) {
                text.append(", ");
            }
            first = false;
            text.append(curio.getHoverName());
        }

        text.withStyle(ChatFormatting.BOLD);
        text.withStyle(ChatFormatting.UNDERLINE);
        shouldDraw = true;
        lastText = text;
        drawText(event.getMatrixStack(), text);
    }

    private static void drawText(PoseStack matrixStack, Component text) {
        Minecraft mc = Minecraft.getInstance();
        Font fontRenderer = mc.font;
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        int x = width / 2 - fontRenderer.width(text) / 2;
        int y = height - 65;

        RenderSystem.enableBlend();
        fontRenderer.draw(matrixStack, text, x, y, alertColor);
        RenderSystem.disableBlend();
    }

    private static List<ItemStack> getZeroUseCurios(Player player) {
        ArrayList<ItemStack> zeroUses =  new ArrayList<>();
        var vaultUseCurios = CuriosApi
            .getCuriosHelper()
            .findCurios(player, (stack) -> stack.getItem() instanceof IVaultUsesItem);
        for (var slot: vaultUseCurios) {
            var stack = slot.stack();
            if (!VaultUsesHelper.hasUsesLeft(stack)) {
                zeroUses.add(stack);
            }
        }
        return zeroUses;
    }

}
