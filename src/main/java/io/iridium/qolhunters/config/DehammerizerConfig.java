package io.iridium.qolhunters.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DehammerizerConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FMLPaths.CONFIGDIR.get().resolve("qolhunters-dehammerizer.json").toFile();

    public Map<String, Map<Integer,Coordinates >> COORDINATES = new HashMap<>();

    public static class Coordinates {
        public int x;
        public int y;
        public int z;

        public Coordinates(int x, int y, int z){
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static DehammerizerConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, DehammerizerConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
                return new DehammerizerConfig();
            }
        }
        return new DehammerizerConfig();
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String getSaveName(){

        if(Minecraft.getInstance().hasSingleplayerServer()){
            return Objects.requireNonNull(Objects.requireNonNull(Minecraft.getInstance().getSingleplayerServer()).getWorldData().getLevelName());
        }else {
            return Objects.requireNonNull(Minecraft.getInstance().getCurrentServer()).name + "_" + Minecraft.getInstance().getCurrentServer().ip;
        }
    }



}
