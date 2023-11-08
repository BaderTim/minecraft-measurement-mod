package io.github.mmm.keyMappings;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.mmm.MMM.MODID;
import static io.github.mmm.keyMappings.KeyDefinitions.MEASURE_START_STOP_MAPPING;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyEvents {

    // Listen to Key Bindings
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) { // Only call code once as the tick event is called twice every tick
            while (MEASURE_START_STOP_MAPPING.get().consumeClick()) {
                // Execute logic to perform on click here
                System.out.println("key is pressed");
            }
        }
    }

}

