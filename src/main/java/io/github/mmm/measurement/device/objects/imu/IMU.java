package io.github.mmm.measurement.device.objects.imu;

import io.github.mmm.measurement.device.scans.ImuScan;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class IMU {

    private static final double G = 9.80665;
    private static final float timeDelta = 1.0F / 20;

    private boolean considerGravity;
    private int frequency;

    private Player player;

    private Quaternionf yawPitchRoll;

    public IMU(boolean considerGravity, float yawFromPOVInDeg, float pitchFromPOVInDeg, float rollFromPOVInDeg, int frequency, Player player) {
        this.considerGravity = considerGravity;
        this.frequency = frequency;
        this.player = player;
        this.yawPitchRoll = quaternionFromEulerDegrees(new Vector3f(yawFromPOVInDeg, pitchFromPOVInDeg, rollFromPOVInDeg));

    }

    private Quaternionf quaternionFromEulerDegrees(Vector3f euler) {
        return new Quaternionf().rotationXYZ((float)Math.toRadians(euler.x), (float)Math.toRadians(euler.y), (float)Math.toRadians(euler.z));
    }

    public ImuScan getScan(Vector3f positionCurrent, Vector3f positionPrevious,
                                        Vector3f rotationCurrent, Vector3f rotationPrevious) { // rotation is euler angles in radians
        Matrix4f rotationMatrixCurrent = new Matrix4f().rotate(yawPitchRoll).rotateX(rotationCurrent.x).rotateY(rotationCurrent.y).rotateZ(rotationCurrent.z);
        Matrix4f rotationMatrixPrevious = new Matrix4f().rotate(yawPitchRoll).rotateX(rotationPrevious.x).rotateY(rotationPrevious.y).rotateZ(rotationPrevious.z);

        Vector3f linearVelocity = calculateLinearVelocity(positionCurrent, positionPrevious);
        Vector3f angularVelocity = calculateAngularVelocity(rotationMatrixCurrent, rotationMatrixPrevious);

        Vector3f linearAcceleration = calculateLinearAcceleration(linearVelocity, angularVelocity,
                rotationMatrixCurrent, rotationMatrixPrevious);

        return new ImuScan(
                player.position().x,
                player.position().y,
                player.position().z,
                player.getViewVector(1.0F).x,
                player.getViewVector(1.0F).y,
                player.getViewVector(1.0F).z,
                linearAcceleration.x,
                linearAcceleration.y,
                linearAcceleration.z,
                angularVelocity.x,
                angularVelocity.y,
                angularVelocity.z
        );
    }

    private Matrix4f rotationMatrixFromEuler(Vector3f rotationAngles) {
        return new Matrix4f().rotationXYZ(rotationAngles.x, rotationAngles.y, rotationAngles.z);
    }

    private Vector3f calculateLinearVelocity(Vector3f positionCurrent, Vector3f positionPrevious) {
        return positionCurrent.sub(positionPrevious, new Vector3f()).div(timeDelta);
    }

    private Vector3f calculateAngularVelocity(Matrix4f rotationMatrixCurrent, Matrix4f rotationMatrixPrevious) {
        Matrix4f rotationChangeMatrix = rotationMatrixCurrent.mul(rotationMatrixPrevious.invert(), new Matrix4f());
        Vector3f axisAngle = new Vector3f().set(
                rotationChangeMatrix.m12() - rotationChangeMatrix.m21(),
                rotationChangeMatrix.m20() - rotationChangeMatrix.m02(),
                rotationChangeMatrix.m01() - rotationChangeMatrix.m10()).mul(0.5f / timeDelta);
        return axisAngle;
    }

    private Vector3f calculateLinearAcceleration(Vector3f linearVelocity, Vector3f angularVelocity,
                                                       Matrix4f rotationMatrixCurrent, Matrix4f rotationMatrixPrevious) {
        Vector3f transformedLinearVelocity = rotationMatrixPrevious.transpose(new Matrix4f()).transformDirection(linearVelocity, new Vector3f());
        Vector3f deltaLinearVelocity = linearVelocity.sub(transformedLinearVelocity, new Vector3f());
        return deltaLinearVelocity.div(timeDelta).sub(angularVelocity.cross(transformedLinearVelocity, new Vector3f()));
    }

    public int getFrequency() {
        return frequency;
    }
}
