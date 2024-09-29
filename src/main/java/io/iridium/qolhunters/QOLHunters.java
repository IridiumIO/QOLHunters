package io.iridium.qolhunters;

import com.mojang.logging.LogUtils;
import io.iridium.qolhunters.config.DehammerizerConfig;
import io.iridium.qolhunters.config.QOLHuntersClientConfigs;
import io.iridium.qolhunters.config.SkillAltarConfig;
import io.iridium.qolhunters.features.treasuredoors.TreasureDoorTileEntityRenderer;
import io.iridium.qolhunters.networking.ModMessages;
import io.iridium.qolhunters.util.KeyBindings;
import iskallia.vault.init.ModBlocks;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.slf4j.Logger;


@Mod(QOLHunters.MOD_ID)
public class QOLHunters {

    public static final String MOD_ID = "qolhunters";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static SkillAltarConfig SKILL_ALTAR_CONFIG;
    public static DehammerizerConfig DEHAMMERIZER_CONFIG;

    public QOLHunters() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, QOLHuntersClientConfigs.CLIENT_SPEC, "qolhunters-client.toml");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        SKILL_ALTAR_CONFIG = SkillAltarConfig.load();
        DEHAMMERIZER_CONFIG = DehammerizerConfig.load();
        BlockEntityRenderers.register(ModBlocks.TREASURE_DOOR_TILE_ENTITY, TreasureDoorTileEntityRenderer::new);
        KeyBindings.init();

        ArtifactVersion vaultModVersion = getModVersion("the_vault");
        checkIfVaultVersionIsTooNew(vaultModVersion);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);
    }


    public enum ModMode {
        CLIENTONLY,
        CLIENTANDSERVER,
        SERVERONLY
    }



    public static ArtifactVersion getModVersion(String modId) {
        if (ModList.get().isLoaded(modId)) {
            ModInfo modInfo = (ModInfo) ModList.get().getModContainerById(modId).get().getModInfo();
            return modInfo.getVersion();
        } else {
            return null;
        }
    }


    private static void checkIfVaultVersionIsTooNew(ArtifactVersion CurrentVaultVersion) {
        ArtifactVersion WorkingVersion = new DefaultArtifactVersion("1.18.2-3.15.1.3336");

        QOLHunters.LOGGER.info("QOLHunters: The Vault Installed version" + CurrentVaultVersion);
        QOLHunters.LOGGER.info("QOLHunters: The Vault Working version" + WorkingVersion);

       if(CurrentVaultVersion.compareTo(WorkingVersion) > 0){
           QOLHunters.LOGGER.warn("Your Vault Hunters mod version is too new! Some features will be disabled to prevent crashes.");
           QOLHuntersClientConfigs.ENABLE_VIRTUAL_DEHAMMERIZER.set(false);
       }

    }

}




