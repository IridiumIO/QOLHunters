package io.iridium.qolhunters.mixin.abilities;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import iskallia.vault.client.atlas.TextureAtlasRegion;
import iskallia.vault.client.gui.screen.player.legacy.widget.AbilityWidget;
import iskallia.vault.client.gui.screen.player.legacy.widget.AbilityWidgetSelectable;
import iskallia.vault.client.gui.screen.player.legacy.widget.NodeState;
import iskallia.vault.init.ModKeybinds;
import iskallia.vault.skill.base.Skill;
import iskallia.vault.skill.tree.AbilityTree;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(value = AbilityWidgetSelectable.class, remap = false)
public class MixinAbilityWidgetSelectable extends AbilityWidget {
    public MixinAbilityWidgetSelectable(String abilityName, AbilityTree abilityTree, int x, int y,
                                        Map<NodeState, TextureAtlasRegion> background,
                                        TextureAtlasRegion icon) {
        super(abilityName, abilityTree, x, y, background, icon);
    }

    @Inject(method = "renderHover", at = @At(value = "INVOKE", target = "Liskallia/vault/client/util/TooltipUtil;renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;IIII)V"))
    private void addMulticastAbilitiesToTooltip(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci, @Local(name = "tooltip") List<FormattedCharSequence> tooltip){
        KeyMapping keyMapping = ModKeybinds.abilityQuickfireKey.get(this.makeAbilityNode().getId());
        if (keyMapping.isUnbound()) {
            return;
        }
        List<String> matchingAbilities = new ArrayList<>();
        for(Map.Entry<String, KeyMapping> entry : ModKeybinds.abilityQuickfireKey.entrySet()) {
            KeyMapping other = entry.getValue();
            if (other != keyMapping && other.getKey().equals(keyMapping.getKey()) && other.getKeyModifier() == keyMapping.getKeyModifier()) {
                matchingAbilities.add(entry.getKey());
            }
        }
        if (!matchingAbilities.isEmpty()) {
            tooltip.add(new TextComponent("Also activates: ").withStyle(ChatFormatting.GRAY).getVisualOrderText());
        }
        for (var matching : matchingAbilities) {
            var name = this.abilityTree.getForId(matching).map(Skill::getName).orElse(matching);
            tooltip.add(new TextComponent(name).getVisualOrderText());
        }
    }
}
