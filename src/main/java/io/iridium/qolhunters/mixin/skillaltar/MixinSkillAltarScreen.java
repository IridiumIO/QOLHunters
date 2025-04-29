package io.iridium.qolhunters.mixin.skillaltar;

import iskallia.vault.client.gui.framework.element.ButtonElement;
import iskallia.vault.client.gui.framework.spatial.Spatials;
import iskallia.vault.client.gui.framework.spatial.spi.IMutableSpatial;
import iskallia.vault.client.gui.screen.block.SkillAltarScreen;
import iskallia.vault.container.SkillAltarContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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


    @Redirect(method="createSkillView", remap=false, at=@At(value = "INVOKE", target = "Liskallia/vault/client/gui/framework/spatial/spi/IMutableSpatial;size(II)Liskallia/vault/client/gui/framework/spatial/spi/IMutableSpatial;"))
    private IMutableSpatial getSkillViewSize(IMutableSpatial instance, int i, int i1) {
        int tabCount = Math.max(5, Math.min(this.getMenu().getSkillIcons().size() + 1, 10) );
        int computedWidth = tabCount * 31 + 21;
        return Spatials.positionXY(7, 16).size(computedWidth, 81);
    }



}
