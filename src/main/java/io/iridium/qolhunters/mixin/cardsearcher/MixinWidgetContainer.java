package io.iridium.qolhunters.mixin.cardsearcher;

import appeng.client.gui.WidgetContainer;
import appeng.client.gui.widgets.AETextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WidgetContainer.class)
public class MixinWidgetContainer {


    @Redirect(method = "addTextField", at = @At(value = "INVOKE", target = "Lappeng/client/gui/widgets/AETextField;setMaxLength(I)V"))
    private void redirectSetMaxLength(AETextField instance, int i) {
        instance.setMaxLength(75);
    }

}
