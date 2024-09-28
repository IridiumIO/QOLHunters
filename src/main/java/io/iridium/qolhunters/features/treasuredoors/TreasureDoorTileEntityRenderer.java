package io.iridium.qolhunters.features.treasuredoors;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.block.TreasureDoorBlock;
import iskallia.vault.block.entity.TreasureDoorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class TreasureDoorTileEntityRenderer implements BlockEntityRenderer<TreasureDoorTileEntity> {

    public TreasureDoorTileEntityRenderer(BlockEntityRendererProvider.Context context) {
        // Initialization code if needed
    }

    private String playerName = null;

    @Override
    public void render(TreasureDoorTileEntity tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        if(!QOLHuntersClientConfigs.TREASURE_DOOR_NAMES.get()) return;
        if(tileEntity.getBlockState().getValue(TreasureDoorBlock.HALF) == DoubleBlockHalf.LOWER) return;

        poseStack.pushPose();

        Direction facing = tileEntity.getBlockState().getValue(TreasureDoorBlock.FACING);
        switch (facing) {
            case NORTH:
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
                poseStack.translate(-1,0,-1);
                break;
            case SOUTH:
                // No rotation needed
                break;
            case WEST:
                poseStack.mulPose(Vector3f.YP.rotationDegrees(-90));
                poseStack.translate(0,0,-1);
                break;
            case EAST:
                poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
                poseStack.translate(-1,0,0);
                break;
        }

        poseStack.translate(0.5,0.75,0.3);

        float scale = 0.014f;
        poseStack.scale(scale, -scale, scale);

        String text = tileEntity.getBlockState().getValue(TreasureDoorBlock.TYPE).toString();

        //SMH devs can't spell
        text = text.equals("PETZANITE") ? "PETEZANITE" : text;

        if(playerName == null) playerName = Minecraft.getInstance().player.getName().getString();

        text = text.equals("ISKALLIUM") && playerName.equals("HrryBrry") ? "GLORPIUM" : text;

        int xOffset = Minecraft.getInstance().font.width(text);
        Minecraft.getInstance().font.drawInBatch(text, (float) (-xOffset)/2.0F, 0, 0xFFFFFF, true, poseStack.last().pose(), bufferSource, false, 0, combinedLight);

        poseStack.popPose();
    }
}