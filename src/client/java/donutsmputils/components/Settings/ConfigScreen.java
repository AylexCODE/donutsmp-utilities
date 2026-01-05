package donutsmputils.components.Settings;

import donutsmputils.commands.SetConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private static final int ELEMENT_HEIGHT = 20;
    private static final int FIELD_WIDTH = 210;
    private static final int BUTTONS_WIDTH = 100;

    public ConfigScreen(){
        super(Text.of("Config Screen"));
    }

    public void init(){
        initGuis();
    }

    public void initGuis(){
        int button_y = ((height + ELEMENT_HEIGHT) / 2) + 40;
        int closeButtonX = ((width + BUTTONS_WIDTH) / 2) - BUTTONS_WIDTH - (BUTTONS_WIDTH / 2) - 5;
        ButtonWidget closeButton = ButtonWidget.builder(Text.of("Close"), button -> client.setScreen(null))
            .dimensions(closeButtonX, button_y, BUTTONS_WIDTH, ELEMENT_HEIGHT)
            .build();

        int apiKeyFieldX = (width - FIELD_WIDTH) / 2;
        int apiKeyFieldY = ((height - ELEMENT_HEIGHT) / 4) + 20;
        TextFieldWidget apiKeyField = new TextFieldWidget(textRenderer, apiKeyFieldX, apiKeyFieldY, FIELD_WIDTH,ELEMENT_HEIGHT, Text.of("Eyyy"));
        
        int usernameFieldX = (width - FIELD_WIDTH) / 2;
        int usernameFieldY = ((height - ELEMENT_HEIGHT) / 2) + 15;
        TextFieldWidget usernameField = new TextFieldWidget(textRenderer, usernameFieldX, usernameFieldY, FIELD_WIDTH,ELEMENT_HEIGHT, Text.of("Eyyy"));

        int submitButtonX = ((width + BUTTONS_WIDTH) / 2) - (BUTTONS_WIDTH / 2) + 5;
        ButtonWidget submitButton = ButtonWidget.builder(Text.of("Confirm"), button -> {
            Text statusTitle;
            Text statusMessage;

            if(apiKeyField.getText().isBlank() || usernameField.getText().isBlank()){
                statusMessage = Text.of("Fields cannot be empty");
                statusTitle = Text.of("Error");
            }else{
                SetConfig config = new SetConfig();
                client.setScreen(null);
                statusTitle = Text.of("Success");
                statusMessage = Text.of("Configurations set, you can now use dsmpu commands");
                config.setAPIKey(apiKeyField.getText());
                config.setUsername(usernameField.getText());
            }

            client.getToastManager().add(SystemToast.create(client, SystemToast.Type.NARRATOR_TOGGLE, statusTitle, statusMessage));
        }).dimensions(submitButtonX, button_y, BUTTONS_WIDTH, ELEMENT_HEIGHT).build();
        
        addDrawableChild(apiKeyField);
        addDrawableChild(usernameField);
        addDrawableChild(submitButton);
        addDrawableChild(closeButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        context.drawText(this.textRenderer, Text.literal("You need to enter your DonutSMP API Key for this mod to work."), 10, 10, 0xFFFFFFFF, false);

        int x = (width - FIELD_WIDTH) / 2; 
        int y = height / 4;
        
        context.drawText(this.textRenderer, Text.literal("API key"), x, y, 0xFFFFFFFF, false);
        context.drawText(this.textRenderer, Text.literal("Username"), x, y + 50, 0xFFFFFFFF, false);
    }
}
