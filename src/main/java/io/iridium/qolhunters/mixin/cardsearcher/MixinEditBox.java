package io.iridium.qolhunters.mixin.cardsearcher;

import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(EditBox.class)
public class MixinEditBox {

    /** {@link iskallia.vault.gear.tooltip.ModifierCategoryTooltip} */
    @Unique
    private static final Map<String, Character> qolhunters$REPL = Map.of(
            "\\leg", '✦',
            "\\frozen", '❰',
            "\\sealed", '【',
            "\\abyss", 'ᚼ',
            "\\crafted", '⛏',
            "\\enhanced", '⏶',
            "\\greater", '⧫',
            "\\imbued", '✤',
            "\\unusual", 'ᛳ'
    );

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void onCharTyped(char codePoint, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        EditBox editBox = (EditBox) (Object) this;

        String currentValue = editBox.getValue();

        // Iterate through the replacements and check if the current value contains any sequence
        for (Map.Entry<String, Character> entry : qolhunters$REPL.entrySet()) {
            String sequenceToReplace = entry.getKey();
            char replacementChar = entry.getValue();

            if (currentValue.contains(sequenceToReplace)) {
                // Replace the sequence with the replacement character
                String newValue = currentValue.replace(sequenceToReplace, String.valueOf(replacementChar));
                editBox.setValue(newValue);

                // Cancel the original method call
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
