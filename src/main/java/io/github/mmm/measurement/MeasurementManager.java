package io.github.mmm.measurement;

import io.github.mmm.MMM;
import io.github.mmm.measurement.utils.Scan2D;
import io.github.mmm.measurement.utils.Scan3D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;

import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MeasurementManager {

    private Boolean currentlyMeasuring;
    private final String filePath = Minecraft.getInstance().gameDirectory.getPath() + "/measurements/";

    public MeasurementManager() {
        System.out.println("Measure constructor");
        this.currentlyMeasuring = false;
        try {
            System.out.println("Creating directory: " + this.filePath);
            Files.createDirectories(Paths.get(this.filePath));
        } catch (Exception e) {
            System.out.println("Error creating directory: " + e.getMessage());
        }
    }

    public Boolean isCurrentlyMeasuring() {
        return this.currentlyMeasuring;
    }

    public void startMeasure() {
        this.currentlyMeasuring = true;
        float maximumMeasurementDistance = 10.0f;
        LocalPlayer player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;
        assert player != null;
        assert level != null;
        player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.start"), false);

        Scan3D scan3D = get3DScanFromPOVToBlocks(180, 100, 60, 60, 0, 0, 0, player, maximumMeasurementDistance);
        this.saveStringToFile("timestamp;scan3d\n", "scan3d.csv");
        this.saveScanToFile(scan3D, "scan3d.csv");

    }

    public void stopMeasure() {
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.stop"), false);
        this.currentlyMeasuring = false;
    }

    private void saveScanToFile(Scan2D scan2D, String fileName) {
        String distances = scan2D.getDistances().toString();
        long timestamp = System.currentTimeMillis();
        String result = timestamp + ";" + distances + "\n";
        this.saveStringToFile(result, fileName);
    }

    private void saveScanToFile(Scan3D scan3D, String fileName) {
        Scan2D[] distances = scan3D.getDistances();
        String distancesString = "";
        for (int i = 0; i < distances.length - 1; i++) {
            distancesString += java.util.Arrays.toString(distances[i].getDistances()) + ",";
        }
        distancesString += java.util.Arrays.toString(distances[distances.length - 1].getDistances());
        long timestamp = System.currentTimeMillis();
        String result = timestamp + ";[" + distancesString + "]\n";
        this.saveStringToFile(result, fileName);
    }

    private void saveStringToFile(String newData, String fileName) {

        final String savePath = this.filePath + fileName;

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


    private double getDistanceToBlock(Vec3 startPos, Vec3 endPos, Player player) {
        ClientLevel level = Minecraft.getInstance().level;
        assert level != null;
        ClipContext context = new ClipContext(
                startPos,
                endPos,
                ClipContext.Block.OUTLINE, // check block outlines
                ClipContext.Fluid.NONE, // ignore fluids
                player
        );
        Vec3 targetPosition = Minecraft.getInstance().level.clip(context).getLocation();
        return startPos.distanceTo(targetPosition);
    }

    private double getDistanceFromPOVToBlock(float yawFromPOVInDeg, float pitchFromPOVInDeg, float rollFromPOVInDeg, Player player, float maximumMeasurementDistance) {
        ClientLevel level = Minecraft.getInstance().level;
        assert level != null;
        float yawFromPOVInRad = (float)Math.toRadians(yawFromPOVInDeg);
        float pitchFromPOVInRad = (float)Math.toRadians(pitchFromPOVInDeg);
        float rollFromPOVInRad = (float)Math.toRadians(rollFromPOVInDeg);
        Vec3 eyePosition = player.getEyePosition();
        Vec3 directionFromEyeView = player.getViewVector(1.0F);
        // get the center of the player's head (0.2 thick) --> move position 'back' by 0.1
        Vec3 startPosition = eyePosition.add(directionFromEyeView.yRot((float)Math.PI).scale(0.1));
        // get the target direction by adding the given rotations to the direction from the eye view
        Vec3 targetDirection = directionFromEyeView.yRot(yawFromPOVInRad).xRot(pitchFromPOVInRad).zRot(rollFromPOVInRad);
        Vec3 endPosition = startPosition.add(targetDirection.scale(maximumMeasurementDistance));
        ClipContext context = new ClipContext(
                startPosition,
                endPosition,
                ClipContext.Block.OUTLINE, // check block outlines
                ClipContext.Fluid.NONE, // ignore fluids
                player
        );
        Vec3 targetPosition = Minecraft.getInstance().level.clip(context).getLocation();
        BigDecimal bd = new BigDecimal(startPosition.distanceTo(targetPosition));
        return bd.setScale(3, RoundingMode.HALF_UP).doubleValue();
    }

    private Scan2D get2DScanFromPOVToBlocks(
            float scanRadiusInDeg,
            int scansPerRadius,
            float yawFromPOVInDeg,
            float pitchFromPOVInDeg,
            float rollFromPOVInDeg,
            Player player,
            float maximumMeasurementDistance
    ) {
        float scanAngleDifferenceInDeg = scanRadiusInDeg / scansPerRadius;
        float yawFromPOVInDegOffset = yawFromPOVInDeg - (scanRadiusInDeg / 2); // start at the left side of the scan
        Scan2D scan = new Scan2D(scansPerRadius);
        for(int i = 0; i < scansPerRadius; i++) {
            float yawFromPOVInDeg1D = yawFromPOVInDegOffset + i * scanAngleDifferenceInDeg;
            scan.setDistance(i, this.getDistanceFromPOVToBlock(yawFromPOVInDeg1D, pitchFromPOVInDeg, rollFromPOVInDeg, player, maximumMeasurementDistance));
        }
        return scan;
    }

    private Scan3D get3DScanFromPOVToBlocks(
            float horizontalScanRadiusInDeg,
            int scansPerHorizontalRadius,
            float verticalScanRadiusInDeg,
            int scansPerVerticalRadius,
            float yawFromPOVInDeg,
            float pitchFromPOVInDeg,
            float rollFromPOVInDeg,
            Player player,
            float maximumMeasurementDistance
    ) {
        float verticalScanAngleDifferenceInDeg = verticalScanRadiusInDeg / scansPerVerticalRadius;
        float pitchFromPOVInDegOffset = pitchFromPOVInDeg + (verticalScanRadiusInDeg / 2); // start at the top of the scan
        Scan3D scan3D = new Scan3D(scansPerHorizontalRadius, scansPerVerticalRadius);
        for(int i = 0; i < scansPerVerticalRadius; i++) {
            float pitchFromPOVInDeg2D = pitchFromPOVInDegOffset - i * verticalScanAngleDifferenceInDeg;
            Scan2D scan2D = this.get2DScanFromPOVToBlocks(
                    horizontalScanRadiusInDeg,
                    scansPerHorizontalRadius,
                    yawFromPOVInDeg,
                    pitchFromPOVInDeg2D,
                    rollFromPOVInDeg,
                    player,
                    maximumMeasurementDistance
            );
            scan3D.setScan2D(i, scan2D);
        }
        return scan3D;
    }

}
