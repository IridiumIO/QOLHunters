package io.iridium.qolhunters.mixin.skillaltar;

import iskallia.vault.client.atlas.TextureAtlasRegion;
import iskallia.vault.client.gui.framework.ScreenTextures;
import iskallia.vault.client.gui.framework.element.TabElement;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.framework.spatial.Spatials;
import iskallia.vault.client.gui.framework.spatial.spi.IMutableSpatial;
import iskallia.vault.client.gui.screen.block.SkillAltarScreen;
import iskallia.vault.container.SkillAltarContainer;
import iskallia.vault.world.data.SkillAltarData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.atomic.AtomicInteger;

import static iskallia.vault.client.gui.screen.block.SkillAltarScreen.getSkillIcon;

@Mixin(value = SkillAltarScreen.class, remap = false)
public abstract class MixinSkillAltarScreen2<C extends SkillAltarContainer> extends AbstractElementContainerScreen<C> {


    protected MixinSkillAltarScreen2(C container, Inventory inventory, Component title, IElementRenderer elementRenderer, ITooltipRendererFactory<AbstractElementContainerScreen<C>> tooltipRendererFactory) {
        super(container, inventory, title, elementRenderer, tooltipRendererFactory);
    }


    @Redirect(method="<init>", at= @At(value = "INVOKE", target = "iskallia/vault/client/gui/framework/spatial/Spatials.size (II)Liskallia/vault/client/gui/framework/spatial/spi/IMutableSpatial;"))
    private IMutableSpatial getSkillAltarScreenSize(int width, int height) {

        int tabCount = Math.max(5, Math.min(this.getMenu().getSkillIcons().size() + 1, 10) );
        int computedWidth = tabCount * 31 + 21;
        return Spatials.size(computedWidth, 216);
    }



    @Shadow
    protected abstract void addSelectedTab();

    @Shadow
    protected abstract void openTab(int templateIndex);

    @Shadow
    private static TabElement<?> createTab(boolean selected, TextureAtlasRegion icon, int x, Runnable onClick) {
        return null;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void addTabs() {
        for (AtomicInteger i = new AtomicInteger(0); i.get() < Math.min(10, this.getMenu().getSkillIcons().size() + 1); i.incrementAndGet()) {
            int templateIndex = i.get();
            if (templateIndex == this.getMenu().getTemplateIndex()) {
                this.addSelectedTab();
            } else if (templateIndex < this.getMenu().getSkillIcons().size()) {
                SkillAltarData.SkillIcon icon = this.getMenu().getSkillIcons().get(templateIndex);
                this.addElement(createTab(false, getSkillIcon(icon), 4 + templateIndex * 31, () -> this.openTab(templateIndex)));
            } else {
                this.addElement(createTab(false, ScreenTextures.TAB_ICON_ABILITIES, 4 + templateIndex * 31, () -> this.openTab(templateIndex)));
            }
        }
    }

}
