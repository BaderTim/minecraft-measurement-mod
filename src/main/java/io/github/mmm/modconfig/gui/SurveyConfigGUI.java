package io.github.mmm.modconfig.gui;

import io.github.mmm.MMM;
import io.github.mmm.measurement.survey.objects.Survey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static io.github.mmm.MMM.SURVEY_CONTROLLER;
import static net.minecraft.util.CommonColors.GRAY;
import static net.minecraft.util.CommonColors.WHITE;
import static net.minecraft.util.CommonColors.RED;

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

    // back button
    private static final Component BACK = Component.translatable("gui." + MMM.MODID + ".settings.survey.back.name");
    private static Button BackButton;

    // forward button
    private static final Component FORWARD = Component.translatable("gui." + MMM.MODID + ".settings.survey.forward.name");
    private static Button ForwardButton;

    // number selector button
    private static final Component NUMBER = Component.translatable("gui." + MMM.MODID + ".settings.survey.number.name");
    private static Button NumberButton;
    // number selector edit box
    private static EditBox NumberEditBox;

    // set vertex as head button
    private static final Component SET_HEAD = Component.translatable("gui." + MMM.MODID + ".settings.survey.sethead.name");
    private static Button SetHeadButton;

    private static int vertexIndex = 0;

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


        if(SURVEY_CONTROLLER.isCurrentlySurveying() && !SURVEY_CONTROLLER.getSurvey().getVertices().isEmpty()) {
            Survey survey = SURVEY_CONTROLLER.getSurvey();
            vertexIndex = survey.getPositionVertexIndex();

            // back
            BackButton = Button.builder(BACK, this::handleBackButtonPress)
                    .bounds(10, 70, 50, 20)
                    .build();
            addRenderableWidget(BackButton);

            // forward
            ForwardButton = Button.builder(FORWARD, this::handleForwardButtonPress)
                    .bounds(120, 70, 50, 20)
                    .build();
            addRenderableWidget(ForwardButton);

            // number edit box
            NumberEditBox = new EditBox(
                    this.font, 200, 70, 40, 20,
                    null);
            NumberEditBox.setMaxLength(4);
            NumberEditBox.setValue("1");
            addRenderableWidget(NumberEditBox);
            NumberButton = Button.builder(NUMBER, this::handleNumberButtonPress)
                    .bounds(245, 70, 40, 20)
                    .build();
            addRenderableWidget(NumberButton);

            // set head
            SetHeadButton = Button.builder(SET_HEAD, this::handleSetHeadButtonPress)
                    .bounds(10, 140, 140, 20)
                    .build();
            addRenderableWidget(SetHeadButton);

        }
    }

    private void handleSetHeadButtonPress(Button button) {
        if(SURVEY_CONTROLLER.isCurrentlySurveying()) {
            Survey survey = SURVEY_CONTROLLER.getSurvey();
            if (!survey.getVertices().isEmpty()) {
                survey.setPositionVertexIndex(vertexIndex);
            }
        }
    }

    private void handleNumberButtonPress(Button button) {
        if(SURVEY_CONTROLLER.isCurrentlySurveying()) {
            Survey survey = SURVEY_CONTROLLER.getSurvey();
            if (!survey.getVertices().isEmpty()) {
                int number = Integer.parseInt(NumberEditBox.getValue());
                if (number > 0 && number <= survey.getVertices().size()) {
                    vertexIndex = number - 1;
                }
            }
        }
    }

    private void handleForwardButtonPress(Button button) {
        if(SURVEY_CONTROLLER.isCurrentlySurveying()) {
            Survey survey = SURVEY_CONTROLLER.getSurvey();
            if (!survey.getVertices().isEmpty()) {
                if (vertexIndex < survey.getVertices().size() - 1) {
                    vertexIndex++;
                }
            }
        }
    }

    private void handleBackButtonPress(Button button) {
        if(SURVEY_CONTROLLER.isCurrentlySurveying()) {
            Survey survey = SURVEY_CONTROLLER.getSurvey();
            if (!survey.getVertices().isEmpty()) {
                if (vertexIndex > 0) {
                    vertexIndex--;
                }
            }
        }
    }

    private void handleExitButtonPress(Button button) {
        this.onClose();
    }

    private void handleDeviceButtonPress(Button button) {
        this.close();
        Minecraft.getInstance().setScreen(new DeviceConfigGUI());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        this.renderDirtBackground(graphics);

        super.render(graphics, mouseX, mouseY, partialTick);

        // title
        graphics.drawCenteredString(this.font, TITLE, this.width / 2, 17, WHITE);

        if (SURVEY_CONTROLLER.isCurrentlySurveying() && !SURVEY_CONTROLLER.getSurvey().getVertices().isEmpty()) {
            Survey survey = SURVEY_CONTROLLER.getSurvey();
            String index = vertexIndex + 1 + "/" + survey.getVertices().size();
            String posX = Math.round(survey.getVertex(vertexIndex).getPosition().x*100)/100.0 + "";
            String posY = Math.round(survey.getVertex(vertexIndex).getPosition().y*100)/100.0 + "";
            String posZ = Math.round(survey.getVertex(vertexIndex).getPosition().z*100)/100.0 + "";
            String position = "X:  " + posX + " Y:  " + posY + " Z:  " + posZ;
            graphics.drawCenteredString(this.font, index, 90, 77, GRAY);
            graphics.drawString(this.font, position, 10, 100, WHITE);
            if (vertexIndex == survey.getPositionVertexIndex()) {
                graphics.drawString(this.font, "Current Head", 10, 120, RED);
            } else {
                graphics.drawString(this.font, "Not Head", 10, 120, GRAY);
            }
        } else if(SURVEY_CONTROLLER.isCurrentlySurveying() && SURVEY_CONTROLLER.getSurvey().getVertices().isEmpty()) {
            graphics.drawString(this.font, "Survey has no data yet.", 10, 77, GRAY);
        } else {
            graphics.drawString(this.font, "Survey Mode not active.", 10, 77, RED);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public void onClose() {
        this.close();
    }

    private void close() {
        MMM.latestConfigGUI = MMM.ConfigGUIType.SURVEY;
        super.onClose();
    }
}
