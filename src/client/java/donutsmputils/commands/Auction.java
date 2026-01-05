package donutsmputils.commands;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import donutsmputils.components.Auctions.AuctionScreen;
import donutsmputils.components.Settings.ConfigScreen;
import donutsmputils.utils.AuctionData;
import donutsmputils.utils.ConfigManager;
import donutsmputils.utils.RequestData;
import donutsmputils.utils.RequestData.ResponseObject;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class Auction {
    public static int auction(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        MinecraftClient client = context.getSource().getClient();
        if(ConfigManager.INSTANCE.apikey.isBlank()){
            client.send(() -> client.setScreen(new ConfigScreen(true, false)));
            return 0;
        }

        AuctionScreen auctionScreen = new AuctionScreen();
        client.send(() -> client.setScreen(auctionScreen));
        CompletableFuture.runAsync(() -> {
            try {
                ResponseObject data = RequestData.getAuctionData("", "",false ,false);
                auctionScreen.setData(data.getResponse(), data.getStatus());
            }catch(Exception e){
                auctionScreen.setData(new ArrayList<AuctionData>(), "Error ah");
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

        return 1;
    }
}