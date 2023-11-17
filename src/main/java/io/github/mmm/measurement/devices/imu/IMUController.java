package io.github.mmm.measurement.devices.imu;

import io.github.mmm.measurement.objects.ImuScan;
import org.joml.Vector3f;

import java.util.ArrayList;

import static io.github.mmm.MMM.MEASUREMENT_CONTROLLER;

public class IMUController {

    private final IMU imu;
    private ArrayList<ImuScan> scans;
    private int passedTicks;

    public IMUController(IMU imu) {
        this.imu = imu;
        this.scans = new ArrayList<>();
    }

    public void scan(Vector3f positionCurrent, Vector3f positionPrevious,
                     Vector3f rotationCurrent, Vector3f rotationPrevious) {
        this.passedTicks = 0;
        this.executeScan(positionCurrent, positionPrevious, rotationCurrent, rotationPrevious);
    }

    public void scan(int passedTicks, Vector3f positionCurrent, Vector3f positionPrevious,
                     Vector3f rotationCurrent, Vector3f rotationPrevious) {
        this.passedTicks = passedTicks;
        this.executeScan(positionCurrent, positionPrevious, rotationCurrent, rotationPrevious);
    }

    private void executeScan(Vector3f positionCurrent, Vector3f positionPrevious,
                             Vector3f rotationCurrent, Vector3f rotationPrevious) {
        if(!this.isImuAllowedToScan()) {
            return;
        }
        ImuScan currentScan = imu.getScan(positionCurrent, positionPrevious, rotationCurrent, rotationPrevious);
        this.scans.add(currentScan);
    }

    private boolean isImuAllowedToScan() {
        return imu != null && passedTicks % MEASUREMENT_CONTROLLER.getFrequencyInTicks(imu.getFrequency()) == 0;
    }

    public ArrayList<ImuScan> getScans() {
        return scans;
    }

    public void clearScans() {
        this.scans = new ArrayList<>();
    }
}
