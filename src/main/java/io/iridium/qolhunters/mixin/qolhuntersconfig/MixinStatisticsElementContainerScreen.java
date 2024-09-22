package io.iridium.qolhunters.mixin.qolhuntersconfig;

import com.simibubi.create.foundation.config.ui.ConfigHelper;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import iskallia.vault.client.gui.framework.ScreenTextures;
import iskallia.vault.client.gui.framework.element.ButtonElement;
import iskallia.vault.client.gui.framework.render.TooltipDirection;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.spatial.Spatials;
import iskallia.vault.client.gui.screen.player.AbstractSkillTabElementContainerScreen;
import iskallia.vault.client.gui.screen.player.StatisticsElementContainerScreen;
import iskallia.vault.container.StatisticsTabContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(StatisticsElementContainerScreen.class)
public abstract class MixinStatisticsElementContainerScreen extends AbstractSkillTabElementContainerScreen<StatisticsTabContainer>{


    protected MixinStatisticsElementContainerScreen(StatisticsTabContainer container, Inventory inventory, Component title, IElementRenderer elementRenderer) {
        super(container, inventory, title, elementRenderer);
    }

    @Inject(method="<init>", at=@At("RETURN"))
    public void init(CallbackInfo ci) {

        this.addElement((ButtonElement)((ButtonElement)(new ButtonElement(Spatials.positionXY(-3, 3), ScreenTextures.BUTTON_RELIC_TEXTURES, () -> {
            SubMenuConfigScreen screen = SubMenuConfigScreen.find(ConfigHelper.ConfigPath.parse("qolhunters:client.Client-Only Extensions"));
            Minecraft.getInstance().setScreen(screen);

        })).layout((screen, gui, parent, world) -> {
            world.width(21).height(21).translateX(gui.right() + 5).translateY(this.getTabContentSpatial().bottom() + 109);
        })).tooltip((tooltipRenderer, poseStack, mouseX, mouseY, tooltipFlag) -> {
            tooltipRenderer.renderTooltip(poseStack, List.of(new TextComponent("QOLHunters Config")), mouseX, mouseY, ItemStack.EMPTY, TooltipDirection.RIGHT);
            return false;
        }));

    }

    @Shadow(remap = false)
    public abstract int getTabIndex();


    @Shadow(remap = false)
    public abstract MutableComponent getTabTitle() ;
}
