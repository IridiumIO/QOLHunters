package io.iridium.qolhunters.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class QOLHuntersClientConfigs {

    public static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_STATS_DESCRIPTIONS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_BINGO_DESCRIPTIONS;


    public static final ForgeConfigSpec.ConfigValue<Boolean> VAULT_MODIFIER_TEXT_OVERLAYS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VAULT_MODIFIERS_TOP_RIGHT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> VAULT_INTERFACE_KEYBINDS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_ABILITIES_TAB;
    public static final ForgeConfigSpec.EnumValue<CakeVaultOverlayColor> CAKE_VAULT_OVERLAY_COLOR;
    public static final ForgeConfigSpec.EnumValue<CakeVaultOverlayStyle> CAKE_VAULT_OVERLAY_STYLE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SCAVENGER_INV_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SCAVENGER_HIGHLIGHTER;
    public static final ForgeConfigSpec.EnumValue<BrazierHologramMode> BRAZIER_HOLOGRAM_MODE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SAVE_KEYBINDS_WITH_SKILL_ALTAR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_GEAR_COOLDOWN_TIME;
    public static final ForgeConfigSpec.ConfigValue<Integer> BINGO_GRID_BACKGROUND_OPACITY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SEARCHABLE_VAULT_STATIONS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BROKEN_CURIO_ALERT;

    public static final ForgeConfigSpec.ConfigValue<Integer> GOD_OBJECTIVE_X_OFFSET;
    public static final ForgeConfigSpec.ConfigValue<Integer> GOD_OBJECTIVE_Y_OFFSET;

    public static final ForgeConfigSpec.ConfigValue<Boolean> PARADOX_GATE_ZOOM;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BARTERING_DISCOUNT_DISPLAY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_SHOPPING_PED_THROW_ITEMS;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ABILITY_MULTICAST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CHAIN_BOOSTER_PACKS;

    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_CONFIG_BUTTON;

    public static final ForgeConfigSpec.ConfigValue<Boolean> RARITY_HIGHLIGHTER;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ASCENSION_CRYSTAL_EMBER_GRANT_AMOUNT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> CHALLENGER_ROCK_EMBER_GRANT_AMOUNT;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_VIRTUAL_DEHAMMERIZER;
    public static final ForgeConfigSpec.ConfigValue<Integer> VIRTUAL_DEHAMMERIZER_RANGE;
    public static final ForgeConfigSpec.EnumValue<VirtualDehammerizerMode> VIRTUAL_DEHAMMERIZER_MODE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_SOUL_VALUE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_SOUL_VALUE_SHORTHAND;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_SOUL_VALUE_USE_SHARDS;

    public static final ForgeConfigSpec.ConfigValue<Boolean> BACKPACK_CYCLER;


    public static final ForgeConfigSpec.ConfigValue<Boolean> HUNTER_PARTICLES_BLOCKS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> HUNTER_PARTICLES_GILDED;
    public static final ForgeConfigSpec.ConfigValue<Boolean> HUNTER_PARTICLES_LIVING;
    public static final ForgeConfigSpec.ConfigValue<Boolean> HUNTER_PARTICLES_ORNATE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> HUNTER_PARTICLES_COINS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> HUNTER_PARTICLES_WOODEN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> HUNTER_PARTICLES_OTHER;

    public static final ForgeConfigSpec.ConfigValue<Boolean> TREASURE_KEY_SWAPPER;

    public static final ForgeConfigSpec.EnumValue<BingoGridCompletionColor> BINGO_GRID_COMPLETED_COLOR;
    public static final ForgeConfigSpec.EnumValue<BingoGridSelectionColor> BINGO_GRID_SELECTION_COLOR;

    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_SCREEN_JEWEL_APPLICATION;
    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_SCREEN_CARD_DECK;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ELIXIR_SHADOWLESS_ORBS;
    public static final ForgeConfigSpec.EnumValue<ElixirOrbCountCulling> ELIXIR_ORB_CULLING;

    public static final ForgeConfigSpec.ConfigValue<Integer> GEAR_ROLL_COLOR_SCRAPPY;
    public static final ForgeConfigSpec.ConfigValue<Integer> GEAR_ROLL_COLOR_COMMON;
    public static final ForgeConfigSpec.ConfigValue<Integer> GEAR_ROLL_COLOR_RARE;
    public static final ForgeConfigSpec.ConfigValue<Integer> GEAR_ROLL_COLOR_EPIC;
    public static final ForgeConfigSpec.ConfigValue<Integer> GEAR_ROLL_COLOR_OMEGA;


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

    public enum BingoGridCompletionColor {
        RED(0x64ff0000),
        BLUE(0x6400d2ff),
        YELLOW(0x64ffff00),
        GREEN(0x6400ff00),
        GLORP(0x64ff00ff);

        private final int colorCode;
        BingoGridCompletionColor(int colorCode) {this.colorCode = colorCode;}
        public int getColorCode() {return colorCode;}
    }

    public enum BingoGridSelectionColor {
        RED(0x40ff0000),
        BLUE(0x4000d2ff),
        YELLOW(0x40ffff00),
        GREEN(0x4000ff00),
        GLORP(0x40ff00ff);

        private final int colorCode;
        BingoGridSelectionColor(int colorCode) {this.colorCode = colorCode;}
        public int getColorCode() {return colorCode;}
    }


    public enum RarityHighlighterMode {
        GRADIENT,
        UNDERLINE
    }

    public enum VirtualDehammerizerMode {
        SPHERE,
        CYLINDER,
    }

    public enum ElixirOrbCountCulling {
        NONE(999),
        LOW(100),
        MED(50),
        HIGH(25);

        private final int count;
        ElixirOrbCountCulling(int count) {this.count = count;}
        public int getCount() {return count;}
    }


    public record ConfigPaths() {

        public static final String BETTER_STATS_DESCRIPTIONS = "Better Stats Descriptions";
        public static final String BETTER_BINGO_DESCRIPTIONS = "Better Bingo Descriptions";

        public static final String VAULT_MODIFIER_TEXT_OVERLAYS = "Vault Modifier Text Overlays";
        public static final String VAULT_MODIFIERS_TOP_RIGHT = "Vault Modifiers In Top Right";
        public static final String VAULT_INTERFACE_KEYBINDS = "Vault Interface Keybinds";
        public static final String BETTER_ABILITIES_TAB = "Better Abilities Tab";
        public static final String CAKE_VAULT_OVERLAY_COLOR = "Cake Vault Overlay Color";
        public static final String CAKE_VAULT_OVERLAY_STYLE = "Cake Vault Overlay Style";
        public static final String SCAVENGER_INV_COUNT = "Scavenger Inventory Count";
        public static final String SAVE_KEYBINDS_WITH_SKILL_ALTAR = "Skill Altar Save Keybinds";
        public static final String BRAZIER_HOLOGRAM_MODE = "Brazier Hologram Mode";
        public static final String SCAVENGER_HIGHLIGHTER = "Scavenger Highlighter";
        public static final String SHOW_GEAR_COOLDOWN_TIME = "Show Gear Cooldown Time";
        public static final String BINGO_GRID_BACKGROUND_OPACITY = "Grid Background Opacity";
        public static final String SEARCHABLE_VAULT_STATIONS = "Searchable Vault Stations";
        public static final String BROKEN_CURIO_ALERT = "Broken Curio Alert";
        public static final String GOD_OBJECTIVE_X_OFFSET = "God Objective X Offset";
        public static final String GOD_OBJECTIVE_Y_OFFSET = "God Objective Y Offset";
        public static final String PARADOX_GATE_ZOOM = "Paradox Gate Zoom";
        public static final String ENABLE_BARTERING_DISCOUNT_DISPLAY = "Enable Bartering Discount Display";
        public static final String ENABLE_SHOPPING_PED_THROW_ITEMS = "Shopping Pedestal Throws Items";
        public static final String ABILITY_MULTICAST = "Ability Multicast";
        public static final String CHAIN_BOOSTER_PACKS = "Chain Booster Packs";
        public static final String SHOW_CONFIG_BUTTON = "Show Config Button";
        public static final String RARITY_HIGHLIGHTER = "Enable Rarity Highlighter in AE and RS";
        public static final String VIRTUAL_DEHAMMERIZER_X = "Dehammerizer X";
        public static final String VIRTUAL_DEHAMMERIZER_Y = "Dehammerizer Y";
        public static final String VIRTUAL_DEHAMMERIZER_Z = "Dehammerizer Z";
        public static final String ENABLE_VIRTUAL_DEHAMMERIZER = "Enable Virtual Dehammerizer";
        public static final String VIRTUAL_DEHAMMERIZER_RANGE = "Dehammerizer Range";
        public static final String VIRTUAL_DEHAMMERIZER_MODE = "Dehammerizer Mode";
        public static final String ASCENSION_CRYSTAL_GRANT = "Ascension Crystal Ember Grant Amount";
        public static final String CHALLENGER_ROCK_GRANT = "Challenger Rock Ember Grant Amount";
        public static final String BETTER_SOUL_VALUE = "Enable Better Tooltips";
        public static final String BETTER_SOUL_VALUE_SHORTHAND = "Use Shorthand";
        public static final String BETTER_SOUL_VALUE_USE_SHARDS = "Use Soul Shards";
        public static final String BACKPACK_CYCLER = "Backpack Cycler";
        public static final String TREASURE_KEY_SWAPPER = "Treasure Key Swapper";

        public static final String HUNTER_PARTICLES_BLOCKS = "Objectives";
        public static final String HUNTER_PARTICLES_GILDED = "Gilded";
        public static final String HUNTER_PARTICLES_LIVING = "Living";
        public static final String HUNTER_PARTICLES_ORNATE = "Ornate";
        public static final String HUNTER_PARTICLES_COINS = "Coins";
        public static final String HUNTER_PARTICLES_WOODEN = "Wooden";
        public static final String HUNTER_PARTICLES_OTHER = "Default";

        public static final String BINGO_GRID_COMPLETED_COLOR = "Grid Completion Color";
        public static final String BINGO_GRID_SELECTION_COLOR = "Grid Selection Color";

        public static final String BETTER_SCREEN_JEWEL_APPLICATION = "Better Jewel Application Screen";
        public static final String BETTER_SCREEN_CARD_DECK = "Better Card Deck Screen";

        public static final String ELIXIR_SHADOWLESS_ORBS = "Shadowless Elixir Orbs";
        public static final String ELIXIR_ORB_CULLING = "Elixir Orb Culling";

        public static final String GEAR_ROLL_COLOR_SCRAPPY = "Scrappy+";
        public static final String GEAR_ROLL_COLOR_COMMON = "Common+";
        public static final String GEAR_ROLL_COLOR_RARE = "Rare+";
        public static final String GEAR_ROLL_COLOR_EPIC = "Epic+";
        public static final String GEAR_ROLL_COLOR_OMEGA = "Omega";

        public record Group() {
            public static final String BRAZIER_GROUP = "Brazier Vaults";
            public static final String SCAVENGER_GROUP = "Scavenger Vaults";
            public static final String CAKE_GROUP = "Cake Vaults";
            public static final String BINGO_GROUP = "Bingo Vaults";
            public static final String PARADOX_GATE_GROUP = "Paradox Vaults";
            public static final String ELIXIR_GROUP = "Elixir Vaults";
            public static final String SHOPPING_GROUP = "Shopping Pedestals";
            public static final String BETTER_DESCRIPTIONS_GROUP = "Better Descriptions";
            public static final String BETTER_SOUL_DESCRIPTIONS_GROUP = "Better Soul Value Tooltips";
            public static final String VAULT_MODIFIERS_GROUP = "Vault Modifiers";

            public static final String RARITY_HIGHLIGHTER_GROUP = "Rarity Highlighter";

            public static final String VIRTUAL_DEHAMMERIZER_GROUP = "Virtual Dehammerizer";

            public static final String ASCENSION = "Ascension Vaults";

            public static final String HUD_POSITION_GROUP = "HUD Positioning";
            public static final String GOD_OBJECTIVE_GROUP = "God Objective";
            public static final String SEARCH_GROUP = "Search Commands";
            public static final String HUNTER_PARTICLES_GROUP = "Hunter Particles";

            public static final String CLIENT_GROUP = "Client-Only Extensions";
            public static final String GENERAL_GROUP = "General Configs";

            public static final String BETTER_SCREENS_GROUP = "Better Screens";

            public static final String GEAR_ROLL_COLOR_GROUP = "Gear Roll Colors";
        }
    }




    static {

        CLIENT_BUILDER.comment("QOLHunters Configuration\nSOME CHANGES REQUIRE A CLIENT RESTART");

        CLIENT_BUILDER.push(ConfigPaths.Group.GENERAL_GROUP);
            SHOW_CONFIG_BUTTON = CLIENT_BUILDER.comment("Show the Config Button in the Statistics Menu (H)").define(ConfigPaths.SHOW_CONFIG_BUTTON, true);

        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.push(ConfigPaths.Group.CLIENT_GROUP);

            VAULT_MODIFIER_TEXT_OVERLAYS = CLIENT_BUILDER.comment("Adds text overlays to the Vault modifiers, e.g. '+10% Damage' or 'Speed +1'").define(ConfigPaths.VAULT_MODIFIER_TEXT_OVERLAYS, true);
            VAULT_INTERFACE_KEYBINDS = CLIENT_BUILDER.comment("Adds keybinds to craft/forge/reroll in the Bounty Table, Enchanter, Vault Forge, etc").define(ConfigPaths.VAULT_INTERFACE_KEYBINDS, true);
            BETTER_ABILITIES_TAB = CLIENT_BUILDER.comment("Improves the Abilities Tab including levelling specializations directly and showing all possible levels/overlevels").define(ConfigPaths.BETTER_ABILITIES_TAB, true);
            SAVE_KEYBINDS_WITH_SKILL_ALTAR = CLIENT_BUILDER.comment("Saves and loads current keybinds when you save/load skills in the Skill Altar").define(ConfigPaths.SAVE_KEYBINDS_WITH_SKILL_ALTAR, true);
            SHOW_GEAR_COOLDOWN_TIME = CLIENT_BUILDER.comment("Render a timer over Vault Gear items that are on cooldown").define(ConfigPaths.SHOW_GEAR_COOLDOWN_TIME, true);
            ABILITY_MULTICAST = CLIENT_BUILDER.comment("Allows you to cast multiple abilities with a single keybind").define(ConfigPaths.ABILITY_MULTICAST, true);
            CHAIN_BOOSTER_PACKS = CLIENT_BUILDER.comment("Automatically open the next booster pack in your inventory after you select a card").define(ConfigPaths.CHAIN_BOOSTER_PACKS, true);
            BACKPACK_CYCLER = CLIENT_BUILDER.comment("Add inventory buttons / use arrow keys to cycle through backpacks in your inventory").define(ConfigPaths.BACKPACK_CYCLER, true);
            TREASURE_KEY_SWAPPER = CLIENT_BUILDER.comment("Autoswap to the correct treasure key when you right-click a treasure door with another key as long as the correct key is in your main inventory").define(ConfigPaths.TREASURE_KEY_SWAPPER, true);
            SEARCHABLE_VAULT_STATIONS = CLIENT_BUILDER.comment("Add search box to vault stations").define(ConfigPaths.SEARCHABLE_VAULT_STATIONS, true);
            BROKEN_CURIO_ALERT = CLIENT_BUILDER.comment("Show alert when equipped vault curio is broken").define(ConfigPaths.BROKEN_CURIO_ALERT, true);

            CLIENT_BUILDER.push(ConfigPaths.Group.BETTER_DESCRIPTIONS_GROUP);
                BETTER_STATS_DESCRIPTIONS = CLIENT_BUILDER.comment("Fixes lines in descriptions in the 'Statistics' Tab.").define(ConfigPaths.BETTER_STATS_DESCRIPTIONS, true);

                CLIENT_BUILDER.push(ConfigPaths.Group.BETTER_SOUL_DESCRIPTIONS_GROUP);
                    BETTER_SOUL_VALUE = CLIENT_BUILDER.comment("Improves the descriptions of soul values in tooltips when holding SHIFT").define(ConfigPaths.BETTER_SOUL_VALUE, true);
                    BETTER_SOUL_VALUE_SHORTHAND = CLIENT_BUILDER.comment("Uses shorthand for soul values in tooltips (e.g. 1.4M instead of 1408933)").define(ConfigPaths.BETTER_SOUL_VALUE_SHORTHAND, true);
                    BETTER_SOUL_VALUE_USE_SHARDS = CLIENT_BUILDER.comment("Use soul shards instead of soul value ").define(ConfigPaths.BETTER_SOUL_VALUE_USE_SHARDS, false);
                CLIENT_BUILDER.pop();

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
                BINGO_GRID_COMPLETED_COLOR = CLIENT_BUILDER.comment("Changes the color of the Bingo Grid completion overlay").defineEnum(ConfigPaths.BINGO_GRID_COMPLETED_COLOR, BingoGridCompletionColor.GREEN);
                BINGO_GRID_SELECTION_COLOR = CLIENT_BUILDER.comment("Changes the color of the Bingo Grid selection overlay").defineEnum(ConfigPaths.BINGO_GRID_SELECTION_COLOR, BingoGridSelectionColor.YELLOW);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.ELIXIR_GROUP);
                ELIXIR_SHADOWLESS_ORBS = CLIENT_BUILDER.comment("Removes shadows from Elixir orbs. Significantly improves performance.\nRequires Restart").define(ConfigPaths.ELIXIR_SHADOWLESS_ORBS, true);
                ELIXIR_ORB_CULLING = CLIENT_BUILDER.comment("Culls Elixir orbs when there are more than this many on screen\nLOW=100\nMED=50\nHIGH=25").defineEnum(ConfigPaths.ELIXIR_ORB_CULLING, ElixirOrbCountCulling.MED);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.HUD_POSITION_GROUP);

                CLIENT_BUILDER.push(ConfigPaths.Group.GOD_OBJECTIVE_GROUP);
                    GOD_OBJECTIVE_X_OFFSET = CLIENT_BUILDER.comment("Changes the X offset of the God Objective\nDefined as % of screen width").defineInRange(ConfigPaths.GOD_OBJECTIVE_X_OFFSET, 0, 0, 100);
                    GOD_OBJECTIVE_Y_OFFSET = CLIENT_BUILDER.comment("Changes the Y offset of the God Objective\nDefined as % of screen height").defineInRange(ConfigPaths.GOD_OBJECTIVE_Y_OFFSET, 45, 0, 100);
                CLIENT_BUILDER.pop();

                CLIENT_BUILDER.push(ConfigPaths.Group.VAULT_MODIFIERS_GROUP);
                    VAULT_MODIFIERS_TOP_RIGHT = CLIENT_BUILDER.comment("Render Vault Modifiers in the top right corner of the screen").define(ConfigPaths.VAULT_MODIFIERS_TOP_RIGHT, false);
                CLIENT_BUILDER.pop();

            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.PARADOX_GATE_GROUP);
                PARADOX_GATE_ZOOM = CLIENT_BUILDER.comment("Hold <SHIFT> to enlarge the paradox vault gate unlock text.\nNote: Designed for BUILD mode, but currently also takes effect when running the vault too.").define(ConfigPaths.PARADOX_GATE_ZOOM, true);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.SHOPPING_GROUP);
                ENABLE_BARTERING_DISCOUNT_DISPLAY = CLIENT_BUILDER.comment("Includes your bartering discount on shopping pedestals").define(ConfigPaths.ENABLE_BARTERING_DISCOUNT_DISPLAY, true);
                ENABLE_SHOPPING_PED_THROW_ITEMS = CLIENT_BUILDER.comment("Shopping pedestals throw items so that your magnets/bags can pick them up instead").define(ConfigPaths.ENABLE_SHOPPING_PED_THROW_ITEMS, true);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.RARITY_HIGHLIGHTER_GROUP);
                RARITY_HIGHLIGHTER = CLIENT_BUILDER.comment("Enable highlighting Vault Gear in AE and RS while holding <SHIFT>").define(ConfigPaths.RARITY_HIGHLIGHTER, true);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.comment("Use a Warped Fungus on a Stick to configure Virtual Dehammerizer.\nUse arrow UP/DOWN keys to cycle between Dehammerizers.\nUse Ctrl+RightClick to add a Dehammerizer.\nUse Ctrl+Delete to remove a Dehammerizer").push(ConfigPaths.Group.VIRTUAL_DEHAMMERIZER_GROUP);
                ENABLE_VIRTUAL_DEHAMMERIZER = CLIENT_BUILDER.comment("Enable Virtual Dehammerizer").define(ConfigPaths.ENABLE_VIRTUAL_DEHAMMERIZER, true);
                VIRTUAL_DEHAMMERIZER_MODE = CLIENT_BUILDER.comment("Virtual Dehammerizer Mode\nChoose SPHERE for a sphere defined around the chosen block position.\nChoose CYLINDER to encompass the full height of the map").defineEnum(ConfigPaths.VIRTUAL_DEHAMMERIZER_MODE, VirtualDehammerizerMode.SPHERE);
                VIRTUAL_DEHAMMERIZER_RANGE = CLIENT_BUILDER.comment("Virtual Dehammerizer Range").defineInRange(ConfigPaths.VIRTUAL_DEHAMMERIZER_RANGE, 24, 8, 64);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.ASCENSION);
                ASCENSION_CRYSTAL_EMBER_GRANT_AMOUNT = CLIENT_BUILDER.comment("Enable a tooltip to show the amount of Ember granted on a failed Ascension Vault while holding <SHIFT> on Ascension Crystals").define(ConfigPaths.ASCENSION_CRYSTAL_GRANT, true);
                CHALLENGER_ROCK_EMBER_GRANT_AMOUNT = CLIENT_BUILDER.comment("Enable a tooltip to show the amount of Ember granted on a failed Ascension Vault while holding <SHIFT> on Challenger Rocks").define(ConfigPaths.CHALLENGER_ROCK_GRANT, true);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.HUNTER_PARTICLES_GROUP);
                HUNTER_PARTICLES_BLOCKS = CLIENT_BUILDER.comment("Show Hunter Particles for Objectives/Altars").define(ConfigPaths.HUNTER_PARTICLES_BLOCKS, true);
                HUNTER_PARTICLES_GILDED = CLIENT_BUILDER.comment("Show Hunter Particles for Gilded Chests").define(ConfigPaths.HUNTER_PARTICLES_GILDED, true);
                HUNTER_PARTICLES_LIVING = CLIENT_BUILDER.comment("Show Hunter Particles for Living Chests").define(ConfigPaths.HUNTER_PARTICLES_LIVING, true);
                HUNTER_PARTICLES_ORNATE = CLIENT_BUILDER.comment("Show Hunter Particles for Ornate Chests").define(ConfigPaths.HUNTER_PARTICLES_ORNATE, true);
                HUNTER_PARTICLES_COINS = CLIENT_BUILDER.comment("Show Hunter Particles for Coins").define(ConfigPaths.HUNTER_PARTICLES_COINS, true);
                HUNTER_PARTICLES_WOODEN = CLIENT_BUILDER.comment("Show Hunter Particles for Wooden Chests").define(ConfigPaths.HUNTER_PARTICLES_WOODEN, true);
                HUNTER_PARTICLES_OTHER = CLIENT_BUILDER.comment("Show Default White Hunter Particles").define(ConfigPaths.HUNTER_PARTICLES_OTHER, true);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.BETTER_SCREENS_GROUP);
                BETTER_SCREEN_JEWEL_APPLICATION = CLIENT_BUILDER.comment("Improves the Jewel Application Screen").define(ConfigPaths.BETTER_SCREEN_JEWEL_APPLICATION, true);
                BETTER_SCREEN_CARD_DECK = CLIENT_BUILDER.comment("Improves the Card Deck Screen").define(ConfigPaths.BETTER_SCREEN_CARD_DECK, true);
            CLIENT_BUILDER.pop();

            CLIENT_BUILDER.push(ConfigPaths.Group.GEAR_ROLL_COLOR_GROUP);
                GEAR_ROLL_COLOR_SCRAPPY = CLIENT_BUILDER.comment("Scrappy+ Gear").define(ConfigPaths.GEAR_ROLL_COLOR_SCRAPPY, 12369084);
                GEAR_ROLL_COLOR_COMMON = CLIENT_BUILDER.comment("Common+ Gear").define(ConfigPaths.GEAR_ROLL_COLOR_COMMON, 5353215);
                GEAR_ROLL_COLOR_RARE = CLIENT_BUILDER.comment("Rare+ Gear").define(ConfigPaths.GEAR_ROLL_COLOR_RARE, 16771072);
                GEAR_ROLL_COLOR_EPIC = CLIENT_BUILDER.comment("Epic+ Gear").define(ConfigPaths.GEAR_ROLL_COLOR_EPIC, 16711935);
                GEAR_ROLL_COLOR_OMEGA = CLIENT_BUILDER.comment("Omega Gear").define(ConfigPaths.GEAR_ROLL_COLOR_OMEGA, 7012096);
            CLIENT_BUILDER.pop();


        CLIENT_BUILDER.pop();

        CLIENT_SPEC = CLIENT_BUILDER.build();



    }



}
