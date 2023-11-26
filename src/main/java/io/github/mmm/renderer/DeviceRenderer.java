package io.github.mmm.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import io.github.mmm.measurement.device.objects.LidarScan2D;
import io.github.mmm.measurement.device.types.lidar.LiDAR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
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
        int horizontalScansPerRadius = lidar.getHorizontalScansPerRadius(); //10;
        float scanAngleDifferenceInDeg = lidar.getHorizontalScanRadiusInDeg() / horizontalScansPerRadius;
        float yawFromPOVInDegOffset = lidar.getYawFromPOVInDeg() - (lidar.getHorizontalScanRadiusInDeg() / 2); // start at the left side of the scan#

        // setup LOCAL coordinate system with the GLOBAL player view vector on the LOCAL z-axis and GLOBAL player position as local 0 point
        Vec3 globalEyePosition = lidar.getPlayer().getEyePosition();
        Vec3 globalViewVector = lidar.getPlayer().getViewVector(10.0F);
        Matrix4f transformationMatrix = this.getTransformationMatrix(globalEyePosition.toVector3f(), globalViewVector.toVector3f());

        // get the GLOBAL vector of the center of the player's head (0.2 thick) --> move position 'back' by 0.1
        Vec3 globalStartPosition = globalEyePosition.add(globalViewVector.yRot((float)Math.PI).scale(-0.1));

        for(int i = 0; i < horizontalScansPerRadius; i++) {

            // build LOCAL lidar ray direction vector
            float yawFromPOVInDeg = yawFromPOVInDegOffset + i * scanAngleDifferenceInDeg;
            float pitchFromPOVInDeg = lidar.getPitchFromPOVInDeg();
            float rollFromPOVInDeg = lidar.getRollFromPOVInDeg();
            float yawFromPOVInRad = (float)Math.toRadians(yawFromPOVInDeg);
            float pitchFromPOVInRad = (float)Math.toRadians(pitchFromPOVInDeg);
            float rollFromPOVInRad = (float)Math.toRadians(rollFromPOVInDeg);
            Vec3 localRayDirection = new Vec3(0, 0, 1).yRot(yawFromPOVInRad).xRot(pitchFromPOVInRad).zRot(rollFromPOVInRad);

            // transform LOCAL lidar ray direction vector to GLOBAL lidar ray direction vector
            Vector3f globalRayDirection = this.localToGlobal(localRayDirection.toVector3f(), transformationMatrix);

            // create GLOBAL end position of lidar ray
            Vec3 globalEndPosition = globalStartPosition.add(new Vec3(globalRayDirection.x, globalRayDirection.y, globalRayDirection.z).scale(lidar.getMaximumMeasurementDistanceInMeters()));

            buffer.vertex(globalStartPosition.x, globalStartPosition.y, globalStartPosition.z).color(lidar.getColor().getRed(), lidar.getColor().getBlue(), lidar.getColor().getGreen(), 255).endVertex();
            buffer.vertex(globalEndPosition.x, globalEndPosition.y, globalEndPosition.z).color(lidar.getColor().getRed(), lidar.getColor().getBlue(), lidar.getColor().getGreen(), 255).endVertex();
        }
    }

    private void render3DLidar(LiDAR lidar) {

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


    // Function to calculate transformation matrix based on eye position and view vector
    private Matrix4f getTransformationMatrix(Vector3f AG, Vector3f BG) {
        // Calculate z-axis of local coordinate system
        Vector3f zL = new Vector3f(BG.x, BG.y, BG.z).sub(AG);
        float lengthSquared = zL.lengthSquared();

        zL.normalize();

        // Calculate a temporary right vector (x-axis)
        Vector3f right = new Vector3f();
        right.cross(zL, new Vector3f(0.0f, 1.0f, 0.0f));

        // If the cross product length is too small, use a different vector
        if (right.lengthSquared() < 1e-8f) {
            right.cross(zL, new Vector3f(1.0f, 0.0f, 0.0f));
        }
        right.normalize();

        // Calculate the local y-axis
        Vector3f yL = new Vector3f();
        yL.cross(right, zL).normalize();

        // Calculate the local x-axis
        Vector3f xL = new Vector3f();
        xL.cross(yL, zL).normalize();

        // Create the transformation matrix
        Matrix4f transformationMatrix = new Matrix4f();
        transformationMatrix.set(xL.x, yL.x, zL.x, AG.x,
                xL.y, yL.y, zL.y, AG.y,
                xL.z, yL.z, zL.z, AG.z,
                0,    0,    0,    1);

        return transformationMatrix;
    }

    // Function to convert from local to global coordinates
    private Vector3f localToGlobal(Vector3f localPoint, Matrix4f transformationMatrix) {
        Vector3f transformedPoint = new Vector3f(localPoint);
        return transformedPoint.mulPosition(transformationMatrix);
    }

    public void startVisualize() {
        visualizing = true;
    }

    public void stopVisualize() {
        visualizing = false;
    }

    public boolean isVisualizing() {
        return visualizing;
    }

}
