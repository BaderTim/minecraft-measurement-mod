package io.github.mmm.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.github.mmm.MMM;
import io.github.mmm.measurement.device.objects.lidar.LiDAR;
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
        this.render2DLidar(lidar, 0, lidar.getGlobalStartPosition(), lidar.getLocalToGlobalMatrix());
    }

    private void render2DLidar(LiDAR lidar, float pitchFromPOVInDeg, Vec3 globalStartPosition, Matrix3f localToGlobalMatrix) {
        float scanAngleDifferenceInDeg = lidar.getHorizontalScanRadiusInDeg() / lidar.getHorizontalScansPerRadius();
        float yawFromPOVInDegOffset = lidar.getYawFromPOVInDeg() - (lidar.getHorizontalScanRadiusInDeg() / 2); // start at the left side of the scan#

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
        Vec3 globalStartPosition = lidar.getGlobalStartPosition();
        Matrix3f localToGlobalMatrix = lidar.getLocalToGlobalMatrix();
        for(int i = 0; i < lidar.getVerticalScansPerRadius(); i++) {
            float pitchFromPOVFor3DScanInDeg2D = pitchFromPOVInDegOffset - i * verticalScanAngleDifferenceInDeg;
            this.render2DLidar(lidar, pitchFromPOVFor3DScanInDeg2D, globalStartPosition, localToGlobalMatrix);
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
