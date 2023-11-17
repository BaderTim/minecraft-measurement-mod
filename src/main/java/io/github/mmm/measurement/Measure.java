package io.github.mmm.measurement;

import io.github.mmm.measurement.objects.LidarScan;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

import java.util.ArrayList;

import static io.github.mmm.MMM.MEASUREMENT_CONTROLLER;
import static io.github.mmm.MMM.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class Measure {

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
            if (MEASUREMENT_CONTROLLER.isCurrentlyMeasuring()) {
                MEASUREMENT_CONTROLLER.getImuController().scan(passedTicks, currentPosition, previousPosition, currentRotation, previousRotation);
                MEASUREMENT_CONTROLLER.getLidarController().scan(passedTicks);
                if (passedTicks % MEASUREMENT_CONTROLLER.getSaveInterval() == 0) {
                    MEASUREMENT_CONTROLLER.saveIMUScansToFile(MEASUREMENT_CONTROLLER.getImuController().getScans(), "imu.csv");
                    ArrayList<LidarScan>[] scans = MEASUREMENT_CONTROLLER.getLidarController().getScans();
                    for (int i = 0; i < scans.length; i++) {
                        MEASUREMENT_CONTROLLER.saveLiDARScansToFile(scans[i], "lidar" + (i + 1) + ".csv");
                    }
                    MEASUREMENT_CONTROLLER.getLidarController().clearScans();
                }
                if (passedTicks == Integer.MAX_VALUE) passedTicks = 0;
                passedTicks++;
            }
            previousPosition = currentPosition;
            previousRotation = currentRotation;
        }
    }

}
