package io.github.mmm.modconfig.gui;

import io.github.mmm.MMM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static net.minecraft.util.CommonColors.WHITE;

public class SurveyConfigGUI extends Screen {

    // title
    private static final Component TITLE = Component.translatable("gui." + MMM.MODID + ".settings.survey.title");

    // save
    private static final Component EXIT = Component.translatable("gui." + MMM.MODID + ".settings.exit.name");
    private static Button ExitButton;
    private static final Component EXIT_BUTTON_TOOLTIP = Component.translatable("gui." + MMM.MODID + ".settings.exit.tooltip");

    // open different survey config
    private static final Component DEVICE = Component.translatable("gui." + MMM.MODID + ".settings.device.name");
    private static Button DeviceButton;
    private static final Component DEVICE_BUTTON_TOOLTIP = Component.translatable("gui." + MMM.MODID + ".settings.device.tooltip");

    public SurveyConfigGUI() {
        // Use the super class' constructor to set the screen's title
        super(TITLE);
    }

    @Override
    protected void init() {
        super.init();

        // device
        DeviceButton = Button.builder(DEVICE, this::handleDeviceButtonPress)
                .bounds(10, 10, 140, 20)
                .tooltip(Tooltip.create(DEVICE_BUTTON_TOOLTIP))
                .build();
        addRenderableWidget(DeviceButton);

        // exit
        ExitButton = Button.builder(EXIT, this::handleExitButtonPress)
                .bounds(this.width-140-10, 10, 140, 20)
                .tooltip(Tooltip.create(EXIT_BUTTON_TOOLTIP))
                .build();
        addRenderableWidget(ExitButton);
    }

    private void handleExitButtonPress(Button button) {
        this.onClose();
    }

    private void handleDeviceButtonPress(Button button) {
        this.onClose();
        Minecraft.getInstance().setScreen(new DeviceConfigGUI());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        this.renderDirtBackground(graphics);

        super.render(graphics, mouseX, mouseY, partialTick);

        // title
        graphics.drawCenteredString(this.font, TITLE, this.width/2, 17, WHITE);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().player.displayClientMessage(
                Component.translatable("chat." + MMM.MODID + ".gui.save.success"),
                false
        );
        MMM.latestConfigGUI = MMM.ConfigGUIType.SURVEY;
        super.onClose();
    }
}
