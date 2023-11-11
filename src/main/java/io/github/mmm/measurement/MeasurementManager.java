package io.github.mmm.measurement;

import io.github.mmm.MMM;
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
        Vec3 lookVectorFromEyeView = player.getViewVector(1.0F);

        System.out.println("Eye Position: " + eyePosition);
        System.out.println("Player Position: " + playerPosition);

        double distanceFromEyeView = this.getDistanceToBlock(eyePosition, eyePosition.add(lookVectorFromEyeView.scale(maximumMeasurementDistance)), player);
        System.out.println("Eye View Distance: " + distanceFromEyeView);

        double eyeViewDistaceFromMethod = this.getDistanceFromPOVToBlock(0, 0, player, maximumMeasurementDistance);
        System.out.println("Eye View Distance from Method: " + eyeViewDistaceFromMethod);

        double inverseEyeViewDistance = this.getDistanceFromPOVToBlock(180, 0, player, maximumMeasurementDistance);
        System.out.println("Inverse Eye View Distance: " + inverseEyeViewDistance);
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

    private double getDistanceFromPOVToBlock(float yawFromPOVInDeg, float pitchFromPOVInDeg, Player player, float maximumMeasurementDistance) {
        ClientLevel level = Minecraft.getInstance().level;
        assert level != null;
        float yawFromPOVInRad = (float)Math.toRadians(yawFromPOVInDeg);
        float pitchFromPOVInRad = (float)Math.toRadians(pitchFromPOVInDeg);
        Vec3 eyePosition = player.getEyePosition();
        Vec3 directionFromEyeView = player.getViewVector(1.0F);
        // get the center of the player's head (0.2 thick) --> move position 'back' by 0.1
        Vec3 startPosition = eyePosition.add(directionFromEyeView.yRot((float)Math.PI).scale(0.1));
        // get the target direction by adding the given rotations to the direction from the eye view
        Vec3 targetDirection = directionFromEyeView.yRot(yawFromPOVInRad).xRot(pitchFromPOVInRad);
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

}
