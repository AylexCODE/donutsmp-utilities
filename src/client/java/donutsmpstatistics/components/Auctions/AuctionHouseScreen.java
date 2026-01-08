package donutsmpstatistics.components.Auctions;

import java.util.ArrayList;
import java.util.List;

import donutsmpstatistics.utils.AuctionData;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class AuctionHouseScreen extends Screen {
    private static final Identifier TEXTURE = Identifier.of("donutsmp-statistics", "textures/gui/inventory_large.png");
    private ArrayList<AuctionData> data;
    private String status = "";
    private int currentPageNumber = 1;

    public AuctionHouseScreen(){
        super(Text.of("AuctionScreen"));
    }
    
    public void setData(ArrayList<AuctionData> data, String status, int page){
        this.data = data;
        this.status = status;
        this.currentPageNumber = page;
        System.out.println(page);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        int invWidth = 176;
        int invHeight = 130;
        int invX = (this.width - invWidth) / 2;
        int invY = (this.height - invHeight) / 2;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, invX, invY, 0, 0, invWidth, invHeight, invWidth, invHeight);
        
        int y = (this.height - invHeight) / 2 + 17;
        int x = (this.width - invWidth) / 2 + 8;
        int size = 16;
        int slotNumber = 1;

        if(status.isBlank()){
            context.drawText(this.textRenderer, Text.literal("..."), x, y - 11, 0xFF444444, false);
             return;
        }else{
            context.drawText(this.textRenderer, Text.literal(status == "ok" ? ("Auction (Page " +currentPageNumber  +")") : status), x, y - 11, 0xFF444444, false);
        }

        for(AuctionData item : data){
            context.drawItem(item.getItemData(), x, y);
            context.drawStackOverlay(this.textRenderer, item.getItemData(), x, y, null);

            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
                List<Text> customLines = List.of(
                    Text.literal(item.getItemData().getName().getString()).formatted(Formatting.WHITE),
                    Text.literal("Price: ").append(Text.literal(item.getItemPrice()).formatted(Formatting.GREEN)),
                    Text.literal("Seller: ").append(Text.literal(item.getItemSeller()).formatted(Formatting.GREEN)),
                    Text.literal("Time Left: ").append(Text.literal(item.getItemTimeLeft()).formatted(Formatting.GREEN))
                );
                context.drawTooltip(this.textRenderer, customLines, mouseX, mouseY);
                context.fill(x, y, x + size, y + size, 0x70FFFFFF);
            }

            x += 18; slotNumber++;
            if(slotNumber > 9){
                x = (this.width - invWidth) / 2 + 8;
                y += 18;
                slotNumber = 1;
            }
        }

        int nextPageX = ((this.width - invWidth) / 2 + 8) + (18 * 8);
        int prevPageX = ((this.width - invWidth) / 2 + 8);
        int allPageY = ((this.height - invHeight) / 2 + 17) + (18 * 5) - 1;
        context.drawItem(new ItemStack(Items.ARROW), nextPageX, allPageY);
        if(mouseX >= nextPageX && mouseX <= nextPageX + 16 && mouseY >= allPageY && mouseY <= allPageY + 16){
            context.drawTooltip(this.textRenderer, Text.literal("Next Page"), mouseX, mouseY);
            context.fill(nextPageX, allPageY, nextPageX + size, allPageY + size, 0x70FFFFFF);
        }

        if(currentPageNumber > 1){
            context.drawItem(new ItemStack(Items.ARROW), prevPageX, allPageY);
            if(mouseX >= prevPageX && mouseX <= prevPageX + 16 && mouseY >= allPageY && mouseY <= allPageY + 16){
                context.drawTooltip(this.textRenderer, Text.literal("Previous Page"), mouseX, mouseY);
                context.fill(prevPageX, allPageY, prevPageX + size, allPageY + size, 0x70FFFFFF);
            }
        }
    }
}
