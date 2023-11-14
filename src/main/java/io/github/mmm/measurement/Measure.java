package io.github.mmm.measurement;

import io.github.mmm.measurement.objects.Scan;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.mmm.MMM.MEASUREMENT_CONTROLLER;
import static io.github.mmm.MMM.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Measure {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && MEASUREMENT_CONTROLLER.isCurrentlyMeasuring()) {
            Scan[] scans = MEASUREMENT_CONTROLLER.getLidarController().getScansFromPOVToBlocks();
            for (int i = 0; i < scans.length; i++) {
                if(scans[i] != null) {
                    MEASUREMENT_CONTROLLER.saveLiDARScanToFile(scans[i], "lidar" + i + ".csv");
                }
            }
            // TODO: implement IMU
        }
    }

}
