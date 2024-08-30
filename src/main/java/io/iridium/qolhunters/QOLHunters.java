package io.iridium.qolhunters;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod(QOLHunters.MOD_ID)
public class QOLHunters {

    public static final String MOD_ID = "qolhunters";
    public static final Logger LOGGER = LogUtils.getLogger();

    public QOLHunters() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(EventPriority.HIGHEST, this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void clientSetup(final FMLClientSetupEvent event) {
    }

    private void commonSetup(final FMLCommonSetupEvent event) {


        ResourceLocation resourceLocation;

        boolean isWoldsVaultModInstalled = ModList.get().isLoaded("woldsvaults");

        if(isWoldsVaultModInstalled){
            LOGGER.info("QOLHUNTERS: Wold's Vault Mod is installed");
            resourceLocation = new ResourceLocation("qolhunters", "wolds_modifiers.json");
            LOGGER.info(resourceLocation.toString());
        }else{
            resourceLocation = new ResourceLocation("qolhunters", "vault_modifiers.json");
        }



        ConfigBuilder.buildConfig(resourceLocation);
    }


}




