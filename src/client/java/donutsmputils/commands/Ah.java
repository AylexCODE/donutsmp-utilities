package donutsmputils.commands;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import donutsmputils.components.Auction.AhScreen;
import donutsmputils.utils.AuctionData;
import donutsmputils.utils.ConfigManager;
import donutsmputils.utils.RequestData;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Ah {
    public static int ah(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        if(ConfigManager.INSTANCE.apikey.trim().isEmpty()){
            MinecraftClient.getInstance().player.sendMessage(Text.of("No API Key Found for DonutSMP.\n/dsmpu apikey [your apikey]"), false);
            return 1;
        }else if(ConfigManager.INSTANCE.username.trim().isEmpty()){
            MinecraftClient.getInstance().player.sendMessage(Text.of("No Username Found for DonutSMP.\n/dsmpu username [your username]"), false);
            return 1;
        }
        
        AhScreen ah = new AhScreen();
        // MinecraftClient.getInstance().player.sendMessage(Text.of("Getting Your Action House Data..."), false);
        MinecraftClient client = context.getSource().getClient();
        client.send(() -> client.setScreen(ah));

        CompletableFuture.runAsync(() -> {
            try {
                ArrayList<AuctionData> data = RequestData.getAuctionData("", "", false);
                // ArrayList<String> data = RequestData.getAuctionData("", "", false);

                // MinecraftClient client = MinecraftClient.getInstance();
                // client.execute(() -> {
                //     if(client != null){
                //         for(String item : data){
                //             client.player.sendMessage(Text.of(item), false);
                //         }
                //     }
                // });
                ah.setData(data);
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