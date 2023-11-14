package io.github.mmm.measurement.devices.lidar;

import io.github.mmm.measurement.objects.Scan;
import io.github.mmm.modconfig.Config;

public class LiDARController {

    private final LiDAR[] lidars;
    private int threadCount;

    public LiDARController(LiDAR[] lidars) {
        if (Config.MULTITHREAD_SWITCH.get()) {
            this.threadCount = Config.THREAD_COUNT_MULTIPLIER.get() * Runtime.getRuntime().availableProcessors();
        } else {
            this.threadCount = 1;
        }
        this.lidars = lidars;
    }

    public Scan[] getScansFromPOVToBlocks() {
        Scan[] scans = new Scan[this.lidars.length];
        if(this.threadCount == 1) {
            for (int i = 0; i < this.lidars.length; i++) {
                if(this.lidars[i] != null) {
                    scans[i] = this.lidars[i].scanFromPOVToBlocks();
                } else {
                    scans[i] = null;
                }
            }
            return scans;
        }
        return scans;
    }

}
