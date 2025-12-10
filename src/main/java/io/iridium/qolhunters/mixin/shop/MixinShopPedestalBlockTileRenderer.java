package io.iridium.qolhunters.mixin.shop;

import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.features.shopbarteringdiscountdisplay.Shopping;
import io.iridium.qolhunters.util.SharedFunctions;
import iskallia.vault.block.render.ShopPedestalBlockTileRenderer;
import iskallia.vault.client.data.ClientExpertiseData;
import iskallia.vault.skill.base.TieredSkill;
import iskallia.vault.skill.expertise.type.BarteringExpertise;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(value = ShopPedestalBlockTileRenderer.class, remap = false)
public class MixinShopPedestalBlockTileRenderer {


    @ModifyArg(method="render(Liskallia/vault/block/entity/ShopPedestalBlockTile;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
        at=@At(value="INVOKE", target="Liskallia/vault/block/render/ShopPedestalBlockTileRenderer;drawPrice(Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Ljava/lang/String;II)V"),
        index=3)
    private String modifyPrice(String name) {

        if (!QOLHuntersClientConfigs.ENABLE_BARTERING_DISCOUNT_DISPLAY.get()) return name;
        Player player = Minecraft.getInstance().player;
        if (player == null || !player.level.dimension().location().toString().startsWith("the_vault:vault")) return name;

        float barteringDiscount = qOLHunters$getDiscount();
        if (barteringDiscount == 0) return name;
        return name + " → " + (int)(Integer.parseInt(name) * (1-barteringDiscount)) ;


    }


    @Inject(method="drawPrice" ,
            at= @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderStatic(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;IILcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.AFTER),
            remap = true,
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void drawPrice(ItemStack stack, PoseStack matrixStack, MultiBufferSource buffer, String name, int combinedLight, int combinedOverlay, CallbackInfo ci, FormattedCharSequence text, Font fr, int xOffset, Iterator var10, Direction dir) {

        if(Shopping.isLookingAtShopPedestal && Shopping.invGoldCount > 0) {

            String formattedCurrency = SharedFunctions.formatNumber(Shopping.invGoldCount);
            matrixStack.pushPose();
            matrixStack.translate(0.0, 0, 0.075);
            float scale = 0.015F;
            matrixStack.scale(scale, -scale, scale);
            fr.drawInBatch(formattedCurrency, 0, 1.0F - 9.0F / 2.0F, -16777216, false, matrixStack.last().pose(), buffer, false, 0, combinedLight);
            matrixStack.translate(0.0, 0.0, 0.001);
            fr.drawInBatch(formattedCurrency, 0, (float)(-9) / 2.0F, -1, false, matrixStack.last().pose(), buffer, false, 0, combinedLight);
            matrixStack.popPose();


        }

    }


    @Unique
    private static long qOLHunters$lastChecked = 0;
    @Unique
    private static float qOLHunters$lastDiscount = 0;
    @Unique
    private static float qOLHunters$getDiscount() {
        long currentTime = System.currentTimeMillis();
        if (currentTime < qOLHunters$lastChecked + 5000) return qOLHunters$lastDiscount;

        TieredSkill bartering = ClientExpertiseData.getLearnedTalentNode("Bartering");
        if (bartering == null) {
            qOLHunters$lastDiscount = 0;
        } else {
            BarteringExpertise expertise = (BarteringExpertise) bartering.getChild(bartering.getActualTier());
            qOLHunters$lastDiscount = expertise.getCostReduction();
        }
        qOLHunters$lastChecked = currentTime;
        return qOLHunters$lastDiscount;
    }
}
