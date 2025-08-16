package io.iridium.qolhunters.features.betterabilitiestab;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.ability.component.AbilityLabelBindingRegistry;
import iskallia.vault.skill.ability.component.AbilityLabelFactory;
import iskallia.vault.skill.base.Skill;
import iskallia.vault.util.calc.CooldownHelper;
import iskallia.vault.util.calc.EffectDurationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Field;
import java.util.Map;

public interface IBetterAbilities {


    default void HijackAbilityLabelFactory() throws NoSuchFieldException, IllegalAccessException {
        Field FACTORY_MAP_FIELD = AbilityLabelFactory.class.getDeclaredField("FACTORY_MAP");
        FACTORY_MAP_FIELD.setAccessible(true);

        // Retrieve the value of the field
        Map<String, AbilityLabelFactory.IAbilityComponentFactory> factoryMap =
                (Map<String, AbilityLabelFactory.IAbilityComponentFactory>) FACTORY_MAP_FIELD.get(null);


        Player player = Minecraft.getInstance().player;

        factoryMap.put("cooldown",
                context -> labelWithCooldownValue(
                        "\n Cooldown: ",
                        binding(context.config(), "cooldown"),
                        "cooldown",
                        player != null ? CooldownHelper.getCooldownMultiplier(player) : 0.0F
                ));

        factoryMap.put("duration",
            context -> labelWithDurationValue(
                "\n Duration: ",
                binding(context.config(), "duration"),
                "duration",
                player
            ));


    }

    private static <C extends Skill> String binding(C config, String key) {
        return AbilityLabelBindingRegistry.getBindingValue(config, key);
    }

    private static MutableComponent labelWithCooldownValue(String label, String value, String colorKey, float abilityValue) {
        float result = Float.parseFloat(value.replace("s", ""));
        return new TextComponent(label)
                .withStyle(Style.EMPTY.withColor(ModConfigs.COLORS.getColor("text")))
                .append(stylizeText(value, colorKey).append(stylizeText(" (" + String.format("%.1f", result * (1.0 - abilityValue)) + "s)", colorKey)));
    }

    private static MutableComponent labelWithDurationValue(String label, String value, String colorKey, Player player) {
        float durationSeconds = Float.parseFloat(value.replace("s", ""));
        int modifiedDurationTicks = EffectDurationHelper.adjustEffectDurationFloor(player, durationSeconds * 20);
        float modifiedDurationSeconds = modifiedDurationTicks/20f;

        return new TextComponent(label)
            .withStyle(Style.EMPTY.withColor(ModConfigs.COLORS.getColor("text")))
            .append(stylizeText(value, colorKey).append(stylizeText(" (" + String.format("%.1f", modifiedDurationSeconds) + "s)", colorKey)));
    }

    private static MutableComponent stylizeText(String text, String colorKey) {
        return stylizeText(text, ModConfigs.COLORS.getColor(colorKey));
    }

    private static MutableComponent stylizeText(String text, TextColor color) {
        return new TextComponent(text).withStyle(Style.EMPTY.withColor(color));
    }

}
