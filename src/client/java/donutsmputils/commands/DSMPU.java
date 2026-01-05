package donutsmputils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class DSMPU {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess){
        dispatcher.register(ClientCommandManager.literal("dsmp")
            .executes(DSMPU::run)
            .then(ClientCommandManager.literal("ah")
                .executes(AuctionHouse::auction)
                .then(ClientCommandManager.literal("profile")
                    .executes(AuctionHouse::profile)
                )
                .then(ClientCommandManager.argument("Search", StringArgumentType.string())
                    .executes(AuctionHouse::search)
                )
                .then(ClientCommandManager.argument("Page", IntegerArgumentType.integer(1))
                    .then(ClientCommandManager.argument("Search", StringArgumentType.greedyString())
                        .executes(AuctionHouse::searchWPage)
                    )
                )
            )
            .then(ClientCommandManager.literal("auction")
                .executes(AuctionHouse::auction)
                .then(ClientCommandManager.argument("Search", StringArgumentType.string())
                    .executes(AuctionHouse::search)
                )
                .then(ClientCommandManager.argument("Page", IntegerArgumentType.integer(1))
                    .then(ClientCommandManager.argument("Search", StringArgumentType.greedyString())
                        .executes(AuctionHouse::searchWPage)
                    )
                )
                .then(ClientCommandManager.literal("profile")
                    .executes(AuctionHouse::profile)
                )
            )
        );
    }

    public static int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(Text.literal("Insufficient Command Args..."));
        return 1;
    }
}
