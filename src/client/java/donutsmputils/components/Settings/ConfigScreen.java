package donutsmputils.components.Settings;

import java.util.ArrayList;

import donutsmputils.utils.ConfigManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private enum Tab { GENERAL, CREDENTIALS }
    private Tab currentTab = Tab.GENERAL;

    private final ArrayList<ButtonWidget> buttonWidgets = new ArrayList<>();
    private final ArrayList<ClickableWidget> generalWidgets = new ArrayList<>();
    private final ArrayList<ClickableWidget> credentialWidgets = new ArrayList<>();

    private static final int ELEMENT_HEIGHT = 20;
    private static final int BUTTON_WIDTH = 100;
    private static final int FIELD_WIDTH = 150;

    private static String newUsername = "";
    private static String newAPIKey = "";

    public ConfigScreen(){
        super(Text.of("Config Screen"));
    }

    public void init(){
        initConfig();
    }

    public void initConfig(){
        generalWidgets.clear();
        credentialWidgets.clear();

        String configUsername = ConfigManager.INSTANCE.username;

        int buttonY = height / 5;
        int fieldX = (width - (FIELD_WIDTH / 2)) / 2;
        int fieldY = height / 2;

        int closeButtonX = ((width + BUTTON_WIDTH) / 2) - BUTTON_WIDTH;
        ButtonWidget closeButton = ButtonWidget.builder(Text.of("Close"), button -> {
            if(!button.getMessage().equals(Text.of("Close"))){
                if(!newUsername.equals(configUsername)) ConfigManager.INSTANCE.username = newUsername;
                if(!newAPIKey.equals("********************************") || !newAPIKey.isBlank()) ConfigManager.INSTANCE.apikey = newAPIKey;
                ConfigManager.save();
            }

            client.setScreen(null);
        }).dimensions(closeButtonX, height - 40, BUTTON_WIDTH, ELEMENT_HEIGHT).build();

        int generalButtonX = ((width + BUTTON_WIDTH) / 2) - (BUTTON_WIDTH / 2);
        ButtonWidget generalButton = ButtonWidget.builder(Text.of("General"), button -> switchTab(Tab.GENERAL))
            .dimensions(generalButtonX - BUTTON_WIDTH - 5, buttonY, BUTTON_WIDTH, ELEMENT_HEIGHT)
            .build();
        
        int credentialsButtonX = ((width + BUTTON_WIDTH) / 2) - (BUTTON_WIDTH / 2);
        ButtonWidget credentialButton = ButtonWidget.builder(Text.of("Credentials"), button -> switchTab(Tab.CREDENTIALS))
            .dimensions(credentialsButtonX + 5, buttonY, BUTTON_WIDTH, ELEMENT_HEIGHT)
            .build();

        // ButtonWidget toDo = ButtonWidget.builder(Text.of("To Do"), button -> client.setScreen(null))
        //     .dimensions(90, 90, 150, ELEMENT_HEIGHT)
        //     .build();

        TextFieldWidget apiKeyField = new TextFieldWidget(textRenderer, fieldX, fieldY - 40, FIELD_WIDTH,ELEMENT_HEIGHT, Text.of("Eyyy"));
        if(!ConfigManager.INSTANCE.apikey.isBlank()) apiKeyField.setText("********************************");
        apiKeyField.setChangedListener(apikey -> {
            if(apikey.equals("********************************") || apikey.isBlank()){
                newAPIKey = "";
                if(newUsername.isBlank()) closeButton.setMessage(Text.of("Close"));
            }else{
                newAPIKey = apikey;
                closeButton.setMessage(Text.of("Save & Close"));
            }
        });

        TextFieldWidget usernameField = new TextFieldWidget(textRenderer, fieldX, fieldY - 10, FIELD_WIDTH,ELEMENT_HEIGHT, Text.of("Eyyy"));
        if(!ConfigManager.INSTANCE.username.isBlank()) usernameField.setText(ConfigManager.INSTANCE.username.toString());
        usernameField.setChangedListener(username -> {
            if(username.equals(configUsername) || username.isBlank()){
                newUsername = "";
                if(newAPIKey.isBlank()) closeButton.setMessage(Text.of("Close"));
            }else{
                newUsername = username;
                closeButton.setMessage(Text.of("Save & Close"));
            }
        });

        // generalWidgets.add(this.addDrawableChild(toDo));
        credentialWidgets.add(this.addDrawableChild(usernameField));
        credentialWidgets.add(this.addDrawableChild(apiKeyField));

        buttonWidgets.add(generalButton);
        buttonWidgets.add(credentialButton);
        
        addDrawableChild(closeButton);
        addDrawableChild(generalButton);
        addDrawableChild(credentialButton);

        switchTab(Tab.GENERAL);
    }

    private void switchTab(Tab tab) {
        this.currentTab = tab;
        
        switch (tab) {
            case GENERAL:
                buttonWidgets.get(0).active = false;
                buttonWidgets.get(1).active = true;
                break;
            case CREDENTIALS:
                buttonWidgets.get(0).active = true;
                buttonWidgets.get(1).active = false;
                break;
            default:
                buttonWidgets.get(0).active = false;
                buttonWidgets.get(1).active = false;
                break;
        }
        
        generalWidgets.forEach(w -> w.visible = (tab == Tab.GENERAL));
        credentialWidgets.forEach(w -> w.visible = (tab == Tab.CREDENTIALS));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int textY = height / 2;
        int textX = width / 2;
        
        context.drawText(this.textRenderer, Text.literal("DonutSMP Utilities"), (width / 2) - 42, textY / 5, 0xFFCCCCCC, false);
        
        switch (this.currentTab) {
            case GENERAL:
                break;
            case CREDENTIALS:
                context.drawText(this.textRenderer, Text.literal("API Key"), textX - 110, textY - 34, 0xFFCCCCCC, false);         
                context.drawText(this.textRenderer, Text.literal("Username"), textX - 110, textY - 5, 0xFFCCCCCC, false);         
                break;
            default:
                break;
        }
    }
}