package io.iridium.qolhunters.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class QOLHuntersClientConfigs {

    public static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> VANILLA_SAFE_MODE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_DESCRIPTIONS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VAULT_MODIFIER_TEXT_OVERLAYS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VAULT_ENCHANTER_EMERALDS_SLOT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VAULT_INTERFACE_KEYBINDS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_ABILITIES_TAB;
    public static final ForgeConfigSpec.ConfigValue<Integer> CAKE_VAULT_OVERLAY_COLOR;
    public static final ForgeConfigSpec.ConfigValue<Integer> CAKE_VAULT_OVERLAY_STYLE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BLACK_MARKET_SHARD_POUCH_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BLACK_MARKET_SHARD_POUCH_COUNT_SHORTHAND;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SCAVENGER_INV_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SCAVENGER_HIGHLIGHTER;

    static {

        CLIENT_BUILDER.comment("QOLHunters Configuration\nALL CHANGES REQUIRE A CLIENT RESTART");

        CLIENT_BUILDER.push("General Configs");

        VANILLA_SAFE_MODE = CLIENT_BUILDER.comment("Disables QOL mods that cannot work on vanilla VH servers (disables all mods in the 'Client-Server Extensions' list)\nBe very careful if you change this on single player worlds! For example, if you have emeralds in your enchantment table and you enable this, those will probably get deleted if you load the world before changing this back").define("Vanilla Safe Mode", false);

        CLIENT_BUILDER.pop();




        CLIENT_BUILDER.push("Client-Only Extensions");

        BETTER_DESCRIPTIONS = CLIENT_BUILDER.comment("Improves the descriptions of abilities, talents, expertises and researches").define("Better Descriptions", true);
        VAULT_MODIFIER_TEXT_OVERLAYS = CLIENT_BUILDER.comment("Adds text overlays to the Vault modifiers, e.g. '+10% Damage' or 'Speed +1'").define("Vault Modifier Text Overlays", true);
        VAULT_INTERFACE_KEYBINDS = CLIENT_BUILDER.comment("Adds keybinds to craft/forge/reroll in the Bounty Table, Enchanter, Vault Forge, etc").define("Vault Interface Keybinds", true);
        BETTER_ABILITIES_TAB = CLIENT_BUILDER.comment("Improves the Abilities Tab including levelling specializations directly and showing all possible levels/overlevels").define("Better Abilities Tab", true);
        CAKE_VAULT_OVERLAY_COLOR = CLIENT_BUILDER.comment("Changes the color of the Vault Cake overlay").defineInRange("Cake Vault Overlay Color", 0, 0, 3);
        CAKE_VAULT_OVERLAY_STYLE = CLIENT_BUILDER.comment("Changes the style of the Vault Cake overlay\n0=Vignette, 1=Cake HUD Icons").defineInRange("Cake Vault Overlay Style", 0, 0, 1);
        BLACK_MARKET_SHARD_POUCH_COUNT = CLIENT_BUILDER.comment("Shows the amount of shards in the Shard Pouch in the Black Market").define("Black Market Shard Pouch Count", true);
        BLACK_MARKET_SHARD_POUCH_COUNT_SHORTHAND = CLIENT_BUILDER.comment("Uses shorthand for the Black Market Shard Pouch count (e.g. 1.4M instead of 1408933)").define("Black Market Shard Pouch Count Shorthand", false);
        SCAVENGER_INV_COUNT = CLIENT_BUILDER.comment("Shows the number of scavenger items you have in your inventory").define("Scavenger Inventory Count", true);
        SCAVENGER_HIGHLIGHTER = CLIENT_BUILDER.comment("Highlights current objective scavenger items in your inventory").define("Scavenger Highlighter", true);

        CLIENT_BUILDER.pop();




        CLIENT_BUILDER.push("Client-Server Extensions");

        VAULT_ENCHANTER_EMERALDS_SLOT = CLIENT_BUILDER.comment("Adds an emerald slot to the Vault Enchanter").define("Vault Enchanter Emeralds Slot", true);

        CLIENT_SPEC = CLIENT_BUILDER.build();
    }


}
