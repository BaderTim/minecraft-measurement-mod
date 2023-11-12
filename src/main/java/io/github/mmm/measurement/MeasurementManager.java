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

public class MeasurementManager {

    private Boolean currentlyMeasuring;

    public MeasurementManager() {
        System.out.println("Measure constructor");
        currentlyMeasuring = false;
    }

    public Boolean isCurrentlyMeasuring() {
        return currentlyMeasuring;
    }

    public void startMeasure() {
        currentlyMeasuring = true;
        float maximumMeasurementDistance = 10.0f;
        LocalPlayer player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;
        assert player != null;
        assert level != null;
        player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.start"), false);


        Vec3 eyePosition = player.getEyePosition();
        Vec3 playerPosition = player.position();

        System.out.println("Eye Position: " + eyePosition);
        System.out.println("Player Position: " + playerPosition);

        Scan2D scan2D = get2DScanFromPOVToBlocks(180, 100, 0, 0, 0, player, maximumMeasurementDistance);
        System.out.println("Scan2D: " + scan2D);

        Scan3D scan3D = get3DScanFromPOVToBlocks(180, 100, 60, 60, 0, 0, 0, player, maximumMeasurementDistance);
        System.out.println("Scan3D: " + scan3D);
    }

    public void stopMeasure() {
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.stop"), false);
        currentlyMeasuring = false;
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
        return startPosition.distanceTo(targetPosition);
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
        float yawFromPOVInDegOffset = yawFromPOVInDeg - (scanRadiusInDeg / 2);
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
        float pitchFromPOVInDegOffset = pitchFromPOVInDeg - (verticalScanRadiusInDeg / 2);
        Scan3D scan3D = new Scan3D(scansPerHorizontalRadius, scansPerVerticalRadius);
        for(int i = 0; i < scansPerVerticalRadius; i++) {
            float pitchFromPOVInDeg2D = pitchFromPOVInDegOffset + i * verticalScanAngleDifferenceInDeg;
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
