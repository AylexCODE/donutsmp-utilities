package donutsmputils.utils;

import donutsmputils.commands.DSMPU;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class ModRegistries {
    public static void registerCommands(){
        ClientCommandRegistrationCallback.EVENT.register(DSMPU::register);
    }
}
