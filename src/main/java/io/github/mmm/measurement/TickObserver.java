package io.github.mmm.measurement;

import io.github.mmm.MMM;
import io.github.mmm.modconfig.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.mmm.MMM.*;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TickObserver {

    public static final int TICKS_PER_SECOND = 20;
    public static int messageTimeoutInTicks = TICKS_PER_SECOND*5; // 5 seconds
    public static long lastTickTime = 0;
    public static int tickCount = 0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(!MEASUREMENT_CONTROLLER.isTickTimeWarning()) return;
        Player player = Minecraft.getInstance().player;
        if (event.phase == TickEvent.Phase.END && player != null) {
            long currentTime = System.currentTimeMillis();
            if (lastTickTime == 0) {
                lastTickTime = currentTime;
                return;
            }
            if (MEASUREMENT_CONTROLLER.isCurrentlyMeasuring() && (currentTime - lastTickTime - 1000/TICKS_PER_SECOND - MEASUREMENT_CONTROLLER.getTickTimeWarningTolerance()) > 0) {
                if (tickCount > messageTimeoutInTicks) {
                    player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".warning.tickrate"), false);
                    LOGGER.warn("Tick time took too long: " + (currentTime - lastTickTime) + "ms");
                    tickCount = 0;
                }
            }
            if(tickCount > 1000000) tickCount = messageTimeoutInTicks; // avoid overflow
            tickCount++;
            lastTickTime = currentTime;
        }
    }
}
