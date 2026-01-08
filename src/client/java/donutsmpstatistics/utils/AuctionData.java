package donutsmpstatistics.utils;

import net.minecraft.item.ItemStack;

public class AuctionData {
    private ItemStack itemData;
    private String seller;
    private String price;
    private String timeLeft;

    public AuctionData(ItemStack itemData, String price, String time, String seller){
        this.itemData = itemData;
        this.seller = seller;
        this.price = price;
        this.timeLeft = time;
    }

    public ItemStack getItemData(){
        return itemData;
    }

    public String getItemSeller(){
        return seller;
    }

    public String getItemPrice(){
        return price;
    }

    public String getItemTimeLeft(){
        return timeLeft;
    }
}

