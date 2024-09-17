package io.iridium.qolhunters;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class QOLHuntersMixinPlugin implements IMixinConfigPlugin {

    private static boolean vanillaSafeMode = false;
    private static boolean betterDescriptions = true;
    private static boolean vaultModifierOverlays = true;
    private static boolean vaultInterfaceKeybinds = true;
    private static boolean vaultEnchanterEmeraldSlot = true;
    private static boolean betterAbilitiesTab = true;
    private static boolean blackMarketShardPouch = true;
    private static boolean scavengerInventoryCount = true;

    private static boolean isWoldsVaultModInstalled = false;



    private static final Supplier<Boolean> TRUE = () -> true;
    private static final Supplier<Boolean> FALSE = () -> false;

    private static final Map<String, Supplier<Boolean>> BETTER_DESCRIPTIONS_CONDITIONS = ImmutableMap.of(
            "io.iridium.qolhunters.mixin.configs.MixinAbilitiesDescriptionsConfig", () -> QOLHuntersMixinPlugin.betterDescriptions && !QOLHuntersMixinPlugin.isWoldsVaultModInstalled,
            "io.iridium.qolhunters.mixin.configs.MixinMenuPlayerStatDescriptionConfig", () -> QOLHuntersMixinPlugin.betterDescriptions && !QOLHuntersMixinPlugin.isWoldsVaultModInstalled,
            "io.iridium.qolhunters.mixin.configs.MixinSkillDescriptionsConfig", () -> QOLHuntersMixinPlugin.betterDescriptions && !QOLHuntersMixinPlugin.isWoldsVaultModInstalled,
            "io.iridium.qolhunters.mixin.configs.MixinBingoConfig", () -> QOLHuntersMixinPlugin.betterDescriptions && !QOLHuntersMixinPlugin.isWoldsVaultModInstalled
    );

    private static final Map<String, Supplier<Boolean>> VAULT_MODIFIER_OVERLAYS_CONDITIONS = ImmutableMap.of(
            "io.iridium.qolhunters.mixin.configs.MixinVaultModifiersConfig", () -> QOLHuntersMixinPlugin.vaultModifierOverlays,
            "io.iridium.qolhunters.mixin.vaultmodifiers.MixinVaultModifiersElement", () -> QOLHuntersMixinPlugin.vaultModifierOverlays
    );

    private static final Map<String, Supplier<Boolean>> VAULT_ENCHANTER_EMERALD_SLOT_CONDITIONS = ImmutableMap.of(
            "io.iridium.qolhunters.mixin.vaultenchanter.MixinEnchantmentCost", () -> QOLHuntersMixinPlugin.vaultEnchanterEmeraldSlot && !QOLHuntersMixinPlugin.vanillaSafeMode,
            "io.iridium.qolhunters.mixin.vaultenchanter.MixinVaultEnchanterBlock", () -> QOLHuntersMixinPlugin.vaultEnchanterEmeraldSlot && !QOLHuntersMixinPlugin.vanillaSafeMode,
            "io.iridium.qolhunters.mixin.vaultenchanter.MixinVaultEnchanterContainer", () -> QOLHuntersMixinPlugin.vaultEnchanterEmeraldSlot && !QOLHuntersMixinPlugin.vanillaSafeMode,
            "io.iridium.qolhunters.mixin.vaultenchanter.MixinVaultEnchanterScreen", () -> QOLHuntersMixinPlugin.vaultEnchanterEmeraldSlot && !QOLHuntersMixinPlugin.vanillaSafeMode,
            "io.iridium.qolhunters.mixin.vaultenchanter.MixinVaultEnchanterTileEntity", () -> QOLHuntersMixinPlugin.vaultEnchanterEmeraldSlot && !QOLHuntersMixinPlugin.vanillaSafeMode
    );


    private static final Map<String, Supplier<Boolean>> VAULT_KEYBINDS_CONDITIONS = ImmutableMap.of(
            "io.iridium.qolhunters.mixin.keybinds.MixinBountyScreen", () -> QOLHuntersMixinPlugin.vaultInterfaceKeybinds,
            "io.iridium.qolhunters.mixin.keybinds.MixinForgeRecipeContainerScreen", () -> QOLHuntersMixinPlugin.vaultInterfaceKeybinds,
            "io.iridium.qolhunters.mixin.keybinds.MixinModifierWorkbenchScreen", () -> QOLHuntersMixinPlugin.vaultInterfaceKeybinds,
            "io.iridium.qolhunters.mixin.keybinds.MixinVaultEnchanterScreen", () -> QOLHuntersMixinPlugin.vaultInterfaceKeybinds
    );


    private static final Map<String, Supplier<Boolean>> BETTER_ABILITIES_TAB_CONDITIONS = ImmutableMap.of(
            "io.iridium.qolhunters.mixin.Abilities.MixinAbilityDialog", () -> QOLHuntersMixinPlugin.betterAbilitiesTab
    );

    private static final Map<String, Supplier<Boolean>> BLACK_MARKET_CONDITIONS = ImmutableMap.of(
            "io.iridium.qolhunters.mixin.blackmarket.MixinShardTradeScreen", () -> QOLHuntersMixinPlugin.blackMarketShardPouch
    );

    private static final Map<String, Supplier<Boolean>> SCAVENGER_INVENTORY_CONDITIONS = ImmutableMap.of(
            "io.iridium.qolhunters.mixin.scavenger.MixinScavengerObjective", () -> QOLHuntersMixinPlugin.scavengerInventoryCount
    );


    private static final String CONFIG_FILE_NAME = "qolhunters-client.toml";
    private static final String VANILLA_SAFE_MODE_CONFIG_VALUE = "General Configs.Vanilla Safe Mode";
    private static final String BETTER_DESCRIPTIONS_CONFIG_VALUE = "Client-Only Extensions.Better Descriptions";
    private static final String VAULT_MODIFIER_OVERLAYS_CONFIG_VALUE = "Client-Only Extensions.Vault Modifier Text Overlays";
    private static final String VAULT_INTERFACE_KEYBINDS_CONFIG_VALUE = "Client-Only Extensions.Vault Interface Keybinds";
    private static final String VAULT_ENCHANTER_EMERALD_SLOT_CONFIG_VALUE = "Client-Server Extensions.Vault Enchanter Emeralds Slot";
    private static final String BETTER_ABILITIES_TAB_CONFIG_VALUE = "Client-Only Extensions.Better Abilities Tab";
    private static final String BLACK_MARKET_SHARD_POUCH_CONFIG_VALUE = "Client-Only Extensions.Black Market Shard Pouch Count";
    private static final String SCAVENGER_INV_COUNT_CONFIG_VALUE = "Client-Only Extensions.Scavenger Inventory Count";

    private static void loadConfig(){

        isWoldsVaultModInstalled = FMLLoader.getLoadingModList().getModFileById("woldsvaults") != null;
        QOLHunters.LOGGER.info("QOLHunters: WoldsVaults mod is installed: " + isWoldsVaultModInstalled);

        try {
            Path configPath = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE_NAME);
            File configFile = configPath.toFile();
            if (configFile.exists()) {
                FileConfig config = FileConfig.of(configFile);
                config.load();
                vanillaSafeMode = config.getOrElse(VANILLA_SAFE_MODE_CONFIG_VALUE, false);
                betterDescriptions = config.getOrElse(BETTER_DESCRIPTIONS_CONFIG_VALUE, true);
                vaultModifierOverlays = config.getOrElse(VAULT_MODIFIER_OVERLAYS_CONFIG_VALUE, true);
                vaultInterfaceKeybinds = config.getOrElse(VAULT_INTERFACE_KEYBINDS_CONFIG_VALUE, true);
                vaultEnchanterEmeraldSlot = config.getOrElse(VAULT_ENCHANTER_EMERALD_SLOT_CONFIG_VALUE, true);
                betterAbilitiesTab = config.getOrElse(BETTER_ABILITIES_TAB_CONFIG_VALUE, true);
                blackMarketShardPouch = config.getOrElse(BLACK_MARKET_SHARD_POUCH_CONFIG_VALUE, true);
                scavengerInventoryCount = config.getOrElse(SCAVENGER_INV_COUNT_CONFIG_VALUE, false);

                config.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        loadConfig();
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

        boolean shouldApply;

        if(VAULT_ENCHANTER_EMERALD_SLOT_CONDITIONS.containsKey(mixinClassName)){
            shouldApply = VAULT_ENCHANTER_EMERALD_SLOT_CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
        } else if(BETTER_DESCRIPTIONS_CONDITIONS.containsKey(mixinClassName)){
            shouldApply = BETTER_DESCRIPTIONS_CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
        } else if(VAULT_MODIFIER_OVERLAYS_CONDITIONS.containsKey(mixinClassName)){
            shouldApply = VAULT_MODIFIER_OVERLAYS_CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
        } else if(VAULT_KEYBINDS_CONDITIONS.containsKey(mixinClassName)){
            shouldApply = VAULT_KEYBINDS_CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
        } else if(BETTER_ABILITIES_TAB_CONDITIONS.containsKey(mixinClassName)){
            shouldApply = BETTER_ABILITIES_TAB_CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
        } else if(BLACK_MARKET_CONDITIONS.containsKey(mixinClassName)){
            shouldApply = BLACK_MARKET_CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
        } else if(SCAVENGER_INVENTORY_CONDITIONS.containsKey(mixinClassName)){
            shouldApply = SCAVENGER_INVENTORY_CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
        } else {
            shouldApply = TRUE.get();
        }
        QOLHunters.LOGGER.info("QOLHunters: shouldApplyMixin: " + mixinClassName + " -> " + shouldApply);

        return shouldApply;
    }

    // Boilerplate

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
