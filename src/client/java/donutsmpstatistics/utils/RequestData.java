package donutsmpstatistics.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class RequestData {
    public static ResponseObject getAuctionData(int page, String search, boolean isProfile) {
        ArrayList<AuctionData> result = new ArrayList<>(); String status;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;

        if(isProfile){
            request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.donutsmp.net/v1/auction/list/1"))
                .header("Authorization", "Bearer " +ConfigManager.INSTANCE.apikey)
                .POST(HttpRequest.BodyPublishers.ofString("{\"search\": \"" +ConfigManager.INSTANCE.username +"\"}"))
                .build();
        }else{
            request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.donutsmp.net/v1/auction/list/" +page))
                .header("Authorization", "Bearer " +ConfigManager.INSTANCE.apikey)
                .POST(HttpRequest.BodyPublishers.ofString("{\"search\": \"" +search +"\"}"))
                .build();
        }

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // System.err.println(response.body());
            Gson gson = new Gson();
            if (response.statusCode() == 200) {
                Response rs = gson.fromJson(response.body(), Response.class);
                NumberFormat priceFormat = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
                for(ResponseResult item : rs.getResult()){
                    if(item != null){
                        String itemId = item.getItem().getId();
                        String price = priceFormat.format(item.getPrice());
                        long timeLeft = item.getTimeLeft();

                        long hours = TimeUnit.MILLISECONDS.toHours(timeLeft);
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft) % 60;
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft) % 60;

                        String time = String.format("%02dh %02dm %02ds", hours, minutes, seconds).toString();

                        result.add(new AuctionData(new ItemStack(Registries.ITEM.get(Identifier.of(itemId)), item.getItem().getCount()), price, time, item.getSeller().getName()));
                    }
                }
                status = "ok";
            }else{
                // System.err.println(response.body());
                if(response.statusCode() == 500){
                    status = "No Items To Display...";
                }else if(response.statusCode() == 401){
                    status = "Error Invalid API Key";
                }else{
                    status = "Error Unknown";
                }
            }
        } catch (IOException | InterruptedException e) {
            status = "Error IO | Interrupted";
            e.printStackTrace();
        }
System.out.println(page);
        return new ResponseObject(result, status, page);
    }

    public static class ResponseObject {
        private ArrayList<AuctionData> response;
        private int page;
        private String status;

        public ResponseObject(ArrayList<AuctionData> response, String status, int page){
            this.response = response;
            this.status = status;
            this.page = page;
        }

        public ArrayList<AuctionData> getResponse(){
            return response;
        }

        public int getPage(){
            return page;
        }

        public String getStatus(){
            return status;
        }
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
        private Seller seller;
        @SerializedName("time_left")
        private long timeLeft;
        private long price;
        private Item item;

        public Item getItem(){
            return item;
        }

        public long getPrice(){
            return price;
        }

        public long getTimeLeft(){
            return timeLeft;
        }

        public Seller getSeller(){
            return seller;
        }
    }

    static class Seller {
        private String name;
        // private String uuid;

        public String getName(){
            return name;
        }
    }

    static class Item {
        private String id;
        private int count;
        // @SerializedName("display_name")
        // private String displayName;
        // private List<String> lore;
        // private Enchants enchants;
        // private Object contents;

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

    // public static String toTitleCase(String input) {
    //     if (input == null || input.isEmpty()) {
    //         return "";
    //     }

    //     StringBuilder titleCase = new StringBuilder();
    //     boolean nextCharShouldBeCapital = true;

    //     for (char ch : input.toCharArray()) {
    //         if (Character.isWhitespace(ch)) {
    //             nextCharShouldBeCapital = true;
    //             titleCase.append(ch);
    //         } else if (nextCharShouldBeCapital) {
    //             titleCase.append(Character.toTitleCase(ch));
    //             nextCharShouldBeCapital = false;
    //         } else {
    //             titleCase.append(Character.toLowerCase(ch));
    //         }
    //     }

    //     return titleCase.toString();
    // }
}
