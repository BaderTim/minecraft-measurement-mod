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

public class MeasurementController {

    private Boolean currentlyMeasuring;

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
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        if (Config.LIDAR1_SWITCH.get()) {
            this.lidar1 = new LiDAR(
                    180,
                    60,
                    180,
                    60,
                    0,
                    0,
                    0,
                    100,
                    player,
                    Minecraft.getInstance().level
            );
        }
        if (Config.LIDAR2_SWITCH.get()) {
            this.lidar2 = new LiDAR(
                    60,
                    120,
                    180,
                    360,
                    0,
                    0,
                    0,
                    100,
                    player,
                    Minecraft.getInstance().level
            );
        }
        if (Config.LIDAR3_SWITCH.get()) {
            this.lidar3 = new LiDAR(
                    60,
                    120,
                    180,
                    360,
                    0,
                    0,
                    0,
                    100,
                    player,
                    Minecraft.getInstance().level
            );
        }
        this.lidarController = new LiDARController(new LiDAR[]{lidar1, lidar2, lidar3});
        if (Config.IMU1_SWITCH.get()) {
            this.imu1 = new IMU();
        }

        if(lidar1 != null) this.saveStringToFile("timestamp;data\n", "lidar1.csv");
        if(lidar2 != null) this.saveStringToFile("timestamp;data\n", "lidar2.csv");
        if(lidar3 != null) this.saveStringToFile("timestamp;data\n", "lidar3.csv");
        if(imu1 != null) this.saveStringToFile("timestamp;accX;accY;accZ,gyroX;gyroY;gyroZ;magX;magY;magZ\n", "imu1.csv");

        this.startTime = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").format(LocalDateTime.now());
        try {
            Files.createDirectories(Paths.get(this.FILE_PATH+this.startTime));
        } catch (Exception e) {
            System.out.println("Error creating directory: " + e.getMessage());
        }

        player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.start"), false);
        this.currentlyMeasuring = true;
    }

    public void stopMeasure() {
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.stop"), false);
        this.currentlyMeasuring = false;
        this.lidar1 = null;
        this.lidar2 = null;
        this.lidar3 = null;
        this.imu1 = null;
        this.startTime = null;
    }

    public void saveLiDARScanToFile(Scan scan, String fileName) {
        long timestamp = System.currentTimeMillis();
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
        String result = timestamp + ";" + distancesString + "\n";
        this.saveStringToFile(result, fileName);
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

}
