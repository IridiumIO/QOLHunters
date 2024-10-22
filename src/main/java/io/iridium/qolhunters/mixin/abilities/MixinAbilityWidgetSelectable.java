package io.iridium.qolhunters.mixin.abilities;

import com.mojang.blaze3d.vertex.PoseStack;
import io.iridium.qolhunters.features.abilitykeybinds.AbilityTabKeyBinding;
import iskallia.vault.client.atlas.TextureAtlasRegion;
import iskallia.vault.client.gui.screen.player.legacy.widget.AbilityWidget;
import iskallia.vault.client.gui.screen.player.legacy.widget.AbilityWidgetSelectable;
import iskallia.vault.client.gui.screen.player.legacy.widget.NodeState;
import iskallia.vault.init.ModKeybinds;
import iskallia.vault.skill.tree.AbilityTree;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Mixin(value = AbilityWidgetSelectable.class, remap = false)
public abstract class MixinAbilityWidgetSelectable extends AbilityWidget{

    protected MixinAbilityWidgetSelectable(String abilityName, AbilityTree abilityTree, int x, int y, Map<NodeState, TextureAtlasRegion> background, TextureAtlasRegion icon) {
        super(abilityName, abilityTree, x, y, background, icon);
    }


    @Shadow
    public abstract Rectangle getClickableBounds();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Inject(method="renderHover", at=@At("RETURN"), cancellable = true)
    public void renderHover(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {

        if (!this.getClickableBounds().contains(mouseX, mouseY)) return;

        AbilityTabKeyBinding.isAbilityHovered = true;
        AbilityTabKeyBinding.node = this.makeAbilityNode();
        AbilityTabKeyBinding.keyMapping = ModKeybinds.abilityQuickfireKey.get(AbilityTabKeyBinding.node.getId());

        AbilityTabKeyBinding.refresh();

    }

}
