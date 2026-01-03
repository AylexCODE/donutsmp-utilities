package donutsmputils.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import donutsmputils.utils.ConfigManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Auction {
    public static int auction(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        if(ConfigManager.INSTANCE.apikey.trim().isEmpty()){
            MinecraftClient.getInstance().player.sendMessage(Text.of("No API Key Found for DonutSMP.\n/dsmpu apikey [your apikey]"), false);
            return 0;
        }

        String message = StringArgumentType.getString(context, "search");
                        
        context.getSource().sendFeedback(Text.literal("Client Echo: " + message));
        return 1;
    }
}
