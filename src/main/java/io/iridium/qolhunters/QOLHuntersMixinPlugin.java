package io.iridium.qolhunters;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.google.common.collect.ImmutableMap;
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
    private static final Supplier<Boolean> TRUE = () -> true;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "io.iridium.qolhunters.mixin.vaultenchanter.MixinEnchantmentCost", () -> !QOLHuntersMixinPlugin.vanillaSafeMode,
            "io.iridium.qolhunters.mixin.vaultenchanter.MixinVaultEnchanterBlock", () -> !QOLHuntersMixinPlugin.vanillaSafeMode,
            "io.iridium.qolhunters.mixin.vaultenchanter.MixinVaultEnchanterContainer", () -> !QOLHuntersMixinPlugin.vanillaSafeMode,
            "io.iridium.qolhunters.mixin.vaultenchanter.MixinVaultEnchanterScreen", () -> !QOLHuntersMixinPlugin.vanillaSafeMode,
            "io.iridium.qolhunters.mixin.vaultenchanter.MixinVaultEnchanterTileEntity", () -> !QOLHuntersMixinPlugin.vanillaSafeMode
    );


    private static boolean vanillaSafeMode = false;

    private static void loadConfig(){
        try {
            Path configPath = FMLPaths.CONFIGDIR.get().resolve("qolhunters-client.toml");
            File configFile = configPath.toFile();
            if (configFile.exists()) {
                FileConfig config = FileConfig.of(configFile);
                config.load();
                vanillaSafeMode = config.getOrElse("Configs for QOLHunters.Vanilla Safe Mode", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        loadConfig();
        QOLHunters.LOGGER.info("QOLHUNTERS: Vanilla Safe Mode is {}", vanillaSafeMode);
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        boolean shouldApply = CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
        QOLHunters.LOGGER.info("QOLHUNTERS: Mixin {} should be applied to target {}: {}", mixinClassName, targetClassName, shouldApply);
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
