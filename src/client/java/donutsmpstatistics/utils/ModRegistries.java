package donutsmpstatistics.utils;

import donutsmpstatistics.commands.DSMPU;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class ModRegistries {
    public static void registerCommands(){
        ClientCommandRegistrationCallback.EVENT.register(DSMPU::register);
    }
}
