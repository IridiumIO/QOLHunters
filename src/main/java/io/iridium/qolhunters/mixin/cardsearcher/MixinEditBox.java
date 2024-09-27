package io.iridium.qolhunters.mixin.cardsearcher;

import io.iridium.qolhunters.util.SharedFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(EditBox.class)
public class MixinEditBox {

    @Unique
    private static final Map<String, Character> REPL = Map.of(
            "\\leg", '✦',
            "\\frozen", '❰'
    );


    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void onCharTyped(char codePoint, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        EditBox editBox = (EditBox) (Object) this;

        String currentValue = editBox.getValue();

        if(currentValue.contains("\\obs")){
            Minecraft.getInstance().setScreen(null);
            SharedFunctions.displayTitleOnScreen(new TextComponent("OBS Mode Enabled"));
            return;
        }

        // Iterate through the replacements and check if the current value contains any sequence
        for (Map.Entry<String, Character> entry : REPL.entrySet()) {
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


        // pattern to match the bracketed list for card searching in AE2 \#(tag1, tag2, tag...)
        Pattern pattern = Pattern.compile("#\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(currentValue);

        if (matcher.find()) {
            // Extract the content within the brackets
            String content = matcher.group(1);

            // Split the content by commas to get individual strings
            String[] parts = content.split(",\\s*");

            // Construct the regex pattern
            StringBuilder regexBuilder = new StringBuilder("(?si)");
            for (String part : parts) {
                regexBuilder.append("(?=.*").append(part).append(")");
            }
            String regex = regexBuilder.toString();

            // Replace the original sequence with the constructed regex
            String newValue = currentValue.replace(matcher.group(0), regex);
            editBox.setValue(newValue);

            // Cancel the original method call
            cir.setReturnValue(true);
        }
    }

}
