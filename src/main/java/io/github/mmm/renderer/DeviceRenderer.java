package io.github.mmm.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.github.mmm.MMM;
import io.github.mmm.measurement.device.objects.LidarScan2D;
import io.github.mmm.measurement.device.objects.LidarScan3D;
import io.github.mmm.measurement.device.types.lidar.LiDAR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import static io.github.mmm.MMM.DEVICE_CONTROLLER;

public class DeviceRenderer {

    private boolean visualizing = false;

    private Tesselator tesselator;
    private BufferBuilder buffer;
    private VertexBuffer vertexBuffer;

    public DeviceRenderer() {

    }

    public void render(RenderLevelStageEvent event) {
        if(visualizing) {
            beginRender();
            if (DEVICE_CONTROLLER.getLidar1() != null) renderLidar(DEVICE_CONTROLLER.getLidar1());
            if (DEVICE_CONTROLLER.getLidar2() != null) renderLidar(DEVICE_CONTROLLER.getLidar2());
            if (DEVICE_CONTROLLER.getLidar3() != null) renderLidar(DEVICE_CONTROLLER.getLidar3());
            endRender(event);
        }
    }

    private void renderLidar(LiDAR lidar) {
        if(lidar.getVerticalScanRadiusInDeg() == 0 && lidar.getVerticalScansPerRadius() == 0) render2DLidar(lidar);
        else render3DLidar(lidar);
    }

    private void render2DLidar(LiDAR lidar) {
        this.render2DLidar(lidar, 0);
    }

    private void render2DLidar(LiDAR lidar, float pitchFromPOVInDeg) {
        float scanAngleDifferenceInDeg = lidar.getHorizontalScanRadiusInDeg() / lidar.getHorizontalScansPerRadius();
        float yawFromPOVInDegOffset = lidar.getYawFromPOVInDeg() - (lidar.getHorizontalScanRadiusInDeg() / 2); // start at the left side of the scan#

        // setup LOCAL coordinate system with GLOBAL pov coordinates
        // LOCAL x1 x-axis is view vector
        // LOCAL x3 y-axis is up vector
        // LOCAL x2 z-axis is cross product of x-axis and y-axis
        Vec3 localOrigin = lidar.getPlayer().getEyePosition();
        Vec3 localXdirection = lidar.getPlayer().getViewVector(1.0F);
        Vec3 localYdirection = lidar.getPlayer().getUpVector(1.0F);
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

        for(int i = 0; i < lidar.getHorizontalScansPerRadius(); i++) {

            // build LOCAL lidar ray direction vector
            float yawFromPOVInDeg = yawFromPOVInDegOffset + i * scanAngleDifferenceInDeg;
            if(pitchFromPOVInDeg == 0) pitchFromPOVInDeg = lidar.getPitchFromPOVInDeg();
            float rollFromPOVInDeg = lidar.getRollFromPOVInDeg();

            float yawFromPOVInRad = (float)Math.toRadians(yawFromPOVInDeg);
            float pitchFromPOVInRad = (float)Math.toRadians(pitchFromPOVInDeg);
            float rollFromPOVInRad = (float)Math.toRadians(rollFromPOVInDeg);

            Vector3f localRayDirection = new Vec3(1, 0, 0).yRot(yawFromPOVInRad).zRot(pitchFromPOVInRad).xRot(rollFromPOVInRad).toVector3f();

            // transform LOCAL lidar ray direction vector to GLOBAL lidar ray direction vector
            Vector3f globalRayDirection = localToGlobalMatrix.transform(localRayDirection);

            // create GLOBAL end position of lidar ray
            Vec3 globalEndPosition = globalStartPosition.add(new Vec3(globalRayDirection.x, globalRayDirection.y, globalRayDirection.z).scale(lidar.getMaximumMeasurementDistanceInMeters()));

            buffer.vertex(globalStartPosition.x, globalStartPosition.y, globalStartPosition.z).color(lidar.getColor().getRed(), lidar.getColor().getBlue(), lidar.getColor().getGreen(), 255).endVertex();
            buffer.vertex(globalEndPosition.x, globalEndPosition.y, globalEndPosition.z).color(lidar.getColor().getRed(), lidar.getColor().getBlue(), lidar.getColor().getGreen(), 255).endVertex();
        }
    }

    private void render3DLidar(LiDAR lidar) {
        float verticalScanAngleDifferenceInDeg = lidar.getVerticalScanRadiusInDeg() / lidar.getVerticalScansPerRadius();
        float pitchFromPOVInDegOffset = lidar.getPitchFromPOVInDeg() + (lidar.getVerticalScanRadiusInDeg() / 2); // start at the top of the scan

        for(int i = 0; i < lidar.getVerticalScansPerRadius(); i++) {
            float pitchFromPOVFor3DScanInDeg2D = pitchFromPOVInDegOffset - i * verticalScanAngleDifferenceInDeg;
            this.render2DLidar(lidar, pitchFromPOVFor3DScanInDeg2D);
        }
    }

    private void beginRender() {
        RenderSystem.enableDepthTest();
        this.tesselator = Tesselator.getInstance();
        this.buffer = tesselator.getBuilder();
        this.vertexBuffer = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
    }

    private void endRender(RenderLevelStageEvent event) {
        vertexBuffer.bind();
        vertexBuffer.upload(buffer.end());
        Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        PoseStack matrix = event.getPoseStack();
        matrix.pushPose();
        matrix.translate(-view.x, -view.y, -view.z);
        ShaderInstance shader = GameRenderer.getPositionColorShader();
        vertexBuffer.drawWithShader(matrix.last().pose(), event.getProjectionMatrix(), shader);
        matrix.popPose();
        VertexBuffer.unbind();
    }


    public void startVisualize() {
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".visualize.start"), false);
        visualizing = true;
    }

    public void stopVisualize() {
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".visualize.stop"), false);
        visualizing = false;
    }

    public boolean isVisualizing() {
        return visualizing;
    }

}
