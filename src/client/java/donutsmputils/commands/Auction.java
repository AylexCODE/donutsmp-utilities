package donutsmputils.commands;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import donutsmputils.utils.ConfigManager;
import donutsmputils.utils.RequestData;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Auction {
    public static int auction(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        if(ConfigManager.INSTANCE.apikey.trim().isEmpty()){
            MinecraftClient.getInstance().player.sendMessage(Text.of("No API Key Found for DonutSMP.\n/dsmpu apikey [your apikey]"), false);
            return 0;
        }

        MinecraftClient.getInstance().player.sendMessage(Text.of("Getting Auction House Data..."), false);
        CompletableFuture.runAsync(() -> {
            try {
                ArrayList<String> data = RequestData.getAuctionData(StringArgumentType.getString(context, "Page"), StringArgumentType.getString(context, "Search"), true);

                MinecraftClient client = MinecraftClient.getInstance();
                client.execute(() -> {
                    if(client != null){
                        for(String item : data){
                            client.player.sendMessage(Text.of(item), false);
                        }
                    }
                });
            }catch(Exception e){
                if(MinecraftClient.getInstance() != null) MinecraftClient.getInstance().player.sendMessage(Text.of("An error occurred: " + e.getMessage()), false);
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

        return 1;
    }
}
