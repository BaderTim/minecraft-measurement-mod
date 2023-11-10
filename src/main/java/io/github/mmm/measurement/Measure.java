package io.github.mmm.measurement;

import io.github.mmm.MMM;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class Measure {

    public static Boolean currentlyMeasuring = false;

    public static void startMeasure() {
        currentlyMeasuring = true;
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.start"), false);
    }

    public static void stopMeasure() {
        currentlyMeasuring = false;
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("chat." + MMM.MODID + ".measure.stop"), false);
    }

}
