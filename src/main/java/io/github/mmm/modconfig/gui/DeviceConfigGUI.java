package io.github.mmm.modconfig.gui;

import io.github.mmm.MMM;
import io.github.mmm.modconfig.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraftforge.common.ForgeConfigSpec;

import static io.github.mmm.MMM.LOGGER;
import static io.github.mmm.MMM.DEVICE_CONTROLLER;
import static net.minecraft.util.CommonColors.GRAY;
import static net.minecraft.util.CommonColors.WHITE;

public class DeviceConfigGUI extends Screen {

    private static final int[] validFrequencies = new int[]{1, 2, 4, 5, 10, 20};

    // title
    private static final Component TITLE = Component.translatable("gui." + MMM.MODID + ".settings.device.title");

    // save
    private static final Component EXIT = Component.translatable("gui." + MMM.MODID + ".settings.exit.name");
    private static Button ExitButton;
    private static final Component EXIT_BUTTON_TOOLTIP = Component.translatable("gui." + MMM.MODID + ".settings.exit.tooltip");

    // open different survey config
    private static final Component SURVEY = Component.translatable("gui." + MMM.MODID + ".settings.survey.name");
    private static Button SurveyButton;
    private static final Component SURVEY_BUTTON_TOOLTIP = Component.translatable("gui." + MMM.MODID + ".settings.survey.tooltip");

    // switch button
    private static final Component SWITCH_ACTIVE = Component.translatable("gui." + MMM.MODID + ".settings.switch.active");
    private static final Component SWITCH_INACTIVE = Component.translatable("gui." + MMM.MODID + ".settings.switch.inactive");
    private static final Component SWITCH_TOOLTIP_DEACTIVATE = Component.translatable("gui." + MMM.MODID + ".settings.switch.tooltip.deactivate");
    private static final Component SWITCH_TOOLTIP_ACTIVATE = Component.translatable("gui." + MMM.MODID + ".settings.switch.tooltip.activate");

    // lidar
    private static final Component LIDAR = Component.translatable("gui." + MMM.MODID + ".settings.lidar.title");
    private static final Component STATUS = Component.translatable("gui." + MMM.MODID + ".settings.lidar.status");
    private static final Component FREQUENCY = Component.translatable("gui." + MMM.MODID + ".settings.lidar.frequency");
    private static final FormattedText HORIZONTAL = Component.translatable("gui." + MMM.MODID + ".settings.lidar.horizontal");
    private static final FormattedText VERTICAL = Component.translatable("gui." + MMM.MODID + ".settings.lidar.vertical");
    private static final Component YAW_PITCH_ROLL = Component.translatable("gui." + MMM.MODID + ".settings.lidar.offset");
    private static final Component RANGE = Component.translatable("gui." + MMM.MODID + ".settings.lidar.range");

    private static final Component LIDAR1_NAME = Component.translatable("gui." + MMM.MODID + ".settings.lidar.lidar1.name");
    private static Button Lidar1SwitchButton;
    private static Button Lidar1FrequencyButton;
    private static EditBox Lidar1HorizontalScanRadiusEditBox;
    private static EditBox Lidar1HorizontalScansPerRadiusEditBox;
    private static EditBox Lidar1VerticalScanRadiusEditBox;
    private static EditBox Lidar1VerticalScansPerRadiusEditBox;
    private static EditBox Lidar1YawOffsetEditBox;
    private static EditBox Lidar1PitchOffsetEditBox;
    private static EditBox Lidar1RollOffsetEditBox;
    private static EditBox Lidar1MaxMeasurementDistanceEditBox;

    private static final Component LIDAR2_NAME = Component.translatable("gui." + MMM.MODID + ".settings.lidar.lidar2.name");
    private static Button Lidar2SwitchButton;
    private static Button Lidar2FrequencyButton;
    private static EditBox Lidar2HorizontalScanRadiusEditBox;
    private static EditBox Lidar2HorizontalScansPerRadiusEditBox;
    private static EditBox Lidar2VerticalScanRadiusEditBox;
    private static EditBox Lidar2VerticalScansPerRadiusEditBox;
    private static EditBox Lidar2YawOffsetEditBox;
    private static EditBox Lidar2PitchOffsetEditBox;
    private static EditBox Lidar2RollOffsetEditBox;
    private static EditBox Lidar2MaxMeasurementDistanceEditBox;

    private static final Component LIDAR3_NAME = Component.translatable("gui." + MMM.MODID + ".settings.lidar.lidar3.name");
    private static Button Lidar3SwitchButton;
    private static Button Lidar3FrequencyButton;
    private static EditBox Lidar3HorizontalScanRadiusEditBox;
    private static EditBox Lidar3HorizontalScansPerRadiusEditBox;
    private static EditBox Lidar3VerticalScanRadiusEditBox;
    private static EditBox Lidar3VerticalScansPerRadiusEditBox;
    private static EditBox Lidar3YawOffsetEditBox;
    private static EditBox Lidar3PitchOffsetEditBox;
    private static EditBox Lidar3RollOffsetEditBox;
    private static EditBox Lidar3MaxMeasurementDistanceEditBox;

    // imu
    private static final Component IMU = Component.translatable("gui." + MMM.MODID + ".settings.imu.title");
    private static Button IMU1SwitchButton;
    private static Button IMU1FrequencyButton;
    private static final Component IMU1NAME = Component.translatable("gui." + MMM.MODID + ".settings.imu.imu1.name");

    // bottom hints
    private static final Component SAVEPATH = Component.translatable("gui." + MMM.MODID + ".settings.savepath");


    public DeviceConfigGUI() {
        // Use the super class' constructor to set the screen's title
        super(TITLE);
    }

    @Override
    protected void init() {
        super.init();

        // survey
        SurveyButton = Button.builder(SURVEY, this::handleSurveyButtonPress)
                .bounds(10, 10, 140, 20)
                .tooltip(Tooltip.create(SURVEY_BUTTON_TOOLTIP))
                .build();
        addRenderableWidget(SurveyButton);

        // exit
        ExitButton = Button.builder(EXIT, this::handleExitButtonPress)
                .bounds(this.width-140-10, 10, 140, 20)
                .tooltip(Tooltip.create(EXIT_BUTTON_TOOLTIP))
                .build();
        addRenderableWidget(ExitButton);

        // lidar1
        int lidar1Y = 75;
        Lidar1SwitchButton = Button.builder(getSwitchState(Config.LIDAR1_SWITCH.get()), this::handleLidar1SwitchButtonPress)
                .bounds(50, lidar1Y, 50, 20)
                .tooltip(Tooltip.create(getSwitchTooltip(Config.LIDAR1_SWITCH.get())))
                .build();
        addRenderableWidget(Lidar1SwitchButton);
        Lidar1FrequencyButton = Button.builder(Component.literal(Config.LIDAR1_MEASUREMENT_FREQUENCY_IN_HZ.get()+" Hz"), this::handleLidar1FrequencyButtonPress)
                .bounds(110, lidar1Y, 40, 20)
                .build();
        addRenderableWidget(Lidar1FrequencyButton);
        Lidar1HorizontalScanRadiusEditBox = new EditBox(
                this.font, 160, lidar1Y, 30, 20,
                null);
        Lidar1HorizontalScanRadiusEditBox.setMaxLength(3);
        Lidar1HorizontalScanRadiusEditBox.setValue(String.valueOf(Config.LIDAR1_HORIZONTAL_SCANNING_RADIUS_IN_DEG.get()));
        addRenderableWidget(Lidar1HorizontalScanRadiusEditBox);
        Lidar1HorizontalScansPerRadiusEditBox = new EditBox(
                this.font, 190, lidar1Y, 40, 20,
                null);
        Lidar1HorizontalScansPerRadiusEditBox.setMaxLength(4);
        Lidar1HorizontalScansPerRadiusEditBox.setValue(String.valueOf(Config.LIDAR1_HORIZONTAL_SCANS_PER_RADIUS.get()));
        addRenderableWidget(Lidar1HorizontalScansPerRadiusEditBox);
        Lidar1VerticalScanRadiusEditBox = new EditBox(
                this.font, 240, lidar1Y, 30, 20,
                null);
        Lidar1VerticalScanRadiusEditBox.setMaxLength(3);
        Lidar1VerticalScanRadiusEditBox.setValue(String.valueOf(Config.LIDAR1_VERTICAL_SCANNING_RADIUS_IN_DEG.get()));
        addRenderableWidget(Lidar1VerticalScanRadiusEditBox);
        Lidar1VerticalScansPerRadiusEditBox = new EditBox(
                this.font, 270, lidar1Y, 40, 20,
                null);
        Lidar1VerticalScansPerRadiusEditBox.setMaxLength(4);
        Lidar1VerticalScansPerRadiusEditBox.setValue(String.valueOf(Config.LIDAR1_VERTICAL_SCANS_PER_RADIUS.get()));
        addRenderableWidget(Lidar1VerticalScansPerRadiusEditBox);
        Lidar1YawOffsetEditBox = new EditBox(
                this.font, 320, lidar1Y, 30, 20,
                null);
        Lidar1YawOffsetEditBox.setMaxLength(3);
        Lidar1YawOffsetEditBox.setValue(String.valueOf(Config.LIDAR1_YAW_OFFSET_FROM_POV_IN_DEG.get()));
        addRenderableWidget(Lidar1YawOffsetEditBox);
        Lidar1PitchOffsetEditBox = new EditBox(
                this.font, 350, lidar1Y, 30, 20,
                null);
        Lidar1PitchOffsetEditBox.setMaxLength(3);
        Lidar1PitchOffsetEditBox.setValue(String.valueOf(Config.LIDAR1_PITCH_OFFSET_FROM_POV_IN_DEG.get()));
        addRenderableWidget(Lidar1PitchOffsetEditBox);
        Lidar1RollOffsetEditBox = new EditBox(
                this.font, 380, lidar1Y, 30, 20,
                null);
        Lidar1RollOffsetEditBox.setMaxLength(3);
        Lidar1RollOffsetEditBox.setValue(String.valueOf(Config.LIDAR1_ROLL_OFFSET_FROM_POV_IN_DEG.get()));
        addRenderableWidget(Lidar1RollOffsetEditBox);
        Lidar1MaxMeasurementDistanceEditBox = new EditBox(
                this.font, 420, lidar1Y, 40, 20,
                null);
        Lidar1MaxMeasurementDistanceEditBox.setMaxLength(4);
        Lidar1MaxMeasurementDistanceEditBox.setValue(String.valueOf(Config.LIDAR1_MAXIMUM_MEASUREMENT_DISTANCE.get()));
        addRenderableWidget(Lidar1MaxMeasurementDistanceEditBox);

        // lidar2
        int lidar2Y = lidar1Y+30;
        Lidar2SwitchButton = Button.builder(getSwitchState(Config.LIDAR2_SWITCH.get()), this::handleLidar2SwitchButtonPress)
                .bounds(50, lidar2Y, 50, 20)
                .tooltip(Tooltip.create(getSwitchTooltip(Config.LIDAR2_SWITCH.get())))
                .build();
        addRenderableWidget(Lidar2SwitchButton);
        Lidar2FrequencyButton = Button.builder(Component.literal(Config.LIDAR2_MEASUREMENT_FREQUENCY_IN_HZ.get()+" Hz"), this::handleLidar2FrequencyButtonPress)
                .bounds(110, lidar2Y, 40, 20)
                .build();
        addRenderableWidget(Lidar2FrequencyButton);
        Lidar2HorizontalScanRadiusEditBox = new EditBox(
                this.font, 160, lidar2Y, 30, 20,
                null);
        Lidar2HorizontalScanRadiusEditBox.setMaxLength(3);
        Lidar2HorizontalScanRadiusEditBox.setValue(String.valueOf(Config.LIDAR2_HORIZONTAL_SCANNING_RADIUS_IN_DEG.get()));
        addRenderableWidget(Lidar2HorizontalScanRadiusEditBox);
        Lidar2HorizontalScansPerRadiusEditBox = new EditBox(
                this.font, 190, lidar2Y, 40, 20,
                null);
        Lidar2HorizontalScansPerRadiusEditBox.setMaxLength(4);
        Lidar2HorizontalScansPerRadiusEditBox.setValue(String.valueOf(Config.LIDAR2_HORIZONTAL_SCANS_PER_RADIUS.get()));
        addRenderableWidget(Lidar2HorizontalScansPerRadiusEditBox);
        Lidar2VerticalScanRadiusEditBox = new EditBox(
                this.font, 240, lidar2Y, 30, 20,
                null);
        Lidar2VerticalScanRadiusEditBox.setMaxLength(3);
        Lidar2VerticalScanRadiusEditBox.setValue(String.valueOf(Config.LIDAR2_VERTICAL_SCANNING_RADIUS_IN_DEG.get()));
        addRenderableWidget(Lidar2VerticalScanRadiusEditBox);
        Lidar2VerticalScansPerRadiusEditBox = new EditBox(
                this.font, 270, lidar2Y, 40, 20,
                null);
        Lidar2VerticalScansPerRadiusEditBox.setMaxLength(4);
        Lidar2VerticalScansPerRadiusEditBox.setValue(String.valueOf(Config.LIDAR2_VERTICAL_SCANS_PER_RADIUS.get()));
        addRenderableWidget(Lidar2VerticalScansPerRadiusEditBox);
        Lidar2YawOffsetEditBox = new EditBox(
                this.font, 320, lidar2Y, 30, 20,
                null);
        Lidar2YawOffsetEditBox.setMaxLength(3);
        Lidar2YawOffsetEditBox.setValue(String.valueOf(Config.LIDAR2_YAW_OFFSET_FROM_POV_IN_DEG.get()));
        addRenderableWidget(Lidar2YawOffsetEditBox);
        Lidar2PitchOffsetEditBox = new EditBox(
                this.font, 350, lidar2Y, 30, 20,
                null);
        Lidar2PitchOffsetEditBox.setMaxLength(3);
        Lidar2PitchOffsetEditBox.setValue(String.valueOf(Config.LIDAR2_PITCH_OFFSET_FROM_POV_IN_DEG.get()));
        addRenderableWidget(Lidar2PitchOffsetEditBox);
        Lidar2RollOffsetEditBox = new EditBox(
                this.font, 380, lidar2Y, 30, 20,
                null);
        Lidar2RollOffsetEditBox.setMaxLength(3);
        Lidar2RollOffsetEditBox.setValue(String.valueOf(Config.LIDAR2_ROLL_OFFSET_FROM_POV_IN_DEG.get()));
        addRenderableWidget(Lidar2RollOffsetEditBox);
        Lidar2MaxMeasurementDistanceEditBox = new EditBox(
                this.font, 420, lidar2Y, 40, 20,
                null);
        Lidar2MaxMeasurementDistanceEditBox.setMaxLength(4);
        Lidar2MaxMeasurementDistanceEditBox.setValue(String.valueOf(Config.LIDAR2_MAXIMUM_MEASUREMENT_DISTANCE.get()));
        addRenderableWidget(Lidar2MaxMeasurementDistanceEditBox);

        // lidar3
        int lidar3Y = lidar2Y+30;
        Lidar3SwitchButton = Button.builder(getSwitchState(Config.LIDAR3_SWITCH.get()), this::handleLidar3SwitchButtonPress)
                .bounds(50, lidar3Y, 50, 20)
                .tooltip(Tooltip.create(getSwitchTooltip(Config.LIDAR3_SWITCH.get())))
                .build();
        addRenderableWidget(Lidar3SwitchButton);
        Lidar3FrequencyButton = Button.builder(Component.literal(Config.LIDAR3_MEASUREMENT_FREQUENCY_IN_HZ.get()+" Hz"), this::handleLidar3FrequencyButtonPress)
                .bounds(110, lidar3Y, 40, 20)
                .build();
        addRenderableWidget(Lidar3FrequencyButton);
        Lidar3HorizontalScanRadiusEditBox = new EditBox(
                this.font, 160, lidar3Y, 30, 20,
                null);
        Lidar3HorizontalScanRadiusEditBox.setMaxLength(3);
        Lidar3HorizontalScanRadiusEditBox.setValue(String.valueOf(Config.LIDAR3_HORIZONTAL_SCANNING_RADIUS_IN_DEG.get()));
        addRenderableWidget(Lidar3HorizontalScanRadiusEditBox);
        Lidar3HorizontalScansPerRadiusEditBox = new EditBox(
                this.font, 190, lidar3Y, 40, 20,
                null);
        Lidar3HorizontalScansPerRadiusEditBox.setMaxLength(4);
        Lidar3HorizontalScansPerRadiusEditBox.setValue(String.valueOf(Config.LIDAR3_HORIZONTAL_SCANS_PER_RADIUS.get()));
        addRenderableWidget(Lidar3HorizontalScansPerRadiusEditBox);
        Lidar3VerticalScanRadiusEditBox = new EditBox(
                this.font, 240, lidar3Y, 30, 20,
                null);
        Lidar3VerticalScanRadiusEditBox.setMaxLength(3);
        Lidar3VerticalScanRadiusEditBox.setValue(String.valueOf(Config.LIDAR3_VERTICAL_SCANNING_RADIUS_IN_DEG.get()));
        addRenderableWidget(Lidar3VerticalScanRadiusEditBox);
        Lidar3VerticalScansPerRadiusEditBox = new EditBox(
                this.font, 270, lidar3Y, 40, 20,
                null);
        Lidar3VerticalScansPerRadiusEditBox.setMaxLength(4);
        Lidar3VerticalScansPerRadiusEditBox.setValue(String.valueOf(Config.LIDAR3_VERTICAL_SCANS_PER_RADIUS.get()));
        addRenderableWidget(Lidar3VerticalScansPerRadiusEditBox);
        Lidar3YawOffsetEditBox = new EditBox(
                this.font, 320, lidar3Y, 30, 20,
                null);
        Lidar3YawOffsetEditBox.setMaxLength(3);
        Lidar3YawOffsetEditBox.setValue(String.valueOf(Config.LIDAR3_YAW_OFFSET_FROM_POV_IN_DEG.get()));
        addRenderableWidget(Lidar3YawOffsetEditBox);
        Lidar3PitchOffsetEditBox = new EditBox(
                this.font, 350, lidar3Y, 30, 20,
                null);
        Lidar3PitchOffsetEditBox.setMaxLength(3);
        Lidar3PitchOffsetEditBox.setValue(String.valueOf(Config.LIDAR3_PITCH_OFFSET_FROM_POV_IN_DEG.get()));
        addRenderableWidget(Lidar3PitchOffsetEditBox);
        Lidar3RollOffsetEditBox = new EditBox(
                this.font, 380, lidar3Y, 30, 20,
                null);
        Lidar3RollOffsetEditBox.setMaxLength(3);
        Lidar3RollOffsetEditBox.setValue(String.valueOf(Config.LIDAR3_ROLL_OFFSET_FROM_POV_IN_DEG.get()));
        addRenderableWidget(Lidar3RollOffsetEditBox);
        Lidar3MaxMeasurementDistanceEditBox = new EditBox(
                this.font, 420, lidar3Y, 40, 20,
                null);
        Lidar3MaxMeasurementDistanceEditBox.setMaxLength(4);
        Lidar3MaxMeasurementDistanceEditBox.setValue(String.valueOf(Config.LIDAR2_MAXIMUM_MEASUREMENT_DISTANCE.get()));
        addRenderableWidget(Lidar3MaxMeasurementDistanceEditBox);

        // imu1
        int imu1Y = 180;
        IMU1SwitchButton = Button.builder(getSwitchState(Config.IMU1_SWITCH.get()), this::handleImu1SwitchButtonPress)
                .bounds(50, imu1Y, 50, 20)
                .tooltip(Tooltip.create(getSwitchTooltip(Config.IMU1_SWITCH.get())))
                .build();
        addRenderableWidget(IMU1SwitchButton);
        IMU1FrequencyButton = Button.builder(Component.literal(Config.IMU1_MEAUREMENT_FREQUENCY_IN_HZ.get()+" Hz"), this::handleImu1FrequencyButtonPress)
                .bounds(110, imu1Y, 40, 20)
                .build();
        addRenderableWidget(IMU1FrequencyButton);
    }

    private void handleSurveyButtonPress(Button button) {
        this.onClose();
        Minecraft.getInstance().setScreen(new SurveyConfigGUI());
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
        graphics.drawCenteredString(this.font, TITLE, this.width/2, 17, WHITE);

        // lidar
        graphics.drawString(this.font, LIDAR, 10, 45, WHITE);
        graphics.drawString(this.font, STATUS, 60, 60, GRAY);
        graphics.drawString(this.font, FREQUENCY, 117, 60, GRAY);
        graphics.drawWordWrap(this.font, HORIZONTAL, 160, 55, 120, GRAY);
        graphics.drawWordWrap(this.font, VERTICAL, 240, 55, 120, GRAY);
        graphics.drawString(this.font, YAW_PITCH_ROLL, 320, 60, GRAY);
        graphics.drawString(this.font, RANGE, 420, 60, GRAY);

        graphics.drawString(this.font, LIDAR1_NAME, 10, 80, GRAY);
        graphics.drawString(this.font, LIDAR2_NAME, 10, 110, GRAY);
        graphics.drawString(this.font, LIDAR3_NAME, 10, 140, GRAY);

        // imu
        graphics.drawString(this.font, IMU, 10, 165, WHITE);
        graphics.drawString(this.font, IMU1NAME, 10, 185, GRAY);

        // bottom hints
        graphics.drawCenteredString(this.font, SAVEPATH, this.width/2, this.height-25, GRAY);
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
        // save config
        saveRadius(Config.LIDAR1_HORIZONTAL_SCANNING_RADIUS_IN_DEG, Lidar1HorizontalScanRadiusEditBox);
        saveDistanceOrDensity(Config.LIDAR1_HORIZONTAL_SCANS_PER_RADIUS, Lidar1HorizontalScansPerRadiusEditBox);
        saveRadius(Config.LIDAR1_VERTICAL_SCANNING_RADIUS_IN_DEG, Lidar1VerticalScanRadiusEditBox);
        saveDistanceOrDensity(Config.LIDAR1_VERTICAL_SCANS_PER_RADIUS, Lidar1VerticalScansPerRadiusEditBox);
        saveRadius(Config.LIDAR1_YAW_OFFSET_FROM_POV_IN_DEG, Lidar1YawOffsetEditBox);
        saveRadius(Config.LIDAR1_PITCH_OFFSET_FROM_POV_IN_DEG, Lidar1PitchOffsetEditBox);
        saveRadius(Config.LIDAR1_ROLL_OFFSET_FROM_POV_IN_DEG, Lidar1RollOffsetEditBox);
        saveDistanceOrDensity(Config.LIDAR1_MAXIMUM_MEASUREMENT_DISTANCE, Lidar1MaxMeasurementDistanceEditBox);

        saveRadius(Config.LIDAR2_HORIZONTAL_SCANNING_RADIUS_IN_DEG, Lidar2HorizontalScanRadiusEditBox);
        saveDistanceOrDensity(Config.LIDAR2_HORIZONTAL_SCANS_PER_RADIUS, Lidar2HorizontalScansPerRadiusEditBox);
        saveRadius(Config.LIDAR2_VERTICAL_SCANNING_RADIUS_IN_DEG, Lidar2VerticalScanRadiusEditBox);
        saveDistanceOrDensity(Config.LIDAR2_VERTICAL_SCANS_PER_RADIUS, Lidar2VerticalScansPerRadiusEditBox);
        saveRadius(Config.LIDAR2_YAW_OFFSET_FROM_POV_IN_DEG, Lidar2YawOffsetEditBox);
        saveRadius(Config.LIDAR2_PITCH_OFFSET_FROM_POV_IN_DEG, Lidar2PitchOffsetEditBox);
        saveRadius(Config.LIDAR2_ROLL_OFFSET_FROM_POV_IN_DEG, Lidar2RollOffsetEditBox);
        saveDistanceOrDensity(Config.LIDAR2_MAXIMUM_MEASUREMENT_DISTANCE, Lidar2MaxMeasurementDistanceEditBox);

        saveRadius(Config.LIDAR3_HORIZONTAL_SCANNING_RADIUS_IN_DEG, Lidar3HorizontalScanRadiusEditBox);
        saveDistanceOrDensity(Config.LIDAR3_HORIZONTAL_SCANS_PER_RADIUS, Lidar3HorizontalScansPerRadiusEditBox);
        saveRadius(Config.LIDAR3_VERTICAL_SCANNING_RADIUS_IN_DEG, Lidar3VerticalScanRadiusEditBox);
        saveDistanceOrDensity(Config.LIDAR3_VERTICAL_SCANS_PER_RADIUS, Lidar3VerticalScansPerRadiusEditBox);
        saveRadius(Config.LIDAR3_YAW_OFFSET_FROM_POV_IN_DEG, Lidar3YawOffsetEditBox);
        saveRadius(Config.LIDAR3_PITCH_OFFSET_FROM_POV_IN_DEG, Lidar3PitchOffsetEditBox);
        saveRadius(Config.LIDAR3_ROLL_OFFSET_FROM_POV_IN_DEG, Lidar3RollOffsetEditBox);
        saveDistanceOrDensity(Config.LIDAR3_MAXIMUM_MEASUREMENT_DISTANCE, Lidar3MaxMeasurementDistanceEditBox);

        DEVICE_CONTROLLER.initDevices();

        MMM.latestConfigGUI = MMM.ConfigGUIType.DEVICE;
        // Call last in case it interferes with the override
        super.onClose();
    }

    private void saveRadius(ForgeConfigSpec.ConfigValue<Integer> configValue, EditBox editBox) {
        try {
            int value = Integer.parseInt(editBox.getValue());
            if (value < 0 || value > 360) {
                editBox.setValue(String.valueOf(configValue.get()));
            } else {
                configValue.set(value);
                return;
            }
        } catch (NumberFormatException e) {
            editBox.setValue(String.valueOf(configValue.get()));
        }
        Minecraft.getInstance().player.displayClientMessage(
                Component.translatable("chat." + MMM.MODID + ".gui.save.warn"),
                false
        );
    }

    private void saveDistanceOrDensity(ForgeConfigSpec.ConfigValue<Integer> configValue, EditBox editBox) {
        try {
            int value = Integer.parseInt(editBox.getValue());
            if (value < 0 || value > 9999) {
                editBox.setValue(String.valueOf(configValue.get()));
            } else {
                configValue.set(value);
                return;
            }
        } catch (NumberFormatException e) {
            editBox.setValue(String.valueOf(configValue.get()));
        }
        Minecraft.getInstance().player.displayClientMessage(
                Component.translatable("chat." + MMM.MODID + ".gui.save.warn"),
                false
        );
    }

    private void handleExitButtonPress(Button button) {
        LOGGER.info("Exit button pressed!");
        this.onClose();
    }

    private void handleLidar1SwitchButtonPress(Button button) {
        LOGGER.info("Lidar1 switch button pressed!");
        Config.LIDAR1_SWITCH.set(!Config.LIDAR1_SWITCH.get());
        Lidar1SwitchButton.setMessage(getSwitchState(Config.LIDAR1_SWITCH.get()));
        Lidar1SwitchButton.setTooltip(Tooltip.create(getSwitchTooltip(Config.LIDAR1_SWITCH.get())));
    }

    private void handleLidar1FrequencyButtonPress(Button button) {
        LOGGER.info("Lidar1 frequency button pressed!");
        int index = 0;
        for (int i = 0; i < validFrequencies.length; i++) {
            if (validFrequencies[i] == Config.LIDAR1_MEASUREMENT_FREQUENCY_IN_HZ.get()) {
                index = i;
                break;
            }
        }
        index = (index + 1) % validFrequencies.length;
        Config.LIDAR1_MEASUREMENT_FREQUENCY_IN_HZ.set(validFrequencies[index]);
        Lidar1FrequencyButton.setMessage(Component.literal(Config.LIDAR1_MEASUREMENT_FREQUENCY_IN_HZ.get()+" Hz"));
    }

    private void handleLidar2SwitchButtonPress(Button button) {
        LOGGER.info("Lidar2 switch button pressed!");
        Config.LIDAR2_SWITCH.set(!Config.LIDAR2_SWITCH.get());
        Lidar2SwitchButton.setMessage(getSwitchState(Config.LIDAR2_SWITCH.get()));
        Lidar2SwitchButton.setTooltip(Tooltip.create(getSwitchTooltip(Config.LIDAR2_SWITCH.get())));
    }

    private void handleLidar2FrequencyButtonPress(Button button) {
        LOGGER.info("Lidar2 frequency button pressed!");
        int index = 0;
        for (int i = 0; i < validFrequencies.length; i++) {
            if (validFrequencies[i] == Config.LIDAR2_MEASUREMENT_FREQUENCY_IN_HZ.get()) {
                index = i;
                break;
            }
        }
        index = (index + 1) % validFrequencies.length;
        Config.LIDAR2_MEASUREMENT_FREQUENCY_IN_HZ.set(validFrequencies[index]);
        Lidar2FrequencyButton.setMessage(Component.literal(Config.LIDAR2_MEASUREMENT_FREQUENCY_IN_HZ.get()+" Hz"));
    }

    private void handleLidar3SwitchButtonPress(Button button) {
        LOGGER.info("Lidar3 switch button pressed!");
        Config.LIDAR3_SWITCH.set(!Config.LIDAR3_SWITCH.get());
        Lidar3SwitchButton.setMessage(getSwitchState(Config.LIDAR3_SWITCH.get()));
        Lidar3SwitchButton.setTooltip(Tooltip.create(getSwitchTooltip(Config.LIDAR3_SWITCH.get())));
    }

    private void handleLidar3FrequencyButtonPress(Button button) {
        LOGGER.info("Lidar3 frequency button pressed!");
        int index = 0;
        for (int i = 0; i < validFrequencies.length; i++) {
            if (validFrequencies[i] == Config.LIDAR3_MEASUREMENT_FREQUENCY_IN_HZ.get()) {
                index = i;
                break;
            }
        }
        index = (index + 1) % validFrequencies.length;
        Config.LIDAR3_MEASUREMENT_FREQUENCY_IN_HZ.set(validFrequencies[index]);
        Lidar3FrequencyButton.setMessage(Component.literal(Config.LIDAR3_MEASUREMENT_FREQUENCY_IN_HZ.get()+" Hz"));
    }

    private void handleImu1SwitchButtonPress(Button button) {
        LOGGER.info("Imu1 switch button pressed!");
        Config.IMU1_SWITCH.set(!Config.IMU1_SWITCH.get());
        IMU1SwitchButton.setMessage(getSwitchState(Config.IMU1_SWITCH.get()));
        IMU1SwitchButton.setTooltip(Tooltip.create(getSwitchTooltip(Config.IMU1_SWITCH.get())));
    }

    private void handleImu1FrequencyButtonPress(Button button) {
        LOGGER.info("Imu1 frequency button pressed!");
        int index = 0;
        for (int i = 0; i < validFrequencies.length; i++) {
            if (validFrequencies[i] == Config.IMU1_MEAUREMENT_FREQUENCY_IN_HZ.get()) {
                index = i;
                break;
            }
        }
        index = (index + 1) % validFrequencies.length;
        Config.IMU1_MEAUREMENT_FREQUENCY_IN_HZ.set(validFrequencies[index]);
        IMU1FrequencyButton.setMessage(Component.literal(Config.IMU1_MEAUREMENT_FREQUENCY_IN_HZ.get()+" Hz"));
    }

}