package donutsmpstatistics.commands;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import donutsmpstatistics.components.Auctions.AuctionHouseProfileScreen;
import donutsmpstatistics.components.Auctions.AuctionHouseScreen;
import donutsmpstatistics.components.Settings.AuctionPromptScreen;
import donutsmpstatistics.utils.AuctionData;
import donutsmpstatistics.utils.ConfigManager;
import donutsmpstatistics.utils.RequestData;
import donutsmpstatistics.utils.RequestData.ResponseObject;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class AuctionHouse {
    public static int handleCommand(CommandContext<FabricClientCommandSource> context, String branch) throws CommandSyntaxException {
        MinecraftClient client = context.getSource().getClient();
        if((ConfigManager.INSTANCE.apikey.isBlank() && ConfigManager.INSTANCE.username.isBlank()) && branch == "profile"){
            client.send(() -> client.setScreen(new AuctionPromptScreen(true, true)));
            return 1;
        }else if(ConfigManager.INSTANCE.apikey.isBlank()){
            client.send(() -> client.setScreen(new AuctionPromptScreen(true, false)));
            return 1;
        }else if(ConfigManager.INSTANCE.username.isBlank() && branch == "profile"){
            client.send(() -> client.setScreen(new AuctionPromptScreen(false, true)));
            return 1;
        }
        
        if(branch == "profile"){
            AuctionHouseProfileScreen auctionHouseProfileScreen = new AuctionHouseProfileScreen();
            client.send(() -> client.setScreen(auctionHouseProfileScreen));

            CompletableFuture.runAsync(() -> {
                try{
                    ResponseObject data = RequestData.getAuctionData(1, "", true);
                    auctionHouseProfileScreen.setData(data.getResponse(), data.getStatus());
                }catch(Exception e){
                    auctionHouseProfileScreen.setData(new ArrayList<AuctionData>(), "Error Auction");
                }
            }).exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
        }else{
            AuctionHouseScreen auctionHouseScreen = new AuctionHouseScreen();
            client.send(() -> client.setScreen(auctionHouseScreen));

            CompletableFuture.runAsync(() -> {
                try{
                    if(branch == "search"){
                        ResponseObject data = RequestData.getAuctionData(1, StringArgumentType.getString(context, "Search"), false);
                        auctionHouseScreen.setData(data.getResponse(), data.getStatus(), StringArgumentType.getString(context, "Search"), 1);
                    }else if(branch == "searchWPage"){
                        ResponseObject data = RequestData.getAuctionData(IntegerArgumentType.getInteger(context, "Page"), StringArgumentType.getString(context, "Search"), false);
                        auctionHouseScreen.setData(data.getResponse(), data.getStatus(), StringArgumentType.getString(context, "Search"), IntegerArgumentType.getInteger(context, "Page"));
                    }else{
                        ResponseObject data = RequestData.getAuctionData(1, "", false);
                        auctionHouseScreen.setData(data.getResponse(), data.getStatus(), "", 1);
                    }
                }catch(Exception e){
                    System.out.println(e);
                    auctionHouseScreen.setData(new ArrayList<AuctionData>(), "Error Auction", "", 0);
                }
            }).exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });
        }
        return 1;
    }

    public static int auction(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        handleCommand(context, "none");
        return 1;
    }

    public static int profile(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        handleCommand(context, "profile");
        return 1;
    }

    public static int search(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        handleCommand(context, "search");
        return 1;
    }

    public static int searchWPage(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        handleCommand(context, "searchWPage");
        return 1;
    }
}