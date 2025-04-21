package io.iridium.qolhunters.mixin.transmogtable;

import com.mojang.blaze3d.vertex.PoseStack;
import iskallia.vault.client.gui.framework.element.DiscoveredModelSelectElement;
import iskallia.vault.client.gui.framework.element.ScrollableItemStackSelectorElement;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.spatial.spi.ISpatial;
import mezz.jei.Internal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DiscoveredModelSelectElement.class)
public abstract class MixinDiscoveredModelSelectElement<E extends MixinDiscoveredModelSelectElement<E>> extends ScrollableItemStackSelectorElement<MixinDiscoveredModelSelectElement<E>, ScrollableItemStackSelectorElement.ItemSelectorEntry> {
    @Unique private String qOLHunters$filter;

    private MixinDiscoveredModelSelectElement(ISpatial spatial, int slotColumns, SelectorModel<ItemSelectorEntry> selectorModel) {
        super(spatial, slotColumns, selectorModel);
    }

    @Inject(at = @At("TAIL"), method = "render", remap = false)
    public void render(IElementRenderer renderer, PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (Internal.getRuntime() == null) return;
        String newFilter = Internal.getRuntime().getIngredientFilter().getFilterText().toLowerCase();
        if (!newFilter.equals(qOLHunters$filter)) {
            qOLHunters$filter = newFilter;
            refreshElements();
        }
    }
}
