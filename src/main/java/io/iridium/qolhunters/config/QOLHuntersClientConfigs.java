package io.iridium.qolhunters.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class QOLHuntersClientConfigs {

    public static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> VANILLA_SAFE_MODE;

    //public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_DESCRIPTIONS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_STATS_DESCRIPTIONS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_ABILITIES_DESCRIPTIONS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_TALENTS_EXPERTISE_RESEARCH_DESCRIPTIONS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_BINGO_DESCRIPTIONS;


    public static final ForgeConfigSpec.ConfigValue<Boolean> VAULT_MODIFIER_TEXT_OVERLAYS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VAULT_ENCHANTER_EMERALDS_SLOT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VAULT_INTERFACE_KEYBINDS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_ABILITIES_TAB;
    public static final ForgeConfigSpec.EnumValue<CakeVaultOverlayColor> CAKE_VAULT_OVERLAY_COLOR;
    public static final ForgeConfigSpec.EnumValue<CakeVaultOverlayStyle> CAKE_VAULT_OVERLAY_STYLE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BLACK_MARKET_SHARD_POUCH_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BLACK_MARKET_SHARD_POUCH_COUNT_SHORTHAND;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SCAVENGER_INV_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SCAVENGER_HIGHLIGHTER;
    public static final ForgeConfigSpec.EnumValue<BrazierHologramMode> BRAZIER_HOLOGRAM_MODE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SAVE_KEYBINDS_WITH_SKILL_ALTAR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_GEAR_COOLDOWN_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> BINGO_GRID_BACKGROUND_OPACITY;

    public static final ForgeConfigSpec.ConfigValue<Integer> GOD_OBJECTIVE_X_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<Integer> GOD_OBJECTIVE_Y_OFFSET;

    public static final ForgeConfigSpec.ConfigValue<Boolean> PARADOX_GATE_ZOOM;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BARTERING_DISCOUNT_DISPLAY;
//    public static final ForgeConfigSpec.ConfigValue<Integer> BARTERING_DISCOUNT;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ABILITY_MULTICAST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CHAIN_BOOSTER_PACKS;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_CONFIG_BUTTON;

    public static final ForgeConfigSpec.ConfigValue<Boolean> RARITY_HIGHLIGHTER;
    public static final ForgeConfigSpec.EnumValue<RarityHighlighterMode> RARITY_HIGHLIGHTER_MODE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RARITY_HIGHLIGHTER_RARE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RARITY_HIGHLIGHTER_EPIC;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RARITY_HIGHLIGHTER_OMEGA;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RARITY_HIGHLIGHTER_UNIQUE;


    public enum BrazierHologramMode {
        DEFAULT,
        MODE1,
        MODE2,
        MODE3
    }

    public enum CakeVaultOverlayStyle {
        VIGNETTE,
        RADAR
    }

    public enum CakeVaultOverlayColor {
        PINK(0xFF77BA),
        BLUE(0x219ebc),
        YELLOW(0xffc300),
        GREEN(0x77ba77);

        private final int colorCode;
        CakeVaultOverlayColor(int colorCode) {this.colorCode = colorCode;}
        public int getColorCode() {return colorCode;}
    }

    public enum RarityHighlighterMode {
        GRADIENT,
        UNDERLINE
    }



    public record ConfigPaths() {
        public static final String VANILLA_SAFE_MODE = "Vanilla Safe Mode";

        public static final String BETTER_DESCRIPTIONS = "Better Descriptions";
        public static final String BETTER_STATS_DESCRIPTIONS = "Better Stats Descriptions";
        public static final String BETTER_ABILITIES_DESCRIPTIONS = "Better Abilities Descriptions";
        public static final String BETTER_TALENTS_EXPERTISE_RESEARCH_DESCRIPTIONS = "Better Talents, Expertise, Research Descriptions";
        public static final String BETTER_BINGO_DESCRIPTIONS = "Better Bingo Descriptions";

        public static final String VAULT_MODIFIER_TEXT_OVERLAYS = "Vault Modifier Text Overlays";
        public static final String VAULT_ENCHANTER_EMERALDS_SLOT = "Vault Enchanter Emeralds Slot";
        public static final String VAULT_INTERFACE_KEYBINDS = "Vault Interface Keybinds";
        public static final String BETTER_ABILITIES_TAB = "Better Abilities Tab";
        public static final String CAKE_VAULT_OVERLAY_COLOR = "Cake Vault Overlay Color";
        public static final String CAKE_VAULT_OVERLAY_STYLE = "Cake Vault Overlay Style";
        public static final String BLACK_MARKET_SHARD_POUCH_COUNT = "Black Market Shard Pouch Count";
        public static final String BLACK_MARKET_SHARD_POUCH_COUNT_SHORTHAND = "Black Market Shard Pouch Count Shorthand";
        public static final String SCAVENGER_INV_COUNT = "Scavenger Inventory Count";
        public static final String SAVE_KEYBINDS_WITH_SKILL_ALTAR = "Skill Altar Save Keybinds";
        public static final String BRAZIER_HOLOGRAM_MODE = "Brazier Hologram Mode";
        public static final String SCAVENGER_HIGHLIGHTER = "Scavenger Highlighter";
        public static final String SHOW_GEAR_COOLDOWN_TIME = "Show Gear Cooldown Time";
        public static final String BINGO_GRID_BACKGROUND_OPACITY = "Grid Background Opacity";
        public static final String GOD_OBJECTIVE_X_OFFSET = "God Objective X Offset";
        public static final String GOD_OBJECTIVE_Y_OFFSET = "God Objective Y Offset";
        public static final String PARADOX_GATE_ZOOM = "Paradox Gate Zoom";
        public static final String ENABLE_BARTERING_DISCOUNT_DISPLAY = "Enable Bartering Discount Display";
        public static final String BARTERING_DISCOUNT = "Bartering Discount";
        public static final String ABILITY_MULTICAST = "Ability Multicast";
        public static final String CHAIN_BOOSTER_PACKS = "Chain Booster Packs";
        public static final String SHOW_CONFIG_BUTTON = "Show Config Button";
        public static final String RARITY_HIGHLIGHTER = "Enable Rarity Highlighter";
        public static final String RARITY_HIGHLIGHTER_MODE = "Highlighter Mode";
        public static final String RARITY_HIGHLIGHTER_RARE = "Highlight Rare Gear";
        public static final String RARITY_HIGHLIGHTER_EPIC = "Highlight Epic Gear";
        public static final String RARITY_HIGHLIGHTER_OMEGA = "Highlight Omega Gear";
        public static final String RARITY_HIGHLIGHTER_UNIQUE = "Highlight Unique Gear";

        public record Group() {
            public static final String BRAZIER_GROUP = "Brazier Vaults";
            public static final String SCAVENGER_GROUP = "Scavenger Vaults";
            public static final String CAKE_GROUP = "Cake Vaults";
            public static final String BINGO_GROUP = "Bingo Vaults";
            public static final String PARADOX_GATE_GROUP = "Paradox Vaults";
            public static final String BLACK_MARKET_GROUP = "Black Market";
            public static final String SHOPPING_GROUP = "Shopping Pedestals";
            public static final String BETTER_DESCRIPTIONS_GROUP = "Better Descriptions";

            public static final String RARITY_HIGHLIGHTER_GROUP = "Rarity Highlighter";

            public static final String HUD_POSITION_GROUP = "HUD Positioning";
            public static final String GOD_OBJECTIVE_GROUP = "God Objective";
            public static final String SEARCH_GROUP = "Search Commands";

            public static final String CLIENT_GROUP = "Client-Only Extensions";
            public static final String CLIENT_SERVER_GROUP = "Client-Server Extensions";
            public static final String GENERAL_GROUP = "General Configs";

        }
    }




    static {

        CLIENT_BUILDER.comment("QOLHunters Configuration\nSOME CHANGES REQUIRE A CLIENT RESTART");

        CLIENT_BUILDER.push(ConfigPaths.Group.GENERAL_GROUP);
            SHOW_CONFIG_BUTTON = CLIENT_BUILDER.comment("Show the Config Button in the Statistics Menu (H)").define(ConfigPaths.SHOW_CONFIG_BUTTON, true);
            VANILLA_SAFE_MODE = CLIENT_BUILDER.comment("Disables QOL mods that cannot work on vanilla VH servers (disables all mods in the 'Client-Server Extensions' list)\nBe very careful if you change this on single player worlds! For example, if you have emeralds in your enchantment table and you enable this, those will probably get deleted if you load the world before changing this back\nRequires Restart").define(ConfigPaths.VANILLA_SAFE_MODE, false);

        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.push(ConfigPaths.Group.CLIENT_GROUP);

            //BETTER_DESCRIPTIONS = CLIENT_BUILDER.comment("Improves the descriptions of abilities, talents, expertises and researches\nRequires Restart").worldRestart().define(ConfigPaths.BETTER_DESCRIPTIONS, true);
            VAULT_MODIFIER_TEXT_OVERLAYS = CLIENT_BUILDER.comment("Adds text overlays to the Vault modifiers, e.g. '+10% Damage' or 'Speed +1'").define(ConfigPaths.VAULT_MODIFIER_TEXT_OVERLAYS, true);
            VAULT_INTERFACE_KEYBINDS = CLIENT_BUILDER.comment("Adds keybinds to craft/forge/reroll in the Bounty Table, Enchanter, Vault Forge, etc").define(ConfigPaths.VAULT_INTERFACE_KEYBINDS, true);
            BETTER_ABILITIES_TAB = CLIENT_BUILDER.comment("Improves the Abilities Tab including levelling specializations directly and showing all possible levels/overlevels").define(ConfigPaths.BETTER_ABILITIES_TAB, true);
            SAVE_KEYBINDS_WITH_SKILL_ALTAR = CLIENT_BUILDER.comment("Saves and loads current keybinds when you save/load skills in the Skill Altar").define(ConfigPaths.SAVE_KEYBINDS_WITH_SKILL_ALTAR, true);
            SHOW_GEAR_COOLDOWN_TIME = CLIENT_BUILDER.comment("Render a timer over Vault Gear items that are on cooldown").define(ConfigPaths.SHOW_GEAR_COOLDOWN_TIME, true);
            ABILITY_MULTICAST = CLIENT_BUILDER.comment("Allows you to cast multiple abilities with a single keybind").define(ConfigPaths.ABILITY_MULTICAST, true);
            CHAIN_BOOSTER_PACKS = CLIENT_BUILDER.comment("Automatically open the next booster pack in your inventory after you select a card").define(ConfigPaths.CHAIN_BOOSTER_PACKS, true);

            CLIENT_BUILDER.push(ConfigPaths.Group.BETTER_DESCRIPTIONS_GROUP);
                BETTER_STATS_DESCRIPTIONS = CLIENT_BUILDER.comment("Improves descriptions in the 'Statistics' Tab.").define(ConfigPaths.BETTER_STATS_DESCRIPTIONS, true);
                BETTER_ABILITIES_DESCRIPTIONS = CLIENT_BUILDER.comment("Improves descriptions in the 'Abilities' Tab.\nDisable if you're not on version 3.15.1.4").define(ConfigPaths.BETTER_ABILITIES_DESCRIPTIONS, true);
                BETTER_TALENTS_EXPERTISE_RESEARCH_DESCRIPTIONS = CLIENT_BUILDER.comment("Improves descriptions in the 'Talents', 'Expertises', and 'Researches' Tabs.\nDisable if you're not on version 3.15.1.4").define(ConfigPaths.BETTER_TALENTS_EXPERTISE_RESEARCH_DESCRIPTIONS, true);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.BLACK_MARKET_GROUP);
                BLACK_MARKET_SHARD_POUCH_COUNT = CLIENT_BUILDER.comment("Displays the number of shards in your Shard Pouch within the Black Market's interface").define(ConfigPaths.BLACK_MARKET_SHARD_POUCH_COUNT, true);
                BLACK_MARKET_SHARD_POUCH_COUNT_SHORTHAND = CLIENT_BUILDER.comment("Uses shorthand for the Black Market Shard Pouch count (e.g. 1.4M instead of 1408933)").define(ConfigPaths.BLACK_MARKET_SHARD_POUCH_COUNT_SHORTHAND, false);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.SCAVENGER_GROUP);
                SCAVENGER_INV_COUNT = CLIENT_BUILDER.comment("Shows the number of scavenger items you have in your inventory").define(ConfigPaths.SCAVENGER_INV_COUNT, true);
                SCAVENGER_HIGHLIGHTER = CLIENT_BUILDER.comment("Highlights current objective scavenger items in your inventory").define(ConfigPaths.SCAVENGER_HIGHLIGHTER, true);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.BRAZIER_GROUP);
                BRAZIER_HOLOGRAM_MODE = CLIENT_BUILDER.comment("Changes the mode of the Brazier Hologram\nDEFAULT = Always show all icons and text\nMODE1 = Always show icons, text renders when close to the brazier\nMODE2 = Always show icons, hold SHIFT to show text\nMODE3 = Always show icons and labels, hold SHIFT to show descriptions").defineEnum(ConfigPaths.BRAZIER_HOLOGRAM_MODE, BrazierHologramMode.DEFAULT);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.CAKE_GROUP);
                CAKE_VAULT_OVERLAY_COLOR = CLIENT_BUILDER.comment("Changes the color of the Vault Cake overlay").defineEnum(ConfigPaths.CAKE_VAULT_OVERLAY_COLOR, CakeVaultOverlayColor.PINK);
                CAKE_VAULT_OVERLAY_STYLE = CLIENT_BUILDER.comment("Changes the style of the Vault Cake overlay").defineEnum(ConfigPaths.CAKE_VAULT_OVERLAY_STYLE, CakeVaultOverlayStyle.VIGNETTE);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.BINGO_GROUP);
                BINGO_GRID_BACKGROUND_OPACITY = CLIENT_BUILDER.comment("Changes the opacity (%) of the Bingo Grid background").defineInRange(ConfigPaths.BINGO_GRID_BACKGROUND_OPACITY, 50, 0, 100);
                BETTER_BINGO_DESCRIPTIONS = CLIENT_BUILDER.comment("Improves the descriptions of bingo objectives in Bingo Vaults\nMight not work on servers?").define(ConfigPaths.BETTER_BINGO_DESCRIPTIONS, true);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.HUD_POSITION_GROUP);

                CLIENT_BUILDER.push(ConfigPaths.Group.GOD_OBJECTIVE_GROUP);
                    GOD_OBJECTIVE_X_OFFSET = CLIENT_BUILDER.comment("Changes the X offset of the God Objective\nDefined as % of screen width").defineInRange(ConfigPaths.GOD_OBJECTIVE_X_OFFSET, 0, 0, 100);
                    GOD_OBJECTIVE_Y_OFFSET = CLIENT_BUILDER.comment("Changes the Y offset of the God Objective\nDefined as % of screen height").defineInRange(ConfigPaths.GOD_OBJECTIVE_Y_OFFSET, 45, 0, 100);
                CLIENT_BUILDER.pop();

            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.PARADOX_GATE_GROUP);
                PARADOX_GATE_ZOOM = CLIENT_BUILDER.comment("Hold <SHIFT> to enlarge the paradox vault gate unlock text.\nNote: Designed for BUILD mode, but currently also takes effect when running the vault too.").define(ConfigPaths.PARADOX_GATE_ZOOM, true);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.SHOPPING_GROUP);
                ENABLE_BARTERING_DISCOUNT_DISPLAY = CLIENT_BUILDER.comment("Includes your bartering discount on shopping pedestals").define(ConfigPaths.ENABLE_BARTERING_DISCOUNT_DISPLAY, true);
//                BARTERING_DISCOUNT = CLIENT_BUILDER.comment("Enter your current bartering discount (%) to update the shown cost on pedestals.\nOne day I will figure out how to grab this data automatically...").defineInRange(ConfigPaths.BARTERING_DISCOUNT, 0, 0, 100);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.RARITY_HIGHLIGHTER_GROUP);
                RARITY_HIGHLIGHTER = CLIENT_BUILDER.comment("Enable highlighting Vault Gear in your Inventory while holding <SHIFT>").define(ConfigPaths.RARITY_HIGHLIGHTER, true);
                RARITY_HIGHLIGHTER_MODE = CLIENT_BUILDER.comment("Highlighter Mode").defineEnum(ConfigPaths.RARITY_HIGHLIGHTER_MODE, RarityHighlighterMode.GRADIENT);
                RARITY_HIGHLIGHTER_RARE = CLIENT_BUILDER.comment("Highlight Rare Gear").define(ConfigPaths.RARITY_HIGHLIGHTER_RARE, true);
                RARITY_HIGHLIGHTER_EPIC = CLIENT_BUILDER.comment("Highlight Epic Gear").define(ConfigPaths.RARITY_HIGHLIGHTER_EPIC, true);
                RARITY_HIGHLIGHTER_OMEGA = CLIENT_BUILDER.comment("Highlight Omega Gear").define(ConfigPaths.RARITY_HIGHLIGHTER_OMEGA, true);
                RARITY_HIGHLIGHTER_UNIQUE = CLIENT_BUILDER.comment("Highlight Unique Gear").define(ConfigPaths.RARITY_HIGHLIGHTER_UNIQUE, true);
            CLIENT_BUILDER.pop();


        CLIENT_BUILDER.push(ConfigPaths.Group.CLIENT_SERVER_GROUP);

            VAULT_ENCHANTER_EMERALDS_SLOT = CLIENT_BUILDER.comment("Adds an emerald slot to the Vault Enchanter\nThis setting will be safely ignored if you connect to a server without QOLHunters installed").worldRestart().define(ConfigPaths.VAULT_ENCHANTER_EMERALDS_SLOT, true);

        CLIENT_SPEC = CLIENT_BUILDER.build();



    }



}
