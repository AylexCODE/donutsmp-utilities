package donutsmputils.commands;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class Auction {
    public static int ah(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        context.getSource().sendFeedback(Text.literal("Getting Data..."));
        MinecraftClient.getInstance().player.sendMessage(Text.of("Getting Data..."), false);
        CompletableFuture.runAsync(() -> {
            try {
                ArrayList<String> data = getAuctionData();

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

    public static ArrayList<String> getAuctionData() {
        ArrayList<String> result = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.donutsmp.net/v1/auction/list/1"))
            .header("Authorization", "")
            .POST(HttpRequest.BodyPublishers.ofString("{\"search\": \"\"}"))
            .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // System.err.println(response.body());
            Gson gson = new Gson();
            if (response.statusCode() == 200) {
                Response rs = gson.fromJson(response.body(), Response.class);
                NumberFormat priceFormat = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
                int i = 1;
                for(ResponseResult item : rs.getResult()){
                    if(item != null){
                        String itemString = "";
                        String displayName = toTitleCase(item.getItem().getId().split(":")[1].replaceAll("_", " "));
                        String price = priceFormat.format(item.getPrice());
                        itemString += ("[" +item.getItem().getCount() +"] " +displayName +" | " +price +" - (" +(i++) +")");
                        result.add(itemString);
                    }
                }
            }else{
                // System.err.println("API Call Failed with status code: " + response.statusCode());
                // System.err.println(response.body());
                result.add("No Items To Display...");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    static class Response {
        private int status;
        private List<ResponseResult> result;

        public int getStatus(){
            return status;
        }

        public List<ResponseResult> getResult(){
            return result;
        }
    }

    static class ResponseResult {
        // private Seller seller;
        // @SerializedName("time_left")
        private long price;
        // private long timeLeft;
        private Item item;

        public Item getItem(){
            return item;
        }

        public long getPrice(){
            return price;
        }
    }

    // static class Seller {
    //     private String name;
    //     private String uuid;
    // }

    static class Item {
        private String id;
        private int count;
        @SerializedName("display_name")
        private String displayName;
        // private List<String> lore;
        // private Enchants enchants;
        // private Object contents;

        public String getDisplayName(){
            return displayName;
        }

        public String getId(){
            return id;
        }

        public int getCount(){
            return count;
        }
    }

    // static class Enchants {
    //     private EnchantmentLevels enchantments;
    //     private Trim trim;
    // }

    // class EnchantmentLevels {
    //     private Map<String, Integer> levels;
    // }

    // static class Trim {
    //     private String material;
    //     private String pattern;
    // }

    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        StringBuilder titleCase = new StringBuilder();
        boolean nextCharShouldBeCapital = true;

        for (char ch : input.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                nextCharShouldBeCapital = true;
                titleCase.append(ch);
            } else if (nextCharShouldBeCapital) {
                titleCase.append(Character.toTitleCase(ch));
                nextCharShouldBeCapital = false;
            } else {
                titleCase.append(Character.toLowerCase(ch));
            }
        }

        return titleCase.toString();
    }
}