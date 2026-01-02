package donutsmputils.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class CommandSample {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment){
        dispatcher.register(CommandManager.literal("home")
            .executes(CommandSample::run)
            .then(CommandManager.literal("set").executes(CommandSample::setHome)));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(() -> Text.literal("Hello World!"), true);
        return 1;
    }

    public static int setHome(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        BlockPos playerPos = context.getSource().getPlayer().getBlockPos();
        String pos = playerPos.getX() + ", " + playerPos.getY() + ", " + playerPos.getZ();

        context.getSource().sendFeedback(() -> Text.literal("Set Home! " + pos), true);
        return 1;
    }
}
