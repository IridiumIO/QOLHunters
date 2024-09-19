package io.iridium.qolhunters.mixin.skillaltar;

import iskallia.vault.client.gui.framework.element.ButtonElement;
import iskallia.vault.client.gui.screen.block.SkillAltarScreen;
import iskallia.vault.container.SkillAltarContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SkillAltarScreen.Default.class)
public abstract class MixinSkillAltarScreen extends SkillAltarScreen<SkillAltarContainer.Default>{


    @Shadow(remap = false) @Final private ButtonElement<?> saveButton;

    protected MixinSkillAltarScreen(SkillAltarContainer.Default container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }


    @Inject(method="<init>", at=@At("RETURN"))
    public void init(SkillAltarContainer.Default container, Inventory inventory, Component title, CallbackInfo ci) {
        this.saveButton.setDisabled(() -> {
            return this.playerHasNoAbilitiesAndTalents() || !((SkillAltarContainer.Default)this.getMenu()).isEmptyTemplate() && this.isSaveLocked || ((SkillAltarContainer.Default)this.getMenu()).isOpenedByNonOwner();
        });

    }

    @Shadow(remap = false)
    protected abstract boolean playerHasNoAbilitiesAndTalents();

    @Shadow(remap = false)
    private boolean isSaveLocked;


}
