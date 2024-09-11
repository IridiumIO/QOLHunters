package io.iridium.qolhunters.mixin.cake;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.iridium.qolhunters.interfaces.SuperCakeObjective;
import iskallia.vault.core.vault.objective.CakeObjective;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.*;

import static io.iridium.qolhunters.interfaces.SuperCakeObjective.qOLHunters$colorMap;

@Mixin(CakeObjective.class)
public abstract class MixinCakeObjective {

    @Shadow(remap = false) @Final
    protected static ResourceLocation VIGNETTE;

    /**
     * @author Iridium
     * @reason I have no idea why this needs to be an overwrite. I tried to make it an inject but it didn't work.
     *
     */
    @OnlyIn(Dist.CLIENT)
    @Overwrite(remap = false)
    protected void renderVignette(TextColor color, float alpha, int width, int height) {
        color = TextColor.fromRgb(qOLHunters$colorMap.get(SuperCakeObjective.qol$colorIndex));
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        int colorValue = color.getValue();
        float b = (float)(colorValue & 255) / 255.0F;
        float g = (float)(colorValue >> 8 & 255) / 255.0F;
        float r = (float)(colorValue >> 16 & 255) / 255.0F;
        RenderSystem.setShaderColor(r, g, b, alpha);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VIGNETTE);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(0.0, (double)height, -90.0).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex((double)width, (double)height, -90.0).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex((double)width, 0.0, -90.0).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0, 0.0, -90.0).uv(0.0F, 0.0F).endVertex();
        tesselator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }

}
