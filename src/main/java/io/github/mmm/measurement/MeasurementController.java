package io.github.mmm.measurement;

import io.github.mmm.MMM;
import io.github.mmm.measurement.devices.IMU;
import io.github.mmm.measurement.devices.lidar.LiDAR;
import io.github.mmm.measurement.devices.lidar.LiDARController;
import io.github.mmm.measurement.objects.Scan;
import io.github.mmm.measurement.objects.Scan2D;
import io.github.mmm.modconfig.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static io.github.mmm.MMM.MEASUREMENT_CONTROLLER;

public class MeasurementController {

    private Boolean currentlyMeasuring;
    private int saveInterval;

    private final String FILE_PATH = Minecraft.getInstance().gameDirectory.getPath() + "/mmm_data/";
    private String startTime;

    private LiDARController lidarController;
    private LiDAR lidar1;
    private LiDAR lidar2;
    private LiDAR lidar3;
    private IMU imu1;

    public MeasurementController() {
        System.out.println("Measure constructor");
        this.currentlyMeasuring = false;
        try {
            System.out.println("Creating directory: " + this.FILE_PATH);
            Files.createDirectories(Paths.get(this.FILE_PATH));
        } catch (Exception e) {
            System.out.println("Error creating directory: " + e.getMessage());
        }
    }

    public Boolean isCurrentlyMeasuring() {
        return this.currentlyMeasuring;
    }

    public void startMeasure() {
        this.saveInterval = Config.SAVE_INTERVAL.get();
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        if (Config.LIDAR1_SWITCH.get()) {
            this.lidar1 = new LiDAR(
                    (float)Config.LIDAR1_HORIZONTAL_SCANNING_RADIUS_IN_DEG.get(),
                    (float)Config.LIDAR1_VERTICAL_SCANNING_RADIUS_IN_DEG.get(),
                    Config.LIDAR1_HORIZONTAL_SCANS_PER_RADIUS.get(),
                    Config.LIDAR1_VERTICAL_SCANS_PER_RADIUS.get(),
                    (float)Config.LIDAR1_YAW_OFFSET_FROM_POV_IN_DEG.get(),
                    (float)Config.LIDAR1_PITCH_OFFSET_FROM_POV_IN_DEG.get(),
                    (float)Config.LIDAR1_ROLL_OFFSET_FROM_POV_IN_DEG.get(),
                    (float)Config.LIDAR1_MAXIMUM_MEASUREMENT_DISTANCE.get(),
                    Config.LIDAR1_MEASUREMENT_FREQUENCY_IN_HZ.get(),
                    player,
                    Minecraft.getInstance().level
            );
        }
        if (Config.LIDAR2_SWITCH.get()) {
            this.lidar2 = new LiDAR(
                    (float)Config.LIDAR2_HORIZONTAL_SCANNING_RADIUS_IN_DEG.get(),
                    (float)Config.LIDAR2_VERTICAL_SCANNING_RADIUS_IN_DEG.get(),
                    Config.LIDAR2_HORIZONTAL_SCANS_PER_RADIUS.get(),
                    Config.LIDAR2_VERTICAL_SCANS_PER_RADIUS.get(),
                    (float)Config.LIDAR2_YAW_OFFSET_FROM_POV_IN_DEG.get(),
                    (float)Config.LIDAR2_PITCH_OFFSET_FROM_POV_IN_DEG.get(),
                    (float)Config.LIDAR2_ROLL_OFFSET_FROM_POV_IN_DEG.get(),
                    (float)Config.LIDAR2_MAXIMUM_MEASUREMENT_DISTANCE.get(),
                    Config.LIDAR2_MEASUREMENT_FREQUENCY_IN_HZ.get(),
                    player,
                    Minecraft.getInstance().level
            );
        }
        if (Config.LIDAR3_SWITCH.get()) {
            this.lidar3 = new LiDAR(
                    (float)Config.LIDAR3_HORIZONTAL_SCANNING_RADIUS_IN_DEG.get(),
                    (float)Config.LIDAR3_VERTICAL_SCANNING_RADIUS_IN_DEG.get(),
                    Config.LIDAR3_HORIZONTAL_SCANS_PER_RADIUS.get(),
                    Config.LIDAR3_VERTICAL_SCANS_PER_RADIUS.get(),
                    (float)Config.LIDAR3_YAW_OFFSET_FROM_POV_IN_DEG.get(),
                    (float)Config.LIDAR3_PITCH_OFFSET_FROM_POV_IN_DEG.get(),
                    (float)Config.LIDAR3_ROLL_OFFSET_FROM_POV_IN_DEG.get(),
                    (float)Config.LIDAR3_MAXIMUM_MEASUREMENT_DISTANCE.get(),
                    Config.LIDAR3_MEASUREMENT_FREQUENCY_IN_HZ.get(),
                    player,
                    Minecraft.getInstance().level
            );
        }
        this.lidarController = new LiDARController(new LiDAR[]{lidar1, lidar2, lidar3});
        if (Config.IMU1_SWITCH.get()) {
            this.imu1 = new IMU();
        }

        this.startTime = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(LocalDateTime.now());
        try {
            Files.createDirectories(Paths.get(this.FILE_PATH+this.startTime));
        } catch (Exception e) {
            System.out.println("Error creating directory: " + e.getMessage());
        }

        if(lidar1 != null) this.saveStringToFile("timestamp;data\n", "lidar1.csv");
        if(lidar2 != null) this.saveStringToFile("timestamp;data\n", "lidar2.csv");
        if(lidar3 != null) this.saveStringToFile("timestamp;data\n", "lidar3.csv");
        if(imu1 != null) this.saveStringToFile("timestamp;accX;accY;accZ,gyroX;gyroY;gyroZ;magX;magY;magZ\n", "imu1.csv");

        player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.start"), false);
        this.currentlyMeasuring = true;
    }

    public void stopMeasure() {
        // save remaining scans
        ArrayList<Scan>[] scans = MEASUREMENT_CONTROLLER.getLidarController().getScans();
        for (int i = 0; i < scans.length; i++) {
            MEASUREMENT_CONTROLLER.saveLiDARScansToFile(scans[i], "lidar" + (i + 1) + ".csv");
        }
        MEASUREMENT_CONTROLLER.getLidarController().clearScans();
        // send stop message
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.stop"), false);
        // reset
        this.currentlyMeasuring = false;
        this.lidar1 = null;
        this.lidar2 = null;
        this.lidar3 = null;
        this.imu1 = null;
        this.startTime = null;
    }

    public void saveLiDARScansToFile(ArrayList<Scan> scans, String fileName) {
        String scansAsString = "";
        long timestamp = System.currentTimeMillis();
        int nullCounter = 0;
        for(int i = 0; i < scans.size(); i++) {
            if(scans.get(i) == null) {
                nullCounter++;
                continue;
            }
            scansAsString += this.getLidarScanAsSaveString(scans.get(i), timestamp);
        }
        if (nullCounter == scans.size()) return;
        this.saveStringToFile(scansAsString, fileName);
    }

    public void saveLiDARScanToFile(Scan scan, String fileName) {
        long timestamp = System.currentTimeMillis();
        String result = this.getLidarScanAsSaveString(scan, timestamp);
        this.saveStringToFile(result, fileName);
    }

    private String getLidarScanAsSaveString(Scan scan, long timestamp) {
        String distancesString = "";
        if(scan.is2D()) {
            distancesString = java.util.Arrays.toString(scan.getScan2D().getDistances());
        } else {
            Scan2D[] distances = scan.getScan3D().getDistances();
            for (int i = 0; i < distances.length - 1; i++) {
                distancesString += java.util.Arrays.toString(distances[i].getDistances()) + ",";
            }
            distancesString = "[" + distancesString + java.util.Arrays.toString(distances[distances.length - 1].getDistances()) + "]";
        }
        return timestamp + ";" + distancesString + "\n";
    }

    public void saveIMUScanToFile(double accX, double accY, double accZ, double gyroX, double gyroY, double gyroZ, double magX, double magY, double magZ, String fileName) {
        long timestamp = System.currentTimeMillis();
        String result = timestamp + ";" + accX + ";" + accY + ";" + accZ + ";" + gyroX + ";" + gyroY + ";" + gyroZ + ";" + magX + ";" + magY + ";" + magZ + "\n";
        this.saveStringToFile(result, fileName);
    }


    private void saveStringToFile(String newData, String fileName) {
        final String savePath = this.FILE_PATH + this.startTime + "/" + fileName;
        try {
            // Open the file in "rw" mode (read and write)
            RandomAccessFile file = new RandomAccessFile(savePath, "rw");
            // Move the file pointer to the end of the file
            file.seek(file.length());
            // Write the data to the end of the file
            file.writeBytes(newData);
            // Close the file
            file.close();
        } catch (Exception e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public LiDARController getLidarController() {
        return lidarController;
    }

    public LiDAR getLidar1() {
        return lidar1;
    }

    public LiDAR getLidar2() {
        return lidar2;
    }

    public LiDAR getLidar3() {
        return lidar3;
    }

    public IMU getImu1() {
        return imu1;
    }

    public int getSaveInterval() {
        return saveInterval;
    }
}
