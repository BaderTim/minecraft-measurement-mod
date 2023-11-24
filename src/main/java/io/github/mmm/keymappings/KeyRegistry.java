package io.github.mmm.keymappings;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static io.github.mmm.MMM.MODID;
import static io.github.mmm.keymappings.KeyDefinitions.*;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyRegistry {

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(SETTINGS_MAPPING.get());
        event.register(MEASURE_MAPPING.get());
        event.register(VISUALIZE_MAPPING.get());
    }
}
