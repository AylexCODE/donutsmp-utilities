package donutsmputils.commands;

import donutsmputils.utils.ConfigManager;

public class SetConfig {
    public void setAPIKey(String apikey){
        ConfigManager.INSTANCE.apikey = apikey;
        ConfigManager.save();
    }

    public void setUsername(String username){
        ConfigManager.INSTANCE.username = username;
        ConfigManager.save();
    }
}
