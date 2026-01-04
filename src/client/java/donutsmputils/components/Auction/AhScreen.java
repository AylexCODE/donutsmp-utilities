package donutsmputils.components.Auction;

import java.util.ArrayList;
import java.util.List;

import donutsmputils.utils.AuctionData;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class AhScreen extends Screen {
    private static final Identifier TEXTURE = Identifier.of("donutsmp-utilities", "textures/gui/inventory_small.png");
    private static final int ELEMENT_HEIGHT = 20;
    private static final int ELEMENT_SPACING = 10;
    private ArrayList<AuctionData> data;
    private boolean isLoading = true;

    public AhScreen(){
        super(Text.of("AuctionScreen"));
    }
    
    public void setData(ArrayList<AuctionData> data){
        this.data = data;
        this.isLoading = false;
    }

    // public void init(){
    //     initCloseButton();
    // }

    // public void initCloseButton(){
    //     int closeButtonWidth = 100;
    //     int closeButtonX = width - closeButtonWidth - ELEMENT_SPACING;
    //     int closeButtonY = ELEMENT_HEIGHT - ELEMENT_SPACING;
    //     ButtonWidget closeButton = ButtonWidget.builder(Text.of("Close"), button -> client.setScreen(null))
    //         .dimensions(closeButtonX, closeButtonY, closeButtonWidth, ELEMENT_HEIGHT)
    //         .build();
        
    //     addDrawableChild(closeButton);
    // }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int invWidth = 176;
        int invHeight = 77;
        int invX = (this.width - invWidth) / 2;
        int invY = (this.height - invHeight) / 2;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, invX, invY, 0, 0, invWidth, invHeight, invWidth, invHeight);
        
        int y = (this.height - invHeight) / 2 + 17;
        int x = (this.width - invWidth) / 2 + 8;
        int size = 16;
        int slotNumber = 1;

        if(isLoading){
            context.drawText(this.textRenderer, Text.literal("..."), x, y - 11, 0xFF444444, false);
            return;
        }else{
            context.drawText(this.textRenderer, Text.literal("My Auction House Items"), x, y - 11, 0xFF444444, false);
        }

        for(AuctionData item : data){
            context.drawItem(item.getItemData(), x, y);
            context.drawStackOverlay(this.textRenderer, item.getItemData(), x, y, null);

            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
                List<Text> customLines = List.of(
                    Text.literal(item.getItemData().getName().getString()).formatted(Formatting.WHITE),
                    Text.literal("Price: ").append(Text.literal(item.getItemPrice()).formatted(Formatting.GREEN)),
                    Text.literal("Time Left: ").append(Text.literal(item.getItemTimeLeft()).formatted(Formatting.GREEN))
                );
                context.drawTooltip(this.textRenderer, customLines, mouseX, mouseY);
                context.fill(x, y, x + size, y + size, 0x70FFFFFF);
                // context.drawItemTooltip(this.textRenderer, itemStack , mouseX, mouseY);
            }

            x += 18; slotNumber++;
            if(slotNumber > 9){
                x = (this.width - invWidth) / 2 + 8;
                y += 18;
                slotNumber = 1;
            }
        }
    }
}
