package donutsmputils.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("donutsmputilities.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static UtilityConfig INSTANCE = new UtilityConfig();

    public static void load() {
        if(!Files.exists(CONFIG_PATH)){
            save();
            return;
        }

        try{
            INSTANCE = GSON.fromJson(Files.newBufferedReader(CONFIG_PATH), UtilityConfig.class);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void save() {
        try{
            Files.writeString(CONFIG_PATH, GSON.toJson(INSTANCE));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}