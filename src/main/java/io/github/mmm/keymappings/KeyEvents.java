package io.github.mmm.keymappings;

import io.github.mmm.MMM;
import io.github.mmm.modconfig.gui.DeviceConfigGUI;
import io.github.mmm.modconfig.gui.SurveyConfigGUI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.mmm.MMM.*;
import static io.github.mmm.keymappings.KeyDefinitions.*;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyEvents {

    // Listen to Key Bindings
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Only call code once as the tick event is called twice every tick
        if (event.phase == TickEvent.Phase.END) {
            if (MEASURE_MAPPING.get().consumeClick()) {
                LOGGER.info("MEASURE_START_STOP_MAPPING is pressed");
                if (MEASUREMENT_CONTROLLER.isCurrentlyMeasuring()) {
                    MEASUREMENT_CONTROLLER.stopMeasure();
                } else {
                    MEASUREMENT_CONTROLLER.startMeasure();
                }
            } else if (SETTINGS_MAPPING.get().consumeClick()) {
                LOGGER.info("SETTINGS_MAPPING is pressed");
                if(latestConfigGUI == ConfigGUIType.DEVICE) {
                    Minecraft.getInstance().setScreen(new DeviceConfigGUI());
                } else if(latestConfigGUI == ConfigGUIType.SURVEY) {
                    Minecraft.getInstance().setScreen(new SurveyConfigGUI());
                }
            } else if (VISUALIZE_MAPPING.get().consumeClick()) {
                LOGGER.info("VISUALIZE_MAPPING is pressed");
            }
        }
    }

}
