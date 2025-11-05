package io.iridium.qolhunters.mixin.abilities;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.InputConstants;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import iskallia.vault.event.InputEvents;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.AbilityQuickselectMessage;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.Set;

@Mixin(value = InputEvents.class, remap = false)
public class MixinInputEvents {

    @WrapOperation(method = "onInput", at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"))
    private static Set<Map.Entry<String, net.minecraft.client.KeyMapping>> abilityMulticast(
        Map<String, KeyMapping> instance,
        Operation<Set<Map.Entry<String, KeyMapping>>> original,
        @Local(argsOnly = true) InputConstants.Key key,
        @Local(argsOnly = true) int action) {

        if (Boolean.FALSE.equals(QOLHuntersClientConfigs.ABILITY_MULTICAST.get())) {
            return original.call(instance);
        }

        // run the logic and return empty set for the iterator
        for(Map.Entry<String, KeyMapping> entry : instance.entrySet()) {
            KeyMapping keyMapping = entry.getValue();
            if (isKeyDown(keyMapping.getKey()) && keyMapping.getKeyModifier().matches(key) || keyMapping.getKey().equals(key)) {
                ModNetwork.CHANNEL.sendToServer(new AbilityQuickselectMessage(entry.getKey(), action));
                // original returns here (first match)
            }
        }
        return Set.of();
    }

    @Shadow
    private static boolean isKeyDown(InputConstants.Key key) {
        throw new AssertionError("isKeyDown was not shadowed");
    }
}
