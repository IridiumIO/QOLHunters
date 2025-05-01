package io.iridium.qolhunters.mixin.ascenscionforge;

import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.QOLJEIPlugin;
import iskallia.vault.client.gui.framework.element.AscensionForgeSelectElement;
import iskallia.vault.client.gui.framework.element.ScrollableItemStackSelectorElement;
import iskallia.vault.client.gui.framework.render.spi.IElementRenderer;
import iskallia.vault.client.gui.framework.spatial.spi.ISpatial;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AscensionForgeSelectElement.class)
public abstract class MixinAscensionForgeSelectElement<E extends MixinAscensionForgeSelectElement<E>> extends ScrollableItemStackSelectorElement<MixinAscensionForgeSelectElement<E>, ScrollableItemStackSelectorElement.ItemSelectorEntry> {
    @Unique private String qOLHunters$filter;

    private MixinAscensionForgeSelectElement(ISpatial spatial, int slotColumns, SelectorModel<ItemSelectorEntry> selectorModel) {
        super(spatial, slotColumns, selectorModel);
    }

    @Inject(at = @At("TAIL"), method = "render", remap = false)
    public void render(IElementRenderer renderer, PoseStack poseStack, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (QOLJEIPlugin.getRuntime() == null) return;
        String newFilter = QOLJEIPlugin.getRuntime().getIngredientFilter().getFilterText().toLowerCase();
        if (!newFilter.equals(qOLHunters$filter)) {
            qOLHunters$filter = newFilter;
            refreshElements();
        }
        if (!newFilter.isEmpty()) {
            int width = Minecraft.getInstance().font.width(newFilter);
            Minecraft.getInstance().font.draw(poseStack, newFilter, this.right() - width, this.top() - Minecraft.getInstance().font.lineHeight - 1, 0x00AA00);
        }
    }
}
