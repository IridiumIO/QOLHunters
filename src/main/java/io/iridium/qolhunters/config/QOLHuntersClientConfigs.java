package io.iridium.qolhunters.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class QOLHuntersClientConfigs {

    public static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> VANILLA_SAFE_MODE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_DESCRIPTIONS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VAULT_MODIFIER_TEXT_OVERLAYS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VAULT_ENCHANTER_EMERALDS_SLOT;

    static {

        CLIENT_BUILDER.comment("QOLHunters Configuration\nALL CHANGES REQUIRE A CLIENT RESTART");

        CLIENT_BUILDER.push("General Configs");

        VANILLA_SAFE_MODE = CLIENT_BUILDER.comment("Disables QOL mods that cannot work on vanilla VH servers (disables all mods in the 'Client-Server Extensions' list)\nBe very careful if you change this on single player worlds! For example, if you have emeralds in your enchantment table and you enable this, those will probably get deleted if you load the world before changing this back").define("Vanilla Safe Mode", false);

        CLIENT_BUILDER.pop();




        CLIENT_BUILDER.push("Client-Only Extensions");

        BETTER_DESCRIPTIONS = CLIENT_BUILDER.comment("Improves the descriptions of abilities, talents, expertises and researches").define("Better Descriptions", true);
        CLIENT_BUILDER.comment("");
        VAULT_MODIFIER_TEXT_OVERLAYS = CLIENT_BUILDER.comment("Adds text overlays to the Vault modifiers, e.g. '+10% Damage' or 'Speed +1'").define("Vault Modifier Text Overlays", true);

        CLIENT_BUILDER.pop();




        CLIENT_BUILDER.push("Client-Server Extensions");

        VAULT_ENCHANTER_EMERALDS_SLOT = CLIENT_BUILDER.comment("Adds an emerald slot to the Vault Enchanter").define("Vault Enchanter Emeralds Slot", true);

        CLIENT_SPEC = CLIENT_BUILDER.build();
    }


}
