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

import java.util.Iterator;

@Mixin(value = GodAltarOverlay.class, remap = false)
public class MixinGodAltarOverlay {


//    @ModifyArgs(method="render", at=@At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V"))
//    private void qol$modifyTranslateArgs(Args args) {
//        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
//        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
//        double posX = QOLHuntersClientConfigs.GOD_OBJECTIVE_X_OFFSET.get() * screenWidth/100.0;
//        double posY = QOLHuntersClientConfigs.GOD_OBJECTIVE_Y_OFFSET.get() * screenHeight/100.0;
//
//        args.set(0, posX - 4.0);
//        args.set(1, posY);
//        args.set(2, 0.0);
//
//    }
//

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
            Iterator var6 = GodAltarData.CLIENT.iterator();

            while(var6.hasNext()) {
                GodAltarData.Entry entry = (GodAltarData.Entry)var6.next();
                GodAltarRendererContext context = GodAltarRendererContext.forHud(matrixStack, partialTick, gui.getFont());
                entry.getTask().onRender(context);
            }

            matrixStack.popPose();
        }
    }

}
