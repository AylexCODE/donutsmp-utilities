package donutsmputils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class DSMPU {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
        dispatcher.register(CommandManager.literal("dsmpu")
            .executes(DSMPU::run)
            .then(CommandManager.literal("set").executes(Auction::ah)));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(() -> Text.literal("Insufficient Command Args..."), true);
        return 1;
    }
}
