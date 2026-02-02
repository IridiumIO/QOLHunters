package io.iridium.qolhunters.mixin.godaltars;

import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.gui.overlay.GodAltarOverlay;
import iskallia.vault.init.ModKeybinds;
import iskallia.vault.task.renderer.context.GodAltarRendererContext;
import iskallia.vault.world.data.GodAltarData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = GodAltarOverlay.class, remap = false)
public class MixinGodAltarOverlay {


    /**
     * @author
     * @reason
     */
    @Overwrite
    public void render(ForgeIngameGui gui, PoseStack matrixStack, float partialTick, int width, int height) {
        if (!ModKeybinds.bountyStatusKey.isDown()) {
            matrixStack.pushPose();
            int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            double posX = QOLHuntersClientConfigs.GOD_OBJECTIVE_X_OFFSET.get() * (screenWidth/100.0);
            double posY = QOLHuntersClientConfigs.GOD_OBJECTIVE_Y_OFFSET.get() * (screenHeight/100.0);

            matrixStack.translate( posX, posY, 0.0);
            matrixStack.scale(0.8F, 0.8F, 0.8F);

            for (GodAltarData.Entry entry : GodAltarData.CLIENT) {
                GodAltarRendererContext context = GodAltarRendererContext.forHud(matrixStack, partialTick, gui.getFont());
                entry.getTask().onRender(context);
            }

            matrixStack.popPose();
        }
    }

}
