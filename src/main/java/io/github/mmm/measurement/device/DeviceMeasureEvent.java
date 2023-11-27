package io.github.mmm.measurement.device;

import io.github.mmm.measurement.device.scans.LidarScan;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

import java.util.ArrayList;

import static io.github.mmm.MMM.DEVICE_CONTROLLER;
import static io.github.mmm.MMM.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DeviceMeasureEvent {

    private static int passedTicks = 0;
    private static Vector3f previousPosition;
    private static Vector3f previousRotation;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
            Vector3f currentPosition = player.position().toVector3f();
            Vector3f currentRotation = player.getViewVector(1).toVector3f();
            if (DEVICE_CONTROLLER.isCurrentlyMeasuring()) {
                DEVICE_CONTROLLER.getImuController().scan(passedTicks, currentPosition, previousPosition, currentRotation, previousRotation);
                DEVICE_CONTROLLER.getLidarController().scan(passedTicks);
                if (passedTicks > 0 && passedTicks % DEVICE_CONTROLLER.getSaveInterval() == 0) {
                    DEVICE_CONTROLLER.saveIMUScansToFile(DEVICE_CONTROLLER.getImuController().getScans(), "imu1.csv");
                    ArrayList<LidarScan>[] scans = DEVICE_CONTROLLER.getLidarController().getScans();
                    for (int i = 0; i < scans.length; i++) {
                        DEVICE_CONTROLLER.saveLiDARScansToFile(scans[i], "lidar" + (i + 1) + ".csv");
                    }
                    DEVICE_CONTROLLER.getLidarController().clearScans();
                }
                if (passedTicks == Integer.MAX_VALUE) passedTicks = 0;
                passedTicks++;
            }
            previousPosition = currentPosition;
            previousRotation = currentRotation;
        }
    }

}
