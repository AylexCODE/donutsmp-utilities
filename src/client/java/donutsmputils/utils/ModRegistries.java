package donutsmputils.utils;

import donutsmputils.commands.CommandSample;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModRegistries {
    public static void registerCommands(){
        CommandRegistrationCallback.EVENT.register(CommandSample::register);
    }
}
