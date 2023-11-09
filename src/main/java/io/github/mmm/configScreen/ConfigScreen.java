package io.github.mmm.configScreen;

import io.github.mmm.MMM;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static io.github.mmm.MMM.LOGGER;
import static net.minecraft.util.CommonColors.GRAY;
import static net.minecraft.util.CommonColors.WHITE;

public class ConfigScreen extends Screen {

    // title
    private static final Component TITLE = Component.translatable("gui." + MMM.MODID + ".settings.title");
    private static final Component DESCRIPTION = Component.translatable("gui." + MMM.MODID + ".settings.text.description");

    // save
    private static final Component SAVE = Component.translatable("gui." + MMM.MODID + ".settings.exit.name");
    private static final Component SAVE_TOOLTIP = Component.translatable("gui." + MMM.MODID + ".settings.exit.tooltip");

    // switch button
    private static final Component SWITCH_ACTIVE = Component.translatable("gui." + MMM.MODID + ".settings.switch.active");
    private static final Component SWITCH_INACTIVE = Component.translatable("gui." + MMM.MODID + ".settings.switch.inactive");
    private static final Component SWITCH_TOOLTIP_DEACTIVATE = Component.translatable("gui." + MMM.MODID + ".settings.switch.tooltip.deactivate");
    private static final Component SWITCH_TOOLTIP_ACTIVATE = Component.translatable("gui." + MMM.MODID + ".settings.switch.tooltip.activate");

    // lidar
    private static final Component LIDAR = Component.translatable("gui." + MMM.MODID + ".settings.lidar");
    private static final Component LIDAR1_NAME = Component.translatable("gui." + MMM.MODID + ".settings.lidar1.name");
    private static final Component LIDAR2_NAME = Component.translatable("gui." + MMM.MODID + ".settings.lidar2.name");
    private static final Component LIDAR3_NAME = Component.translatable("gui." + MMM.MODID + ".settings.lidar3.name");

    // imu
    private static final Component IMU = Component.translatable("gui." + MMM.MODID + ".settings.imu");
    private static final Component IMU1NAME = Component.translatable("gui." + MMM.MODID + ".settings.imu1.name");

    // bottom hints
    private static final Component CONTROLS = Component.translatable("gui." + MMM.MODID + ".settings.controls");
    private static final Component SAVEPATH = Component.translatable("gui." + MMM.MODID + ".settings.savepath");

    public ConfigScreen() {
        // Use the super class' constructor to set the screen's title
        super(TITLE);
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(
                Button.builder(SAVE, this::handleExampleButtonPress)
                        .bounds(this.width-100-10, 10, 100, 20)
                        .tooltip(Tooltip.create(SAVE_TOOLTIP))
                        .build()
        );
        // lidar1
        addRenderableWidget(
                Button.builder(SWITCH_ACTIVE, this::handleExampleButtonPress)
                        .bounds(50, 75, 50, 20)
                        .tooltip(Tooltip.create(SWITCH_TOOLTIP_DEACTIVATE))
                        .build()
        );
        // lidar2
        addRenderableWidget(
                Button.builder(SWITCH_INACTIVE, this::handleExampleButtonPress)
                        .bounds(50, 105, 50, 20)
                        .tooltip(Tooltip.create(SWITCH_TOOLTIP_ACTIVATE))
                        .build()
        );
        // lidar3
        addRenderableWidget(
                Button.builder(SWITCH_INACTIVE, this::handleExampleButtonPress)
                        .bounds(50, 135, 50, 20)
                        .tooltip(Tooltip.create(SWITCH_TOOLTIP_ACTIVATE))
                        .build()
        );
        // imu
        addRenderableWidget(
                Button.builder(SWITCH_ACTIVE, this::handleExampleButtonPress)
                        .bounds(50, 180, 50, 20)
                        .tooltip(Tooltip.create(SWITCH_TOOLTIP_DEACTIVATE))
                        .build()
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        this.renderDirtBackground(graphics);

        super.render(graphics, mouseX, mouseY, partialTick);

        // title
        graphics.drawCenteredString(this.font, TITLE, this.width/2, 20, WHITE);
        graphics.drawCenteredString(this.font, DESCRIPTION, this.width/2, 35, WHITE);

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

    private void handleExampleButtonPress(Button button) {
        LOGGER.info("Button pressed again!");
    }

}