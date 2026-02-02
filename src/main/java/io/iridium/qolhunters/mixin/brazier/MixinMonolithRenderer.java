package io.iridium.qolhunters.mixin.brazier;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.block.entity.MonolithTileEntity;
import iskallia.vault.block.render.MonolithRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Adds dynamic scaling to brazier hologram and configurable hologram content
 *
 * default => always show everything (icon, name, description)
 * mode1 => always show icons, if < 12 blocks away show everything
 * mode2 => always show icons, if SHIFT show everything
 * mode3 => always show icons + name, if SHIFT show everything
 */
@Mixin(value = MonolithRenderer.class, remap = false)
public class MixinMonolithRenderer {

    @Unique private static final ThreadLocal<Double> qolhunters$MONOLITH_DIST = ThreadLocal.withInitial(() -> 0.0);

    @Inject(method = "render(Liskallia/vault/block/entity/MonolithTileEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V", ordinal = 0, remap = true))
    private void modifyScale(MonolithTileEntity tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight,
                             int combinedOverlayIn, CallbackInfo ci, @Local(name = "scale") LocalFloatRef scaleVar) {
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        Vec3 tilePos = new Vec3(tileEntity.getBlockPos().getX() + 0.5, tileEntity.getBlockPos().getY() + 0.5, tileEntity.getBlockPos().getZ() + 0.5);
        double distance = cameraPos.distanceTo(tilePos);
        qolhunters$MONOLITH_DIST.set(distance);

        if (!QOLHuntersClientConfigs.BRAZIER_DYNAMIC_SCALE.get()) {
            return;
        }
        float minDistance = 0.0F;
        float maxDistance = 28.0F;
        float minScale = 0.02F;
        float maxScale = 0.07F;

        float scale = minScale + (maxScale - minScale) * ((float) distance - minDistance) / (maxDistance - minDistance);
        scale = Math.max(minScale, Math.min(scale, maxScale));
        scaleVar.set(scale);
    }


    @Inject(method = "render(Liskallia/vault/block/entity/MonolithTileEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "NEW", target = "(Lcom/mojang/blaze3d/vertex/PoseStack;FLnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/gui/Font;)Liskallia/vault/task/renderer/context/RendererContext;"))
    private void move(MonolithTileEntity tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight,
                      int combinedOverlayIn, CallbackInfo ci, @Local(name = "scale") float scale) {
        if (!QOLHuntersClientConfigs.BRAZIER_DYNAMIC_SCALE.get()) {
            return;
        }
        matrixStack.translate(0.0F, Math.min(0, -(1.5 * 550 * scale) + 11), 0.0F);
    }

    @WrapOperation(method = "lambda$render$0", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private static <E> boolean modifyNameLines(List<Component> instance, E e, Operation<Boolean> original) {
        double distance = qolhunters$MONOLITH_DIST.get();
        switch (QOLHuntersClientConfigs.BRAZIER_HOLOGRAM_MODIFIER_NAME.get()) {
            case NEAR -> {
                if (distance > QOLHuntersClientConfigs.BRAZIER_HOLOGRAM_MODIFIER_NEAR_DISTANCE.get()) {
                    return false; // no name if far
                }
            }
            case SHIFT -> {
                if (!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT)) {
                    return false; // no name if not shift
                }
            }
            case SHIFT_OR_NEAR -> {
                if (distance > QOLHuntersClientConfigs.BRAZIER_HOLOGRAM_MODIFIER_NEAR_DISTANCE.get()
                    && !InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT)) {
                    return false; // far and not shift
                }
            }
            default -> {/* no change */}
        }
        // name otherwise
        return original.call(instance, e);
    }

    @WrapOperation(method = "lambda$render$0", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    private static <E> boolean modifyDescriptionLines(List<Component> instance, E e, Operation<Boolean> original) {
        double distance = qolhunters$MONOLITH_DIST.get();
        switch (QOLHuntersClientConfigs.BRAZIER_HOLOGRAM_MODIFIER_DESCRIPTION.get()) {
            case NEAR -> {
                if (distance > QOLHuntersClientConfigs.BRAZIER_HOLOGRAM_MODIFIER_NEAR_DISTANCE.get()) {
                    return false; // no description if far
                }
            }
            case SHIFT -> {
                if (!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT)) {
                    return false; // no description if not shift
                }

            }
            case SHIFT_OR_NEAR -> {
                if (distance > QOLHuntersClientConfigs.BRAZIER_HOLOGRAM_MODIFIER_NEAR_DISTANCE.get()
                    && !InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT)) {
                    return false; // far and not shift
                }
            }
            default -> {/* no change */}
        }
        // name otherwise
        return original.call(instance, e);
    }
}
