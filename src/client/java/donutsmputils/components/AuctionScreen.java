package donutsmputils.components;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

public class AuctionScreen extends Screen {
	public AuctionScreen(Text title) {
        super(title);
        this.client.mouse.unlockCursor();
    }

    @Override
    protected void init() {
        // Fabric 1.21+ uses ButtonWidget.builder()
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.literal("Hello World"), (btn) -> {
            // Display a system toast notification
            this.client.getToastManager().add(
                SystemToast.create(this.client, SystemToast.Type.NARRATOR_TOGGLE, 
                Text.literal("Hello World!"), Text.literal("This is a toast."))
            );
        })
        .dimensions(40, 40, 120, 20) // x, y, width, height
        .build();

        this.addDrawable(buttonWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Mandatory: draws the screen background and widgets
        super.render(context, mouseX, mouseY, delta);

        // Fabric uses context.drawTextWithShadow (or drawString in newer versions)
        // 0xFFFFFFFF ensures the text is fully opaque (AARRGGBB)
        context.drawTextWithShadow(this.textRenderer, 
            "Special Button", 
            40, 
            40 - this.textRenderer.fontHeight - 10, 
            0xFFFFFFFF);
    }
}
