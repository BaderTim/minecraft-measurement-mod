package io.github.mmm.measurement;

import io.github.mmm.measurement.objects.Scan;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.mmm.MMM.MEASUREMENT_MANAGER;
import static io.github.mmm.MMM.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Measure {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && MEASUREMENT_MANAGER.isCurrentlyMeasuring()) {

            if(MEASUREMENT_MANAGER.isLidar1Active()) {
                Scan lidar1Scan = MEASUREMENT_MANAGER.getLidar1().scanFromPOVToBlocks();
                MEASUREMENT_MANAGER.saveLiDARScanToFile(lidar1Scan, "lidar1.csv");
            }
            if(MEASUREMENT_MANAGER.isLidar2Active()) {
                Scan lidar2Scan = MEASUREMENT_MANAGER.getLidar2().scanFromPOVToBlocks();
                MEASUREMENT_MANAGER.saveLiDARScanToFile(lidar2Scan, "lidar2.csv");
            }
            if(MEASUREMENT_MANAGER.isLidar3Active()) {
                Scan lidar3Scan = MEASUREMENT_MANAGER.getLidar3().scanFromPOVToBlocks();
                MEASUREMENT_MANAGER.saveLiDARScanToFile(lidar3Scan, "lidar3.csv");
            }
            if(MEASUREMENT_MANAGER.isImu1Active()) {
                // TODO: implement IMU
            }

        }
    }

}
