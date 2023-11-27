package io.github.mmm.measurement.device.objects.lidar;

import io.github.mmm.measurement.device.scans.LidarScan;
import io.github.mmm.measurement.device.scans.LidarScan2D;
import io.github.mmm.measurement.device.scans.LidarScan3D;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class LiDAR {

    private float horizontalScanRadiusInDeg;
    private float verticalScanRadiusInDeg;
    private int horizontalScansPerRadius;
    private int verticalScansPerRadius;
    private float yawFromPOVInDeg;
    private float pitchFromPOVInDeg;
    private float rollFromPOVInDeg;
    private float maximumMeasurementDistanceInMeters;
    private int frequency;

    private Player player;
    private ClientLevel level;

    private final Color color;

    // 2D
    public LiDAR(
            float horizontalScanRadiusInDeg,
            int horizontalScansPerRadius,
            float yawFromPOVInDeg,
            float pitchFromPOVInDeg,
            float rollFromPOVInDeg,
            float maximumMeasurementDistanceInMeters,
            Player player,
            ClientLevel level,
            Color color
    ) {
        this.verticalScanRadiusInDeg = 0;
        this.verticalScansPerRadius = 0;
        this.horizontalScanRadiusInDeg = horizontalScanRadiusInDeg;
        this.horizontalScansPerRadius = horizontalScansPerRadius;
        this.yawFromPOVInDeg = yawFromPOVInDeg;
        this.pitchFromPOVInDeg = pitchFromPOVInDeg;
        this.rollFromPOVInDeg = rollFromPOVInDeg;
        this.maximumMeasurementDistanceInMeters = maximumMeasurementDistanceInMeters;
        this.frequency = 1;
        this.player = player;
        this.level = level;
        this.color = color;
    }
    public LiDAR(
            float horizontalScanRadiusInDeg,
            int horizontalScansPerRadius,
            float yawFromPOVInDeg,
            float pitchFromPOVInDeg,
            float rollFromPOVInDeg,
            float maximumMeasurementDistanceInMeters,
            int frequency,
            Player player,
            ClientLevel level,
            Color color
    ) {
        this.verticalScanRadiusInDeg = 0;
        this.verticalScansPerRadius = 0;
        this.horizontalScanRadiusInDeg = horizontalScanRadiusInDeg;
        this.horizontalScansPerRadius = horizontalScansPerRadius;
        this.yawFromPOVInDeg = yawFromPOVInDeg;
        this.pitchFromPOVInDeg = pitchFromPOVInDeg;
        this.rollFromPOVInDeg = rollFromPOVInDeg;
        this.maximumMeasurementDistanceInMeters = maximumMeasurementDistanceInMeters;
        this.frequency = frequency;
        this.player = player;
        this.level = level;
        this.color = color;
    }

    // 3D
    public LiDAR(
            float horizontalScanRadiusInDeg,
            float verticalScanRadiusInDeg,
            int horizontalScansPerRadius,
            int verticalScansPerRadius,
            float yawFromPOVInDeg,
            float pitchFromPOVInDeg,
            float rollFromPOVInDeg,
            float maximumMeasurementDistanceInMeters,
            Player player,
            ClientLevel level,
            Color color
    ) {
        this.horizontalScanRadiusInDeg = horizontalScanRadiusInDeg;
        this.verticalScanRadiusInDeg = verticalScanRadiusInDeg;
        this.horizontalScansPerRadius = horizontalScansPerRadius;
        this.verticalScansPerRadius = verticalScansPerRadius;
        this.yawFromPOVInDeg = yawFromPOVInDeg;
        this.pitchFromPOVInDeg = pitchFromPOVInDeg;
        this.rollFromPOVInDeg = rollFromPOVInDeg;
        this.maximumMeasurementDistanceInMeters = maximumMeasurementDistanceInMeters;
        this.frequency = 1;
        this.player = player;
        this.level = level;
        this.color = color;
    }
    public LiDAR(
            float horizontalScanRadiusInDeg,
            float verticalScanRadiusInDeg,
            int horizontalScansPerRadius,
            int verticalScansPerRadius,
            float yawFromPOVInDeg,
            float pitchFromPOVInDeg,
            float rollFromPOVInDeg,
            float maximumMeasurementDistanceInMeters,
            int frequency,
            Player player,
            ClientLevel level,
            Color color
    ) {
        this.horizontalScanRadiusInDeg = horizontalScanRadiusInDeg;
        this.verticalScanRadiusInDeg = verticalScanRadiusInDeg;
        this.horizontalScansPerRadius = horizontalScansPerRadius;
        this.verticalScansPerRadius = verticalScansPerRadius;
        this.yawFromPOVInDeg = yawFromPOVInDeg;
        this.pitchFromPOVInDeg = pitchFromPOVInDeg;
        this.rollFromPOVInDeg = rollFromPOVInDeg;
        this.maximumMeasurementDistanceInMeters = maximumMeasurementDistanceInMeters;
        this.frequency = frequency;
        this.player = player;
        this.level = level;
        this.color = color;
    }

    public LidarScan performScan() {
        if(verticalScanRadiusInDeg == 0 && verticalScansPerRadius == 0) {
            return new LidarScan(this.get2DScan());
        } else {
            return new LidarScan(this.get3DScan());
        }
    }

    private LidarScan3D get3DScan() {
        float verticalScanAngleDifferenceInDeg = verticalScanRadiusInDeg / verticalScansPerRadius;
        float pitchFromPOVInDegOffset = pitchFromPOVInDeg + (verticalScanRadiusInDeg / 2); // start at the top of the scan
        LidarScan3D scan3D = new LidarScan3D(horizontalScansPerRadius, verticalScansPerRadius);
        for(int i = 0; i < verticalScansPerRadius; i++) {
            float pitchFromPOVFor3DScanInDeg2D = pitchFromPOVInDegOffset - i * verticalScanAngleDifferenceInDeg;
            LidarScan2D scan2D = this.get2DScan(pitchFromPOVFor3DScanInDeg2D);
            scan3D.setScan2D(i, scan2D);
        }
        return scan3D;
    }

    private LidarScan2D get2DScan() {
        return this.get2DScan(0);
    }

    private LidarScan2D get2DScan(float extraPitchFromPOVInDeg) {
        float scanAngleDifferenceInDeg = horizontalScanRadiusInDeg / horizontalScansPerRadius;
        float yawFromPOVInDegOffset = yawFromPOVInDeg - (horizontalScanRadiusInDeg / 2); // start at the left side of the scan

        // setup LOCAL coordinate system with GLOBAL pov coordinates
        // LOCAL x1 x-axis is view vector
        // LOCAL x3 y-axis is up vector
        // LOCAL x2 z-axis is cross product of x-axis and y-axis
        Vec3 localOrigin = player.getEyePosition();
        Vec3 localXdirection = player.getViewVector(1.0F);
        Vec3 localYdirection = player.getUpVector(1.0F);
        Vec3 localZdirection = localXdirection.cross(localYdirection).normalize();

        // Create a matrix representing the transformation from local to global coordinates
        Matrix3f globalToLocalMatrix = new Matrix3f(
                (float) localXdirection.x, (float) localYdirection.x, (float) localZdirection.x,
                (float) localXdirection.y, (float) localYdirection.y, (float) localZdirection.y,
                (float) localXdirection.z, (float) localYdirection.z, (float) localZdirection.z
        );
        Matrix3f localToGlobalMatrix = new Matrix3f();
        globalToLocalMatrix.invert(localToGlobalMatrix);

        // get the GLOBAL vector of the center of the player's head (0.2 thick) --> move position 'back' by 0.1
        Vec3 globalStartPosition = localOrigin.add(localXdirection.yRot((float)Math.PI).scale(-0.1));

        LidarScan2D scan = new LidarScan2D(horizontalScansPerRadius);
        for(int i = 0; i < horizontalScansPerRadius; i++) {
            // build LOCAL lidar ray direction vector
            float finalYawFromPOVInDeg = yawFromPOVInDegOffset + i * scanAngleDifferenceInDeg;
            float finalPitchFromPOVInDeg = pitchFromPOVInDeg;
            if(extraPitchFromPOVInDeg != 0) finalPitchFromPOVInDeg = extraPitchFromPOVInDeg;

            float yawFromPOVInRad = (float)Math.toRadians(finalYawFromPOVInDeg);
            float pitchFromPOVInRad = (float)Math.toRadians(finalPitchFromPOVInDeg);
            float rollFromPOVInRad = (float)Math.toRadians(rollFromPOVInDeg);

            Vector3f localRayDirection = new Vec3(1, 0, 0).yRot(yawFromPOVInRad).zRot(pitchFromPOVInRad).xRot(rollFromPOVInRad).toVector3f();

            // transform LOCAL lidar ray direction vector to GLOBAL lidar ray direction vector
            Vector3f globalRayDirection = localToGlobalMatrix.transform(localRayDirection);

            // create GLOBAL end position of lidar ray
            Vec3 globalEndPosition = globalStartPosition.add(new Vec3(globalRayDirection.x, globalRayDirection.y, globalRayDirection.z).scale(maximumMeasurementDistanceInMeters));

            scan.setDistance(i, this.getDistanceFromStartToBlock(globalStartPosition, globalEndPosition));
        }
        return scan;
    }

    private double getDistanceFromStartToBlock(Vec3 startPos, Vec3 endPos) {
        ClipContext context = new ClipContext(
                startPos,
                endPos,
                ClipContext.Block.OUTLINE, // check block outlines
                ClipContext.Fluid.NONE, // ignore fluids
                player
        );
        Vec3 targetPosition = level.clip(context).getLocation();
        BigDecimal bd = new BigDecimal(startPos.distanceTo(targetPosition));
        return bd.setScale(3, RoundingMode.HALF_UP).doubleValue();
    }

    public float getHorizontalScanRadiusInDeg() {
        return horizontalScanRadiusInDeg;
    }

    public float getVerticalScanRadiusInDeg() {
        return verticalScanRadiusInDeg;
    }

    public int getHorizontalScansPerRadius() {
        return horizontalScansPerRadius;
    }

    public int getVerticalScansPerRadius() {
        return verticalScansPerRadius;
    }

    public float getYawFromPOVInDeg() {
        return yawFromPOVInDeg;
    }

    public float getPitchFromPOVInDeg() {
        return pitchFromPOVInDeg;
    }

    public float getRollFromPOVInDeg() {
        return rollFromPOVInDeg;
    }

    public float getMaximumMeasurementDistanceInMeters() {
        return maximumMeasurementDistanceInMeters;
    }

    public Player getPlayer() {
        return player;
    }

    public ClientLevel getLevel() {
        return level;
    }

    public int getFrequency() {
        return frequency;
    }

    public Color getColor() {
        return color;
    }
}
