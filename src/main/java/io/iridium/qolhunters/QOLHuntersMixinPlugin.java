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
    private static boolean vaultInterfaceKeybinds = true;
    private static boolean vaultEnchanterEmeraldSlot = true;
    private static boolean betterAbilitiesTab = true;

    private static boolean isWoldsVaultModInstalled = false;


    private static final Map<String, Map<String, Supplier<Boolean>>> MIXIN_CONDITIONS = ImmutableMap.of(
            //"BETTER_DESCRIPTIONS", ImmutableMap.of(
                 //   "io.iridium.qolhunters.mixin.betterdescriptions.MixinAbilitiesDescriptionsConfig", () -> betterDescriptions && !isWoldsVaultModInstalled,
//                    "io.iridium.qolhunters.mixin.betterdescriptions.MixinMenuPlayerStatDescriptionConfig", () -> betterDescriptions && !isWoldsVaultModInstalled,
//                    "io.iridium.qolhunters.mixin.betterdescriptions.MixinSkillDescriptionsConfig", () -> betterDescriptions && !isWoldsVaultModInstalled,
//                    "io.iridium.qolhunters.mixin.betterdescriptions.MixinBingoConfig", () -> betterDescriptions && !isWoldsVaultModInstalled
           // ),
//            "VAULT_ENCHANTER_EMERALD_SLOT", ImmutableMap.of(
//                    "io.iridium.qolhunters.mixin.vaultenchanter.MixinEnchantmentCost", () -> vaultEnchanterEmeraldSlot && !vanillaSafeMode,
//                    "io.iridium.qolhunters.mixin.vaultenchanter.MixinVaultEnchanterBlock", () -> vaultEnchanterEmeraldSlot && !vanillaSafeMode,
//                    "io.iridium.qolhunters.mixin.vaultenchanter.MixinVaultEnchanterContainer", () -> vaultEnchanterEmeraldSlot && !vanillaSafeMode,
//                    "io.iridium.qolhunters.mixin.vaultenchanter.MixinVaultEnchanterScreen", () -> vaultEnchanterEmeraldSlot && !vanillaSafeMode,
//                    "io.iridium.qolhunters.mixin.vaultenchanter.MixinVaultEnchanterTileEntity", () -> vaultEnchanterEmeraldSlot && !vanillaSafeMode
//            ),
//            "VAULT_KEYBINDS", ImmutableMap.of(
//                    "io.iridium.qolhunters.mixin.keybinds.MixinBountyScreen", () -> vaultInterfaceKeybinds,
//                    "io.iridium.qolhunters.mixin.keybinds.MixinForgeRecipeContainerScreen", () -> vaultInterfaceKeybinds,
//                    "io.iridium.qolhunters.mixin.keybinds.MixinModifierWorkbenchScreen", () -> vaultInterfaceKeybinds,
//                    "io.iridium.qolhunters.mixin.keybinds.MixinVaultEnchanterScreen", () -> vaultInterfaceKeybinds
//            ),
//            "BETTER_ABILITIES_TAB", ImmutableMap.of(
//                    "io.iridium.qolhunters.mixin.abilities.MixinAbilityDialog", () -> betterAbilitiesTab
//            )
    );



    private static String ConfigPathBuilder(String... path){
        return String.join(".", path);
    }


    private static final String CONFIG_FILE_NAME = "qolhunters-client.toml";
//    private static final String VANILLA_SAFE_MODE_CONFIG_VALUE = ConfigPathBuilder(ConfigPaths.Group.GENERAL_GROUP, ConfigPaths.VANILLA_SAFE_MODE);
//    private static final String BETTER_DESCRIPTIONS_CONFIG_VALUE = ConfigPathBuilder(ConfigPaths.Group.CLIENT_GROUP, ConfigPaths.BETTER_DESCRIPTIONS);
//    private static final String VAULT_INTERFACE_KEYBINDS_CONFIG_VALUE = ConfigPathBuilder(ConfigPaths.Group.CLIENT_GROUP, ConfigPaths.VAULT_INTERFACE_KEYBINDS);
//    private static final String VAULT_ENCHANTER_EMERALD_SLOT_CONFIG_VALUE = ConfigPathBuilder(ConfigPaths.Group.CLIENT_SERVER_GROUP, ConfigPaths.VAULT_ENCHANTER_EMERALDS_SLOT);
//    private static final String BETTER_ABILITIES_TAB_CONFIG_VALUE = ConfigPathBuilder(ConfigPaths.Group.CLIENT_GROUP, ConfigPaths.BETTER_ABILITIES_TAB);


    private static void loadConfig(){

        isWoldsVaultModInstalled = FMLLoader.getLoadingModList().getModFileById("woldsvaults") != null;

        try {
            Path configPath = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE_NAME);
            File configFile = configPath.toFile();
            if (configFile.exists()) {
                FileConfig config = FileConfig.of(configFile);
                config.load();
//                vanillaSafeMode = config.getOrElse(VANILLA_SAFE_MODE_CONFIG_VALUE, false);
               // betterDescriptions = config.getOrElse(BETTER_DESCRIPTIONS_CONFIG_VALUE, true);
//                vaultInterfaceKeybinds = config.getOrElse(VAULT_INTERFACE_KEYBINDS_CONFIG_VALUE, true);
//                vaultEnchanterEmeraldSlot = config.getOrElse(VAULT_ENCHANTER_EMERALD_SLOT_CONFIG_VALUE, true);
//                betterAbilitiesTab = config.getOrElse(BETTER_ABILITIES_TAB_CONFIG_VALUE, true);

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
        for (Map<String, Supplier<Boolean>> conditions : MIXIN_CONDITIONS.values()) {
            if (conditions.containsKey(mixinClassName)) {
                boolean shouldApply = conditions.get(mixinClassName).get();
                return shouldApply;
            }
        }
        return true;
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
