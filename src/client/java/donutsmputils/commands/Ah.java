package donutsmputils.commands;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import donutsmputils.components.Auctions.AhScreen;
import donutsmputils.components.Settings.ConfigScreen;
import donutsmputils.utils.AuctionData;
import donutsmputils.utils.ConfigManager;
import donutsmputils.utils.RequestData;
import donutsmputils.utils.RequestData.ResponseObject;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class Ah {
    public static int ah(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        MinecraftClient client = context.getSource().getClient();
        if(ConfigManager.INSTANCE.apikey.isBlank() && ConfigManager.INSTANCE.username.isBlank()){
            client.send(() -> client.setScreen(new ConfigScreen(true, true)));
            return 1;
        }else if(ConfigManager.INSTANCE.apikey.isBlank()){
            client.send(() -> client.setScreen(new ConfigScreen(true, false)));
            return 1;
        }else if(ConfigManager.INSTANCE.username.isBlank()){
            client.send(() -> client.setScreen(new ConfigScreen(false, true)));
            return 1;
        }
        
        AhScreen ah = new AhScreen();
        client.send(() -> client.setScreen(ah));

        CompletableFuture.runAsync(() -> {
            try {
                ResponseObject data = RequestData.getAuctionData("", "", false, true);
                ah.setData(data.getResponse(), data.getStatus());
            }catch(Exception e){
                ah.setData(new ArrayList<AuctionData>(), "Error ah");
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

        return 1;
    }
}