package donutsmputils.components.Settings;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private static final int ELEMENT_HEIGHT = 20;
    private static final int BUTTON_WIDTH = 100;

    public ConfigScreen(){
        super(Text.of("Config Screen"));
    }

    public void init(){
        initConfig();
    }

    public void initConfig(){
        int closeButtonX = ((width + BUTTON_WIDTH) / 2) - (BUTTON_WIDTH / 2) + 5;
        ButtonWidget closeButton = ButtonWidget.builder(Text.of("Close"), button -> client.setScreen(null))
            .dimensions(closeButtonX, 10, BUTTON_WIDTH, ELEMENT_HEIGHT)
            .build();

        addDrawableChild(closeButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        context.drawText(this.textRenderer, Text.literal("Settings"), 10, 10, 0xFFCCCCCC, false);
    }
}
