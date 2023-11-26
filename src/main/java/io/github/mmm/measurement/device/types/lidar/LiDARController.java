package io.github.mmm.measurement.device.types.lidar;

import io.github.mmm.measurement.device.types.lidar.multithread.SubLiDARPipeline;
import io.github.mmm.measurement.device.objects.LidarScan;
import io.github.mmm.measurement.device.objects.LidarScan2D;
import io.github.mmm.measurement.device.objects.LidarScan3D;
import io.github.mmm.modconfig.Config;

import java.util.ArrayList;

import static io.github.mmm.MMM.DEVICE_CONTROLLER;

public class LiDARController {

    private final LiDAR[] lidars;
    private ArrayList<LidarScan>[] scans;
    private int maxThreadCount;
    private int passedTicks;

    public LiDARController(LiDAR[] lidars) {
        if (Config.MULTITHREAD_SWITCH.get()) {
            this.maxThreadCount = Config.THREAD_COUNT_MULTIPLIER.get() * Runtime.getRuntime().availableProcessors();
        } else {
            this.maxThreadCount = 1;
        }
        this.lidars = lidars;
        this.scans = new ArrayList[this.lidars.length];
        for(int i = 0; i < this.lidars.length; i++) {
            this.scans[i] = new ArrayList<>();
        }
    }

    public ArrayList<LidarScan>[] getScans() {
        return this.scans;
    }

    public void clearScans() {
        this.scans = new ArrayList[this.lidars.length];
        for(int i = 0; i < this.lidars.length; i++) {
            this.scans[i] = new ArrayList<>();
        }
    }

    public void scan() {
        this.passedTicks = 0;
        this.executeScan();
    }

    public void scan(int passedTicks) {
        this.passedTicks = passedTicks;
        this.executeScan();
    }

    private void executeScan() {
        LidarScan[] currentScans;
        if(this.maxThreadCount == 1) {
            currentScans = this.getScansSingleThreaded();
        } else {
            currentScans = this.getScansMultiThreaded();
        }
        for(int i = 0; i < currentScans.length; i++) {
            this.scans[i].add(currentScans[i]);
        }
    }

    private LidarScan[] getScansSingleThreaded() {
        LidarScan[] scans = new LidarScan[this.lidars.length];
        for (int i = 0; i < this.lidars.length; i++) {
            if(this.isLidarAllowedToScan(this.lidars[i])) {
                scans[i] = this.lidars[i].scanFromPOVToBlocks();
            } else {
                scans[i] = null;
            }
        }
        return scans;
    }

    private LidarScan[] getScansMultiThreaded() {
        int[] subLidarsPerLidar = new int[this.lidars.length];
        ArrayList<LiDAR> subLidars = new ArrayList<>();
        // slice main lidars into sublidars
        for(int l = 0; l < this.lidars.length; l++) {
            LiDAR lidar = this.lidars[l];
            if(!this.isLidarAllowedToScan(lidar)) continue;
            ArrayList<LiDAR> newSubLidars;
            if(lidar.getVerticalScanRadiusInDeg() == 0) { // 2D
                newSubLidars = this.getSubLidarsFrom2DLidar(lidar);
            } else { // 3D
                newSubLidars = this.getSubLidarsFrom3DLidar(lidar);
            }
            subLidarsPerLidar[l] = newSubLidars.size();
            subLidars.addAll(newSubLidars);
        }
        int threadCount = Math.min(this.maxThreadCount, subLidars.size());
        int subLidarsPerThread = (int)Math.ceil((double)subLidars.size() / threadCount);
        // create threads
        SubLiDARPipeline[] pipelines = new SubLiDARPipeline[threadCount];
        Thread[] threads = new Thread[threadCount];
        for(int i = 0; i < threadCount; i++) {
            // get sublidars for thread
            LiDAR[] subLidarsForThread = new LiDAR[subLidarsPerThread];
            for(int j = 0; j < subLidarsPerThread; j++) {
                int index = i * subLidarsPerThread + j;
                if(index >= subLidars.size()) break;
                subLidarsForThread[j] = subLidars.get(index);
            }
            // create and start thread
            pipelines[i] = new SubLiDARPipeline(subLidarsForThread);
            threads[i] = new Thread(pipelines[i]);
            threads[i].start();
        }
        // join threads
        try {
            for(int i = 0; i < threadCount; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // get scans from threads
        int parsedSubLidars = 0;
        int currentLidar = 0;
        LidarScan[] scans = new LidarScan[this.lidars.length];
        ArrayList<LidarScan> scansForLidar = new ArrayList<>();
        for(int i = 0; i < pipelines.length; i++) { // loop through threads
            LidarScan[] subScans = pipelines[i].getScans();
            for(int s=0; s < subScans.length; s++) { // loop through every scan of current thread
                if(subScans[s] == null) break;
                if(parsedSubLidars == subLidarsPerLidar[currentLidar]) {
                    parsedSubLidars = 0;
                    if(!this.isLidarAllowedToScan(this.lidars[currentLidar])) {
                        currentLidar++;
                        break;
                    }
                    LidarScan[] scansForLidarTemp = new LidarScan[scansForLidar.size()];
                    scansForLidarTemp = scansForLidar.toArray(scansForLidarTemp);
                    scans[currentLidar] = this.buildScanFromSubScans(
                            scansForLidarTemp,
                            this.lidars[currentLidar]
                    );
                    currentLidar++;
                    scansForLidar.clear();
                }
                scansForLidar.add(subScans[s]);
                parsedSubLidars++;
            }
        }
        // get last scan
        if(parsedSubLidars == subLidarsPerLidar[currentLidar]) {
            if(this.isLidarAllowedToScan(this.lidars[currentLidar])) {
                LidarScan[] scansForLidarTemp = new LidarScan[scansForLidar.size()];
                scansForLidarTemp = scansForLidar.toArray(scansForLidarTemp);
                scans[currentLidar] = this.buildScanFromSubScans(
                        scansForLidarTemp,
                        this.lidars[currentLidar]
                );
            }
        }
        return scans;
    }

    private ArrayList<LiDAR> getSubLidarsFrom2DLidar(LiDAR lidar2d) {
        // TODO: implement for (very large) 2D lidars
        ArrayList<LiDAR> subLidars = new ArrayList<>();
        subLidars.add(lidar2d);
        return subLidars;
    }

    private ArrayList<LiDAR> getSubLidarsFrom3DLidar(LiDAR lidar3d) {
        ArrayList<LiDAR> subLidars = new ArrayList<>();

        float verticalScanAngleDifferenceInDeg = lidar3d.getVerticalScanRadiusInDeg() / lidar3d.getVerticalScansPerRadius();
        float pitchFromPOVInDegOffset = lidar3d.getPitchFromPOVInDeg() + (lidar3d.getVerticalScanRadiusInDeg() / 2); // start at the top of the scan
        for(int i = 0; i < lidar3d.getVerticalScansPerRadius(); i++) {
            float pitchFromPOVFor3DScanInDeg2D = pitchFromPOVInDegOffset - i * verticalScanAngleDifferenceInDeg;
            subLidars.add(new LiDAR( // just spam 2d lidars lol
                    lidar3d.getHorizontalScanRadiusInDeg(),
                    lidar3d.getHorizontalScansPerRadius(),
                    lidar3d.getYawFromPOVInDeg(),
                    pitchFromPOVFor3DScanInDeg2D,
                    lidar3d.getRollFromPOVInDeg(),
                    lidar3d.getMaximumMeasurementDistanceInMeters(),
                    lidar3d.getPlayer(),
                    lidar3d.getLevel(),
                    null
            ));
        }
        return subLidars;
    }

    private LidarScan buildScanFromSubScans(LidarScan[] subScans, LiDAR lidar) {
        if(lidar.getVerticalScanRadiusInDeg() == 0) { // 2D
            return this.build2DScanFromSubScans(subScans, lidar);
        } else { // 3D
            return this.build3DScanFromSubScans(subScans, lidar);
        }
    }

    private LidarScan build2DScanFromSubScans(LidarScan[] subScans, LiDAR lidar) {
        if (subScans.length == 1) {
            return subScans[0];
        }
        LidarScan2D scan2D = new LidarScan2D(
                lidar.getHorizontalScansPerRadius()
        );
        int currentScan = 0;
        for(LidarScan scan : subScans) {
            if(scan == null) break;
            LidarScan2D scan2DToAdd = scan.getScan2D();
            for(int i = 0; i < scan2DToAdd.getScansPerRadius(); i++) {
                scan2D.setDistance(currentScan, scan2DToAdd.getDistance(i));
                currentScan++;
            }
        }
        return new LidarScan(scan2D);
    }

    private LidarScan build3DScanFromSubScans(LidarScan[] subScans, LiDAR lidar) {
        // subScans are 2D scans
        LidarScan3D scan3D = new LidarScan3D(
                lidar.getHorizontalScansPerRadius(),
                lidar.getVerticalScansPerRadius()
        );
        for(int i = 0; i < subScans.length; i++) {
            scan3D.setScan2D(i, subScans[i].getScan2D());
        }
        return new LidarScan(scan3D);
    }

    private boolean isLidarAllowedToScan(LiDAR lidar) {
        return lidar != null && this.passedTicks % DEVICE_CONTROLLER.getFrequencyInTicks(lidar.getFrequency()) == 0;
    }

}
