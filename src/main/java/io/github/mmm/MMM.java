package io.github.mmm;

import com.mojang.logging.LogUtils;
import io.github.mmm.measurement.MeasurementController;
import io.github.mmm.modconfig.Config;
import net.minecraft.client.telemetry.events.WorldLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MMM.MODID)
public class MMM {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "mmm";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create Measure object
    public static final MeasurementController MEASUREMENT_CONTROLLER = new MeasurementController();
    // Always start with the device config gui
    public static ConfigGUIType latestConfigGUI = ConfigGUIType.DEVICE;
    public static enum ConfigGUIType {
        SURVEY,
        DEVICE
    };

    public MMM() {
        // Register Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC, "mmm-client-config.toml");

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void fmlClientSetupEvent(FMLClientSetupEvent event) {
        // Initialize devices
        MEASUREMENT_CONTROLLER.initDevices();
    }

}
