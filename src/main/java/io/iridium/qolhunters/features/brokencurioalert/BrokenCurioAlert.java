package io.iridium.qolhunters.features.brokencurioalert;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.QOLHunters;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.core.vault.ClientVaults;
import iskallia.vault.gear.trinket.TrinketHelper;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.gear.VaultCharmItem;
import iskallia.vault.item.gear.VoidStoneItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = QOLHunters.MOD_ID, value = Dist.CLIENT)
public class BrokenCurioAlert {

    private static int alertColor = 0xFF0000; // Red
    private static int firstBrokenTick = 0; // only show alert after 20 ticks (1 second) - prevent flashing when traveling from/to vaults where the check logic changes

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {return;}
        Player player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator() || ClientVaults.getActive().isPresent()) {return;}
        if (!(QOLHuntersClientConfigs.BROKEN_CURIO_ALERT.get())) {return;}

        var trinket = brokenTrinket(player);
        var vaultCharm = brokenGodCharm(player);
        var voidStone = brokenVoidStone(player);

        if (!trinket && !vaultCharm && !voidStone) {
            firstBrokenTick = 0; // reset the tick counter if no broken curios
            return;
        }

        if (firstBrokenTick == 0 || Minecraft.getInstance().player.tickCount < firstBrokenTick) {
            firstBrokenTick = Minecraft.getInstance().player.tickCount;
            return;
        }
        if (Minecraft.getInstance().player.tickCount - firstBrokenTick < 20) {
            return; // only show alert after 20 ticks (1 second)
        }

        MutableComponent text = new TextComponent("Broken Curios: ");
        if (trinket) {
            text.append(new TextComponent("Trinket"));
        }
        if (vaultCharm) {
            if (trinket) {
                text.append(new TextComponent(", "));
            }
            text.append(new TextComponent("Charm"));
        }
        if (voidStone) {
            if (trinket || vaultCharm) {
                text.append(new TextComponent(", "));
            }
            text.append(new TextComponent("Void Stone"));
        }

        text.withStyle(ChatFormatting.BOLD);
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

    private static boolean brokenVoidStone(Player player) {
        // simple check for void stone
        if (CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.VOID_STONE.getItem()).isPresent()) {
            return VoidStoneItem.getVoidStone(player).isEmpty(); // get* returns empty if the item is not usable
        }
        return false;
    }

    private static boolean brokenGodCharm(Player player) {
        // simple check for vault charm
        if (CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.VAULT_GOD_CHARM.getItem()).isPresent()) {
            return VaultCharmItem.getCharm(player).isEmpty(); // get* returns empty if the item is not usable
        }
        return false;
    }

    private static boolean brokenTrinket(Player player) {
        return TrinketHelper.getTrinkets(player).stream().anyMatch(trinket -> !trinket.isUsable(player));
    }

}
