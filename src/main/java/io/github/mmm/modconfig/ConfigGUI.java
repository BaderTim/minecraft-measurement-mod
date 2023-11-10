package io.github.mmm.modconfig;

import io.github.mmm.MMM;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static io.github.mmm.MMM.LOGGER;
import static net.minecraft.util.CommonColors.GRAY;
import static net.minecraft.util.CommonColors.WHITE;

public class ConfigGUI extends Screen {

    // title
    private static final Component TITLE = Component.translatable("gui." + MMM.MODID + ".settings.title");
    private static final Component DESCRIPTION = Component.translatable("gui." + MMM.MODID + ".settings.text.description");

    // save
    private static final Component EXIT = Component.translatable("gui." + MMM.MODID + ".settings.exit.name");
    private static Button ExitButton;
    private static final Component EXIT_BUTTON_TOOLTIP = Component.translatable("gui." + MMM.MODID + ".settings.exit.tooltip");

    // switch button
    private static final Component SWITCH_ACTIVE = Component.translatable("gui." + MMM.MODID + ".settings.switch.active");
    private static final Component SWITCH_INACTIVE = Component.translatable("gui." + MMM.MODID + ".settings.switch.inactive");
    private static final Component SWITCH_TOOLTIP_DEACTIVATE = Component.translatable("gui." + MMM.MODID + ".settings.switch.tooltip.deactivate");
    private static final Component SWITCH_TOOLTIP_ACTIVATE = Component.translatable("gui." + MMM.MODID + ".settings.switch.tooltip.activate");

    // lidar
    private static final Component LIDAR = Component.translatable("gui." + MMM.MODID + ".settings.lidar");
    private static final Component LIDAR1_NAME = Component.translatable("gui." + MMM.MODID + ".settings.lidar1.name");
    private static Button Lidar1SwitchButton;
    private static final Component LIDAR2_NAME = Component.translatable("gui." + MMM.MODID + ".settings.lidar2.name");
    private static Button Lidar2SwitchButton;
    private static final Component LIDAR3_NAME = Component.translatable("gui." + MMM.MODID + ".settings.lidar3.name");
    private static Button Lidar3SwitchButton;

    // imu
    private static final Component IMU = Component.translatable("gui." + MMM.MODID + ".settings.imu");
    private static Button IMU1SwitchButton;
    private static final Component IMU1NAME = Component.translatable("gui." + MMM.MODID + ".settings.imu1.name");

    // bottom hints
    private static final Component CONTROLS = Component.translatable("gui." + MMM.MODID + ".settings.controls");
    private static final Component SAVEPATH = Component.translatable("gui." + MMM.MODID + ".settings.savepath");


    public ConfigGUI() {
        // Use the super class' constructor to set the screen's title
        super(TITLE);
    }

    @Override
    protected void init() {
        super.init();

        // exit
        ExitButton = Button.builder(EXIT, this::handleExitButtonPress)
                .bounds(this.width-100-10, 10, 100, 20)
                .tooltip(Tooltip.create(EXIT_BUTTON_TOOLTIP))
                .build();
        addRenderableWidget(ExitButton);
        // lidar1
        Lidar1SwitchButton = Button.builder(getSwitchState(Config.LIDAR1_SWITCH.get()), this::handleLidar1SwitchButtonPress)
                .bounds(50, 75, 50, 20)
                .tooltip(Tooltip.create(getSwitchTooltip(Config.LIDAR1_SWITCH.get())))
                .build();
        addRenderableWidget(Lidar1SwitchButton);
        // lidar2
        Lidar2SwitchButton = Button.builder(getSwitchState(Config.LIDAR2_SWITCH.get()), this::handleLidar2SwitchButtonPress)
                .bounds(50, 105, 50, 20)
                .tooltip(Tooltip.create(getSwitchTooltip(Config.LIDAR2_SWITCH.get())))
                .build();
        addRenderableWidget(Lidar2SwitchButton);
        // lidar3
        Lidar3SwitchButton = Button.builder(getSwitchState(Config.LIDAR3_SWITCH.get()), this::handleLidar3SwitchButtonPress)
                .bounds(50, 135, 50, 20)
                .tooltip(Tooltip.create(getSwitchTooltip(Config.LIDAR3_SWITCH.get())))
                .build();
        addRenderableWidget(Lidar3SwitchButton);
        // imu
        IMU1SwitchButton = Button.builder(getSwitchState(Config.IMU1_SWITCH.get()), this::handleImu1SwitchButtonPress)
                .bounds(50, 180, 50, 20)
                .tooltip(Tooltip.create(getSwitchTooltip(Config.IMU1_SWITCH.get())))
                .build();
        addRenderableWidget(IMU1SwitchButton);
    }

    private Component getSwitchState(boolean state) {
        return state ? SWITCH_ACTIVE : SWITCH_INACTIVE;
    }

    private Component getSwitchTooltip(boolean state) {
        return state ? SWITCH_TOOLTIP_DEACTIVATE : SWITCH_TOOLTIP_ACTIVATE;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        this.renderDirtBackground(graphics);

        super.render(graphics, mouseX, mouseY, partialTick);

        // title
        graphics.drawCenteredString(this.font, TITLE, this.width/2, 20, WHITE);
        graphics.drawCenteredString(this.font, DESCRIPTION, this.width/2, 35, GRAY);

        // lidar
        graphics.drawString(this.font, LIDAR, 10, 60, WHITE);
        graphics.drawString(this.font, LIDAR1_NAME, 10, 80, GRAY);
        graphics.drawString(this.font, LIDAR2_NAME, 10, 110, GRAY);
        graphics.drawString(this.font, LIDAR3_NAME, 10, 140, GRAY);

        // imu
        graphics.drawString(this.font, IMU, 10, 165, WHITE);
        graphics.drawString(this.font, IMU1NAME, 10, 185, GRAY);

        // bottom hints
        graphics.drawCenteredString(this.font, CONTROLS, this.width/2, this.height-40, GRAY);
        graphics.drawCenteredString(this.font, SAVEPATH, this.width/2, this.height-25, GRAY);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public void onClose() {
        // Stop any handlers here

        // Call last in case it interferes with the override
        super.onClose();
    }

    private void handleExitButtonPress(Button button) {
        LOGGER.info("Exit button pressed!");
        this.onClose();
    }

    private void handleLidar1SwitchButtonPress(Button button) {
        LOGGER.info("Lidar1 switch button pressed!");
        Config.LIDAR1_SWITCH.set(!Config.LIDAR1_SWITCH.get());
        Lidar1SwitchButton.setMessage(getSwitchState(Config.LIDAR1_SWITCH.get()));
    }

    private void handleLidar2SwitchButtonPress(Button button) {
        LOGGER.info("Lidar2 switch button pressed!");
        Config.LIDAR2_SWITCH.set(!Config.LIDAR2_SWITCH.get());
        Lidar2SwitchButton.setMessage(getSwitchState(Config.LIDAR2_SWITCH.get()));
    }

    private void handleLidar3SwitchButtonPress(Button button) {
        LOGGER.info("Lidar3 switch button pressed!");
        Config.LIDAR3_SWITCH.set(!Config.LIDAR3_SWITCH.get());
        Lidar3SwitchButton.setMessage(getSwitchState(Config.LIDAR3_SWITCH.get()));
    }

    private void handleImu1SwitchButtonPress(Button button) {
        LOGGER.info("Imu1 switch button pressed!");
        Config.IMU1_SWITCH.set(!Config.IMU1_SWITCH.get());
        IMU1SwitchButton.setMessage(getSwitchState(Config.IMU1_SWITCH.get()));
    }

}