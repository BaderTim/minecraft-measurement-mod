package io.github.mmm.measurement;

import io.github.mmm.measurement.objects.Scan;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

import static io.github.mmm.MMM.MEASUREMENT_CONTROLLER;
import static io.github.mmm.MMM.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Measure {

    private static int passedTicks = 0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && MEASUREMENT_CONTROLLER.isCurrentlyMeasuring()) {
            MEASUREMENT_CONTROLLER.getLidarController().scan(passedTicks);
            if (passedTicks % MEASUREMENT_CONTROLLER.getSaveInterval() == 0) {
                ArrayList<Scan>[] scans = MEASUREMENT_CONTROLLER.getLidarController().getScans();
                for (int i = 0; i < scans.length; i++) {
                    MEASUREMENT_CONTROLLER.saveLiDARScansToFile(scans[i], "lidar" + (i + 1) + ".csv");
                }
                // TODO: implement IMU

                MEASUREMENT_CONTROLLER.getLidarController().clearScans();
            }
            if (passedTicks == Integer.MAX_VALUE) passedTicks = 0;
            passedTicks++;
        }
    }

}
