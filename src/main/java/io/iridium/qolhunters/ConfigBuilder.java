package io.iridium.qolhunters;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigBuilder {

    public static void buildConfig(ResourceLocation customResource) {
        File location = new File(Minecraft.getInstance().gameDirectory + "/config/the_vault/vault_modifiers_iridium.json");

        if (location.exists()) return;

        location.getParentFile().mkdirs();
        try {
            location.createNewFile();

            InputStream inputStream = Minecraft.getInstance().getResourceManager().getResource(customResource).getInputStream();

            Files.copy(inputStream, location.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            QOLHunters.LOGGER.error("Failed to create new file", e);
        }


    }
}