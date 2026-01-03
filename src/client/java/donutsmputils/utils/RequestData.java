package donutsmputils.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class RequestData {
    public static ArrayList<String> getAuctionData(String page, String search, boolean isSearching) {
        ArrayList<String> result = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;

        if(isSearching){
            request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.donutsmp.net/v1/auction/list/" +page))
                .header("Authorization", "Bearer " +ConfigManager.INSTANCE.apikey)
                .POST(HttpRequest.BodyPublishers.ofString("{\"search\": \"" +search +"\"}"))
                .build();
        }else{
            request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.donutsmp.net/v1/auction/list/1"))
                .header("Authorization", "Bearer " +ConfigManager.INSTANCE.apikey)
                .POST(HttpRequest.BodyPublishers.ofString("{\"search\": \"" +ConfigManager.INSTANCE.username +"\"}"))
                .build();
        }

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
                if(response.statusCode() == 500){
                    result.add("No Items To Display...");
                }else if(response.statusCode() == 401){
                    result.add("Error Invalid API KEY");
                }
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
