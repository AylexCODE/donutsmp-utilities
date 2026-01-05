package donutsmputils.components.Settings;

import donutsmputils.utils.ConfigManager;
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
    boolean requireAPIKey = true;
    boolean requireUsername = false;
    int offsetY = 0;

    public ConfigScreen(boolean requireAPIKey, boolean requireUsername){
        super(Text.of("Config Screen"));
        this.requireAPIKey = requireAPIKey;
        this.requireUsername = requireUsername;
    }

    public void init(){
        initGuis();
    }

    public void initGuis(){
        int button_y = ((height + ELEMENT_HEIGHT) / 2) + 20;
        int fieldOffsetY = 0;

        if(requireAPIKey && !requireUsername){
            fieldOffsetY = 15;
            button_y -= 30;
        }else if(requireUsername && !requireAPIKey){
            fieldOffsetY = -30;
            button_y -= 30;
        }

        int closeButtonX = ((width + BUTTONS_WIDTH) / 2) - (BUTTONS_WIDTH / 2) + 5;
        ButtonWidget closeButton = ButtonWidget.builder(Text.of("Close"), button -> client.setScreen(null))
            .dimensions(closeButtonX, button_y, BUTTONS_WIDTH, ELEMENT_HEIGHT)
            .build();

        int apiKeyFieldX = (width - FIELD_WIDTH) / 2;
        int apiKeyFieldY = ((height - ELEMENT_HEIGHT) / 2) - 30 + fieldOffsetY;
        TextFieldWidget apiKeyField = new TextFieldWidget(textRenderer, apiKeyFieldX, apiKeyFieldY, FIELD_WIDTH,ELEMENT_HEIGHT, Text.of("Eyyy"));
        
        int usernameFieldX = (width - FIELD_WIDTH) / 2;
        int usernameFieldY = ((height - ELEMENT_HEIGHT) / 2) + 15 + fieldOffsetY;
        TextFieldWidget usernameField = new TextFieldWidget(textRenderer, usernameFieldX, usernameFieldY, FIELD_WIDTH,ELEMENT_HEIGHT, Text.of("Eyyy"));

        int submitButtonX = ((width + BUTTONS_WIDTH) / 2) - BUTTONS_WIDTH - (BUTTONS_WIDTH / 2) - 5;
        ButtonWidget submitButton = ButtonWidget.builder(Text.of("Confirm"), button -> {
            Text statusTitle;
            Text statusMessage;

            if(apiKeyField.getText().isBlank() && !requireUsername){
                statusMessage = Text.of("Fields cannot be empty");
                statusTitle = Text.of("Error");
            }else if(usernameField.getText().isBlank() && !requireAPIKey){
                statusMessage = Text.of("Fields cannot be empty");
                statusTitle = Text.of("Error");
            }else{
                if((apiKeyField.getText().isBlank() || usernameField.getText().isBlank()) && (requireAPIKey && requireUsername)){
                    statusMessage = Text.of("Fields cannot be empty");
                    statusTitle = Text.of("Error");
                }else{
                    ConfigManager config = new ConfigManager();
                    client.setScreen(null);
                    statusTitle = Text.of("Success");
                    statusMessage = Text.of("Configurations set, you can now use dsmp commands");
                    if(requireAPIKey) config.setAPIKey(apiKeyField.getText());
                    if(requireUsername) config.setUsername(usernameField.getText());
                }
            }

            client.getToastManager().add(SystemToast.create(client, SystemToast.Type.NARRATOR_TOGGLE, statusTitle, statusMessage));
        }).dimensions(submitButtonX, button_y, BUTTONS_WIDTH, ELEMENT_HEIGHT).build();
        
        if(!requireAPIKey) apiKeyField.visible = false;
        if(!requireUsername) usernameField.visible = false;

        addDrawableChild(apiKeyField);
        addDrawableChild(usernameField);
        addDrawableChild(submitButton);
        addDrawableChild(closeButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        String info = "...";

        int x = (width - FIELD_WIDTH) / 2; 
        int apiKeY = (height / 2) - 55;
        int usernameY = (height / 2) - 60;
        if(requireAPIKey && !requireUsername) apiKeY += 15;

        if(requireAPIKey){
            context.drawText(this.textRenderer, Text.literal("API key"), x, apiKeY, 0xFFFFFFFF, false);
            info = "You need to enter your DonutSMP API Key for this mod to work.";
        }

        if(requireAPIKey && requireUsername){
            context.drawText(this.textRenderer, Text.literal("Username"), x, usernameY + 50, 0xFFFFFFFF, false);
            context.drawText(this.textRenderer, Text.literal("You need to enter your Username for this command to work."), 10, 20, 0xFFCCCCCC, false);
        }
        
        if(requireUsername && !requireAPIKey){
            usernameY -= 30;
            context.drawText(this.textRenderer, Text.literal("Username"), x, usernameY + 50, 0xFFFFFFFF, false);
            info = "You need to enter your Username for this command to work.";
        }
        
        context.drawText(this.textRenderer, Text.literal(info), 10, 10, 0xFFCCCCCC, false);
    }
}
