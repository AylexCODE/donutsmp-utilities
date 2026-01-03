package donutsmputils.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import donutsmputils.components.AuctionScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class Auction {
    public static int ah(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        MinecraftClient.getInstance().setScreen(
            new AuctionScreen(Text.literal("Hello World!"))
        );
        return 1;
    }
}
