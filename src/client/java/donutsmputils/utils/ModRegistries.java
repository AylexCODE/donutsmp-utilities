package donutsmputils.utils;

import donutsmputils.commands.DSMPU;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModRegistries {
    public static void registerCommands(){
        CommandRegistrationCallback.EVENT.register(DSMPU::register);
    }
}
