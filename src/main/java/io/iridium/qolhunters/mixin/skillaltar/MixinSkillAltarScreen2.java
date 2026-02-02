package io.iridium.qolhunters.mixin.skillaltar;

import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.render.spi.ITooltipRendererFactory;
import iskallia.vault.client.gui.framework.screen.AbstractElementContainerScreen;
import iskallia.vault.client.gui.framework.spatial.Spatials;
import iskallia.vault.client.gui.framework.spatial.spi.IMutableSpatial;
import iskallia.vault.client.gui.screen.block.SkillAltarScreen;
import iskallia.vault.container.SkillAltarContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

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

    @ModifyArg(method = "addTabs", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"), index = 0)
    private int increaseMaxTabCount(int minSize){
        return 10;
    }

}
