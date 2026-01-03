package donutsmputils.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import donutsmputils.utils.ConfigManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class SetConfig {
    public static int setAPIKey(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String apikey = StringArgumentType.getString(context, "API Key");
        ConfigManager.INSTANCE.apikey = apikey;
        ConfigManager.save();
        context.getSource().sendFeedback(Text.literal("API Key set, you can now use auction commands."));
        return 1;
    }

    public static int setUsername(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String username = StringArgumentType.getString(context, "Username");
        ConfigManager.INSTANCE.username = username;
        ConfigManager.save();
        context.getSource().sendFeedback(Text.literal("Username set, you can now use /dsmpu ah command."));
        return 1;
    }
}
