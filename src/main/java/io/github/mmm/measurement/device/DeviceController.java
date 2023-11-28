package io.github.mmm.measurement.device;

import io.github.mmm.MMM;
import io.github.mmm.measurement.device.objects.imu.IMU;
import io.github.mmm.measurement.device.objects.imu.IMUController;
import io.github.mmm.measurement.device.objects.lidar.LiDAR;
import io.github.mmm.measurement.device.objects.lidar.LiDARController;
import io.github.mmm.measurement.device.scans.ImuScan;
import io.github.mmm.measurement.device.scans.LidarScan;
import io.github.mmm.measurement.device.scans.LidarScan2D;
import io.github.mmm.modconfig.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static io.github.mmm.MMM.DEVICE_CONTROLLER;
import static io.github.mmm.MMM.MMM_ROOT_PATH;
import static io.github.mmm.Utils.saveStringToFile;

public class DeviceController {

    private Boolean currentlyMeasuring;
    private int saveInterval;

    private String savePath;

    private boolean tickTimeWarning = false;
    private int tickTimeWarningTolerance;

    private LiDARController lidarController;
    private LiDAR lidar1;
    private LiDAR lidar2;
    private LiDAR lidar3;

    private IMUController imuController;
    private IMU imu1;


    public DeviceController() {
        System.out.println("Measure constructor");
        this.currentlyMeasuring = false;
    }

    public Boolean isCurrentlyMeasuring() {
        return this.currentlyMeasuring;
    }

    public void startMeasure() {
        this.saveInterval = Config.SAVE_INTERVAL.get();
        this.tickTimeWarning = Config.TICK_TIME_WARNING.get();
        this.tickTimeWarningTolerance = Config.TICK_TIME_WARNING_TOLERANCE.get();
        this.lidarController = new LiDARController(new LiDAR[]{lidar1, lidar2, lidar3});
        this.imuController = new IMUController(imu1);

        String startTime = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(LocalDateTime.now());
        this.savePath = "device_measurements/" + startTime;
        try {
            Files.createDirectories(Paths.get(MMM_ROOT_PATH + this.savePath));
        } catch (Exception e) {
            System.out.println("Error creating directory: " + e.getMessage());
        }

        if(Config.LIDAR1_SWITCH.get()) saveStringToFile("timestamp;data\n", this.savePath, "lidar1.csv");
        if(Config.LIDAR2_SWITCH.get()) saveStringToFile("timestamp;data\n", this.savePath, "lidar2.csv");
        if(Config.LIDAR3_SWITCH.get()) saveStringToFile("timestamp;data\n", this.savePath, "lidar3.csv");
        if(Config.IMU1_SWITCH.get()) saveStringToFile("timestamp;accX;accY;accZ,gyroX;gyroY;gyroZ\n", this.savePath, "imu1.csv");

        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.start"), false);
        this.currentlyMeasuring = true;
    }

    public void stopMeasure() {
        // save remaining scans
        ArrayList<LidarScan>[] scans = DEVICE_CONTROLLER.getLidarController().getScans();
        for (int i = 0; i < scans.length; i++) {
            DEVICE_CONTROLLER.saveLiDARScansToFile(scans[i], "lidar" + (i + 1) + ".csv");
        }
        DEVICE_CONTROLLER.getLidarController().clearScans();
        ArrayList<ImuScan> imuScans = DEVICE_CONTROLLER.getImuController().getScans();
        DEVICE_CONTROLLER.saveIMUScansToFile(imuScans, "imu1.csv");
        // send stop message
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.stop"), false);
        // reset
        this.currentlyMeasuring = false;
        this.lidarController = null;
        this.imuController = null;
    }

    public void initDevices() {        this.initLidars();
        this.initImus();
    }

    private void initLidars() {
        LocalPlayer player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;
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
                    level,
                    new Color(170, 0, 170)
            );
        } else {
            this.lidar1 = null;
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
                    level,
                    new Color(255, 170, 0)
            );
        } else {
            this.lidar2 = null;
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
                    level,
                    new Color(0, 170, 170)
            );
        } else {
            this.lidar3 = null;
        }
    }

    private void initImus() {
        if (Config.IMU1_SWITCH.get()) {
            this.imu1 = new IMU(
                    Config.IMU1_CONSIDER_GRAVITY.get(),
                    Config.IMU1_YAW_OFFSET_FROM_POV_IN_DEG.get(),
                    Config.IMU1_PITCH_OFFSET_FROM_POV_IN_DEG.get(),
                    Config.IMU1_ROLL_OFFSET_FROM_POV_IN_DEG.get(),
                    Config.IMU1_MEAUREMENT_FREQUENCY_IN_HZ.get()
            );
        } else {
            this.imu1 = null;
        }
    }

    public void saveLiDARScansToFile(ArrayList<LidarScan> scans, String fileName) {
        String scansAsString = "";
        int nullCounter = 0;
        for(int i = 0; i < scans.size(); i++) {
            if(scans.get(i) == null) {
                nullCounter++;
                continue;
            }
            scansAsString += this.getLidarScanAsSaveString(scans.get(i));
        }
        if (nullCounter == scans.size()) return;
        saveStringToFile(scansAsString, this.savePath, fileName);
    }

    public void saveLiDARScanToFile(LidarScan scan, String fileName) {
        String result = this.getLidarScanAsSaveString(scan);
        saveStringToFile(result, this.savePath, fileName);
    }

    private String getLidarScanAsSaveString(LidarScan scan) {
        String distancesString = "";
        if(scan.is2D()) {
            distancesString = java.util.Arrays.toString(scan.getScan2D().getDistances());
        } else {
            LidarScan2D[] distances = scan.getScan3D().getDistances();
            for (int i = 0; i < distances.length - 1; i++) {
                distancesString += java.util.Arrays.toString(distances[i].getDistances()) + ",";
            }
            distancesString = "[" + distancesString + java.util.Arrays.toString(distances[distances.length - 1].getDistances()) + "]";
        }
        return scan.getTimestamp() + ";" + distancesString + "\n";
    }

    public void saveIMUScansToFile(ArrayList<ImuScan> scans, String fileName) {
        if (scans.size() == 0) return;
        String imuString = "";
        for(int i = 0; i < scans.size(); i++) {
            imuString += this.getImuScanAsSaveString(scans.get(i));
        }
        saveStringToFile(imuString, this.savePath, fileName);
    }

    private String getImuScanAsSaveString(ImuScan scan) {
        return scan.getTimestamp() + ";" + scan.getAccX() + ";" + scan.getAccY() + ";" + scan.getAccZ() + ";" + scan.getGyroX() + ";" + scan.getGyroY() + ";" + scan.getGyroZ() + "\n";
    }

    public int getFrequencyInTicks(int frequency) {
        return 20 / frequency;
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

    public boolean isTickTimeWarning() {
        return tickTimeWarning;
    }

    public int getTickTimeWarningTolerance() {
        return tickTimeWarningTolerance;
    }

    public IMUController getImuController() {
        return imuController;
    }
}
