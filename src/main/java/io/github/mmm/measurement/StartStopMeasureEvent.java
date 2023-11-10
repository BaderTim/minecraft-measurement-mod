package io.github.mmm.measurement;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.mmm.MMM.LOGGER;
import static io.github.mmm.MMM.MODID;
import static io.github.mmm.keymappings.KeyDefinitions.MEASURE_MAPPING;
import static io.github.mmm.measurement.Measure.*;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class StartStopMeasureEvent {

    // Listen to Key Bindings
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Only call code once as the tick event is called twice every tick
        if (event.phase == TickEvent.Phase.END) {
            if (MEASURE_MAPPING.get().consumeClick()) {
                LOGGER.info("MEASURE_START_STOP_MAPPING is pressed");
                if(currentlyMeasuring) {
                    stopMeasure();
                } else {
                    startMeasure();
                }
            }
        }
    }

}
