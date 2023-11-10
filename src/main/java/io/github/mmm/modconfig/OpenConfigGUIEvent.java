package io.github.mmm.modconfig;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.mmm.MMM.LOGGER;
import static io.github.mmm.MMM.MODID;
import static io.github.mmm.keymappings.KeyDefinitions.SETTINGS_MAPPING;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class OpenConfigGUIEvent {

    // Listen to Key Bindings
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Only call code once as the tick event is called twice every tick
        if (event.phase == TickEvent.Phase.END) {
            if (SETTINGS_MAPPING.get().consumeClick()) {
                LOGGER.info("SETTINGS_MAPPING is pressed");
                Minecraft.getInstance().setScreen(new ConfigGUI());
            }
        }
    }
}
