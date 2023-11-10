package io.github.mmm.measurement;

import io.github.mmm.MMM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
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
        float horizontalRotation = player.getXRot(); // x is around the horizontal axis
        float verticalRotation = player.getYRot(); // y is around the vertical axis
        Vec3 eyePosition = player.getEyePosition();
        Vec3 lookVector = player.getViewVector(1.0F);

        double distance = this.getDistanceToBlock(eyePosition, eyePosition.add(lookVector.scale(maximumMeasurementDistance)));
        System.out.println("Distance: " + distance);
    }

    public void stopMeasure() {
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.stop"), false);
        currentlyMeasuring = false;
    }


    private double getDistanceToBlock(Vec3 startPos, Vec3 endPos) {
        LocalPlayer player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;
        assert player != null;
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

}
