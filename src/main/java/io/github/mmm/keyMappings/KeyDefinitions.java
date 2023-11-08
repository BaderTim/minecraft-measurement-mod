package io.github.mmm.keyMappings;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.util.Lazy;

public class KeyDefinitions {

    public static final Lazy<KeyMapping> MEASURE_START_STOP_MAPPING = Lazy.of(() -> new KeyMapping(
            "Start/Stop Measuring",
            KeyConflictContext.UNIVERSAL,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_M,
            "Measurement"
    ));

    public static final Lazy<KeyMapping> MMM_SETTINGS_MAPPING = Lazy.of(() -> new KeyMapping(
            "Open Measurement Settings",
            KeyConflictContext.UNIVERSAL,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_M,
            "Measurement"
    ));

}
