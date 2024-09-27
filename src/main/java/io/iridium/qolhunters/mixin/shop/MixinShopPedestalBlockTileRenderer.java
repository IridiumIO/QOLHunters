package io.iridium.qolhunters.mixin.shop;

import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.block.render.ShopPedestalBlockTileRenderer;
import iskallia.vault.client.ClientExpertiseData;
import iskallia.vault.skill.base.TieredSkill;
import iskallia.vault.skill.expertise.type.BarteringExpertise;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

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
