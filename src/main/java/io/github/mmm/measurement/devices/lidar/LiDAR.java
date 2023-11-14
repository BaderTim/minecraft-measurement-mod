package io.github.mmm.measurement.devices.lidar;

import io.github.mmm.measurement.objects.Scan;
import io.github.mmm.measurement.objects.Scan2D;
import io.github.mmm.measurement.objects.Scan3D;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;

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

    private Player player;
    private ClientLevel level;

    // 2D
    public LiDAR(
            float horizontalScanRadiusInDeg,
            int horizontalScansPerRadius,
            float yawFromPOVInDeg,
            float pitchFromPOVInDeg,
            float rollFromPOVInDeg,
            float maximumMeasurementDistanceInMeters,
            Player player,
            ClientLevel level
    ) {
        this.verticalScanRadiusInDeg = 0;
        this.verticalScansPerRadius = 0;
        this.horizontalScanRadiusInDeg = horizontalScanRadiusInDeg;
        this.horizontalScansPerRadius = horizontalScansPerRadius;
        this.yawFromPOVInDeg = yawFromPOVInDeg;
        this.pitchFromPOVInDeg = pitchFromPOVInDeg;
        this.rollFromPOVInDeg = rollFromPOVInDeg;
        this.maximumMeasurementDistanceInMeters = maximumMeasurementDistanceInMeters;
        this.player = player;
        this.level = level;
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
            ClientLevel level
    ) {
        this.horizontalScanRadiusInDeg = horizontalScanRadiusInDeg;
        this.verticalScanRadiusInDeg = verticalScanRadiusInDeg;
        this.horizontalScansPerRadius = horizontalScansPerRadius;
        this.verticalScansPerRadius = verticalScansPerRadius;
        this.yawFromPOVInDeg = yawFromPOVInDeg;
        this.pitchFromPOVInDeg = pitchFromPOVInDeg;
        this.rollFromPOVInDeg = rollFromPOVInDeg;
        this.maximumMeasurementDistanceInMeters = maximumMeasurementDistanceInMeters;
        this.player = player;
        this.level = level;
    }

    public Scan scanFromPOVToBlocks() {
        if(verticalScanRadiusInDeg == 0 && verticalScansPerRadius == 0) {
            return new Scan(this.get2DScanFromPOVToBlocks());
        } else {
            return new Scan(this.get3DScanFromPOVToBlocks());
        }
    }

    private Scan3D get3DScanFromPOVToBlocks() {
        float verticalScanAngleDifferenceInDeg = verticalScanRadiusInDeg / verticalScansPerRadius;
        float pitchFromPOVInDegOffset = pitchFromPOVInDeg + (verticalScanRadiusInDeg / 2); // start at the top of the scan
        Scan3D scan3D = new Scan3D(horizontalScansPerRadius, verticalScansPerRadius);
        for(int i = 0; i < verticalScansPerRadius; i++) {
            float pitchFromPOVFor3DScanInDeg2D = pitchFromPOVInDegOffset - i * verticalScanAngleDifferenceInDeg;
            Scan2D scan2D = this.get2DScanFromPOVToBlocks(pitchFromPOVFor3DScanInDeg2D);
            scan3D.setScan2D(i, scan2D);
        }
        return scan3D;
    }

    private Scan2D get2DScanFromPOVToBlocks() {
        float scanAngleDifferenceInDeg = horizontalScanRadiusInDeg / horizontalScansPerRadius;
        float yawFromPOVInDegOffset = yawFromPOVInDeg - (horizontalScanRadiusInDeg / 2); // start at the left side of the scan
        Scan2D scan = new Scan2D(horizontalScansPerRadius);
        for(int i = 0; i < horizontalScansPerRadius; i++) {
            float yawFromPOVInDeg1D = yawFromPOVInDegOffset + i * scanAngleDifferenceInDeg;
            scan.setDistance(i, this.getDistanceFromPOVToBlock(yawFromPOVInDeg1D));
        }
        return scan;
    }

    // overloaded method for 3D scan
    private Scan2D get2DScanFromPOVToBlocks(float pitchFromPOVFor3DScanInDeg2D) {
        float scanAngleDifferenceInDeg = horizontalScanRadiusInDeg / horizontalScansPerRadius;
        float yawFromPOVInDegOffset = yawFromPOVInDeg - (horizontalScanRadiusInDeg / 2); // start at the left side of the scan
        Scan2D scan = new Scan2D(horizontalScansPerRadius);
        for(int i = 0; i < horizontalScansPerRadius; i++) {
            float yawFromPOVFor2DScanInDeg = yawFromPOVInDegOffset + i * scanAngleDifferenceInDeg;
            scan.setDistance(i, this.getDistanceFromPOVToBlock(yawFromPOVFor2DScanInDeg, pitchFromPOVFor3DScanInDeg2D));
        }
        return scan;
    }

    // overloaded method for 2D scan
    private double getDistanceFromPOVToBlock(float yawFromPOVFor2DScanInDeg) {
        float yawFromPOVInRad = (float)Math.toRadians(yawFromPOVFor2DScanInDeg);
        float pitchFromPOVInRad = (float)Math.toRadians(pitchFromPOVInDeg);
        float rollFromPOVInRad = (float)Math.toRadians(rollFromPOVInDeg);
        return this.getDistanceFromPOVToBlock(yawFromPOVInRad, pitchFromPOVInRad, rollFromPOVInRad);
    }

    // overloaded method for 3D scan
    private double getDistanceFromPOVToBlock(float yawFromPOVFor2DScanInDeg, float pitchFromPOVFor3DScanInDeg2D) {
        float yawFromPOVInRad = (float)Math.toRadians(yawFromPOVFor2DScanInDeg);
        float pitchFromPOVInRad = (float)Math.toRadians(pitchFromPOVFor3DScanInDeg2D);
        float rollFromPOVInRad = (float)Math.toRadians(rollFromPOVInDeg);
        return this.getDistanceFromPOVToBlock(yawFromPOVInRad, pitchFromPOVInRad, rollFromPOVInRad);
    }

    private double getDistanceFromPOVToBlock(float yawFromPOVInRad, float pitchFromPOVInRad, float rollFromPOVInRad) {
        Vec3 eyePosition = player.getEyePosition();
        Vec3 directionFromEyeView = player.getViewVector(1.0F);
        // get the center of the player's head (0.2 thick) --> move position 'back' by 0.1
        Vec3 startPosition = eyePosition.add(directionFromEyeView.yRot((float)Math.PI).scale(0.1));
        // get the target direction by adding the given rotations to the direction from the eye view
        Vec3 targetDirection = directionFromEyeView.yRot(yawFromPOVInRad).xRot(pitchFromPOVInRad).zRot(rollFromPOVInRad);
        Vec3 endPosition = startPosition.add(targetDirection.scale(maximumMeasurementDistanceInMeters));
        ClipContext context = new ClipContext(
                startPosition,
                endPosition,
                ClipContext.Block.OUTLINE, // check block outlines
                ClipContext.Fluid.NONE, // ignore fluids
                player
        );
        Vec3 targetPosition = level.clip(context).getLocation();
        BigDecimal bd = new BigDecimal(startPosition.distanceTo(targetPosition));
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

    public static class SubLiDAR implements Runnable {

        public SubLiDAR() {
        }

        @Override
        public void run() {

        }
    }
}
