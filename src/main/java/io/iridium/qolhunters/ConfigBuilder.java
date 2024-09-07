package io.iridium.qolhunters;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static io.iridium.qolhunters.QOLHunters.LOGGER;
@OnlyIn(Dist.CLIENT)
public class ConfigBuilder {

    public  static final File VAULT_MODIFIERS_CONFIG_FILE_CUSTOM = new File(FMLPaths.CONFIGDIR.get().resolve("the_vault/iridium/vault_modifiers.json").toString());
    public static final File ABILITIES_DESCRIPTIONS_CUSTOM = new File(FMLPaths.CONFIGDIR.get().resolve("the_vault/iridium/abilities_descriptions.json").toString());
    public static final File MENU_PLAYER_STAT_DESCRIPTION_CUSTOM = new File(FMLPaths.CONFIGDIR.get().resolve("the_vault/iridium/menu_player_stat_description.json").toString());
    public static final File SKILL_DESCRIPTIONS_CUSTOM = new File(FMLPaths.CONFIGDIR.get().resolve("the_vault/iridium/skill_descriptions.json").toString());
    public static final File BINGO_CONFIG_CUSTOM = new File(FMLPaths.CONFIGDIR.get().resolve("the_vault/iridium/bingo.json").toString());



    public static void buildConfigs() {

        LOGGER.info("QOLHunters: Building custom configs!");
        buildModifiersConfigFile();
        buildBetterDescriptionsConfigFiles();

    }


    public static void buildBetterDescriptionsConfigFiles(){
        if (isWoldsVaultModInstalled()) return;
        copyResourceToFile("betterconfigs/abilities_descriptions.json", ABILITIES_DESCRIPTIONS_CUSTOM);
        copyResourceToFile("betterconfigs/menu_player_stat_description.json", MENU_PLAYER_STAT_DESCRIPTION_CUSTOM);
        copyResourceToFile("betterconfigs/skill_descriptions.json", SKILL_DESCRIPTIONS_CUSTOM);
        copyResourceToFile("betterconfigs/bingo.json", BINGO_CONFIG_CUSTOM);
    }



    public static void buildModifiersConfigFile() {
        String resourceName = isWoldsVaultModInstalled() ? "wolds_modifiers.json" : "vault_modifiers.json";
        copyResourceToFile(resourceName, VAULT_MODIFIERS_CONFIG_FILE_CUSTOM);
    }


    public static boolean isWoldsVaultModInstalled() {
        boolean isWoldsVaultModInstalled = ModList.get().isLoaded("woldsvaults");
        LOGGER.info("QOLHunters: WoldsVaults mod is installed: " + isWoldsVaultModInstalled);
        return isWoldsVaultModInstalled;
    }



    private static void copyResourceToFile(String resourceName, File targetFile) {

        LOGGER.info("QOLHunters: Copying resource {} to file {}", resourceName, targetFile);

        ResourceLocation resource = new ResourceLocation("qolhunters", resourceName);

        if (targetFile.exists()) {
            targetFile.delete();
        }

        targetFile.getParentFile().mkdirs();

        try (InputStream inputStream = Minecraft.getInstance().getResourceManager().getResource(resource).getInputStream()) {
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            LOGGER.error("QOLHunters: failed to create new file", e);
        }
    }


}