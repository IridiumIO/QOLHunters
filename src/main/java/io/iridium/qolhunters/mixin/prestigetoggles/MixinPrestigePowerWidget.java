package io.iridium.qolhunters.mixin.prestigetoggles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.client.data.ClientPrestigePowersData;
import iskallia.vault.client.atlas.ITextureAtlas;
import iskallia.vault.client.atlas.TextureAtlasRegion;
import iskallia.vault.client.gui.framework.ScreenTextures;
import iskallia.vault.client.gui.screen.player.legacy.widget.PrestigePowerWidget;
import iskallia.vault.client.gui.screen.player.legacy.widget.SkillWidget;
import iskallia.vault.config.entry.SkillStyle;
import iskallia.vault.network.message.ServerboundPrestigePowerToggleMessage;
import iskallia.vault.skill.base.TieredSkill;
import iskallia.vault.skill.prestige.core.ActivatePrestigePower;
import iskallia.vault.skill.tree.PrestigeTree;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PrestigePowerWidget.class, remap = false)
public abstract class MixinPrestigePowerWidget extends SkillWidget<PrestigeTree> {

    public MixinPrestigePowerWidget(PrestigeTree skillTree, Component pMessage, TieredSkill skill, SkillStyle style) {
        super(skillTree, pMessage, skill, style);
    }

    @Inject(method = "renderIcon", at = @At("TAIL"))
    private void addToggleElement(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci){
        if (QOLHuntersClientConfigs.PRESTIGE_TOGGLES.get() && this.getSkill().getChild() instanceof ActivatePrestigePower) {
            var node = ClientPrestigePowersData.getLearnedPrestigeNode(this.getSkill().getId());
            if (node == null) {
                return;
            }
            TextureAtlasRegion texture;
            if (node.getChild() instanceof ActivatePrestigePower app) {
                if (app.isActive()) {
                    texture = ScreenTextures.BUTTON_TOGGLE_ON;
                } else {
                    texture = ScreenTextures.BUTTON_TOGGLE_OFF;
                }
                RenderSystem.setShaderTexture(0, texture.atlas().get().getAtlasResourceLocation());
                ITextureAtlas buttonAtlas = texture.atlas().get();
                GuiComponent.blit(matrixStack, this.x - texture.width() * 2 + 4 , this.y - texture.height() / 2 + 4, 0, texture.width() - 4, texture.height() - 8, buttonAtlas.getSprite(texture.resourceLocation()));
            }
        }
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (QOLHuntersClientConfigs.PRESTIGE_TOGGLES.get() && button == 0 && this.getSkill().getChild() instanceof ActivatePrestigePower) {
            if (Screen.hasControlDown() || qOLHunters$aboveToggle(mouseX, mouseY)){
                var node = ClientPrestigePowersData.getLearnedPrestigeNode(this.getSkill().getId());
                if (node != null) {
                    if (node.getChild() instanceof ActivatePrestigePower prestigePower) {
                        ServerboundPrestigePowerToggleMessage.send(this.getSkill().getId(), !prestigePower.isActive());
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Unique private boolean qOLHunters$aboveToggle(double mouseX, double mouseY) {
        int leftX = this.x - 9 * 2 + 4;
        int topY = this.y - 18 / 2 + 4;
        int rightX = leftX + 5;
        int bottomY = topY + 10;
        if (mouseX >= leftX && mouseX <= rightX && mouseY >= topY && mouseY < bottomY) {
            // idk why this is needed but it is
            if (mouseX == leftX) {
                return mouseX > 0;
            }
            if (mouseX == rightX) {
                return mouseX < 0;
            }
            return true;
        }
        return false;
    }

}
