package donutsmputils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class DSMPU {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess){
        dispatcher.register(ClientCommandManager.literal("dsmpu")
            .executes(DSMPU::run)
            .then(ClientCommandManager.literal("ah")
            .executes(Ah::ah))
            .then(ClientCommandManager.literal("auction")
            .then(ClientCommandManager.argument("search", StringArgumentType.greedyString())
            .executes(Auction::auction)))
            .then(ClientCommandManager.literal("apikey")
            .then(ClientCommandManager.argument("API Key", StringArgumentType.string())
            .executes(SetConfig::setAPIKey)))
            .then(ClientCommandManager.literal("username")
            .then(ClientCommandManager.argument("Username", StringArgumentType.string())
            .executes(SetConfig::setUsername)))
        );
    }

    public static int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(Text.literal("Insufficient Command Args..."));
        return 1;
    }
}
