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

    public  static final File VAULT_MODIFIERS_CONFIG_FILE_CUSTOM = new File(FMLPaths.CONFIGDIR.get().resolve("the_vault/vault_modifiers_iridium.json").toString());

    public static void buildConfig() {

        ResourceLocation customResource = new ResourceLocation("qolhunters", isWoldsVaultModInstalled() ? "wolds_modifiers.json" : "vault_modifiers.json");


        if (VAULT_MODIFIERS_CONFIG_FILE_CUSTOM.exists()) {
            VAULT_MODIFIERS_CONFIG_FILE_CUSTOM.delete();
        }

        VAULT_MODIFIERS_CONFIG_FILE_CUSTOM.getParentFile().mkdirs();

        try (InputStream inputStream = Minecraft.getInstance().getResourceManager().getResource(customResource).getInputStream()) {
            Files.copy(inputStream, VAULT_MODIFIERS_CONFIG_FILE_CUSTOM.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            LOGGER.error("Failed to create new file", e);
        }


    }


    public static boolean isWoldsVaultModInstalled() {
        return ModList.get().isLoaded("woldsvaults");
    }


}