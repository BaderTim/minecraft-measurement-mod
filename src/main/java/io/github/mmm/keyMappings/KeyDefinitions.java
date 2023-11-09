package io.github.mmm.keyMappings;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.mmm.MMM;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.util.Lazy;

public class KeyDefinitions {

    private static final Component CATEGORY = Component.translatable("gui." + MMM.MODID + ".controls.category.measurement");
    private static final Component MEASURE = Component.translatable("gui." + MMM.MODID + ".controls.measure");
    private static final Component SETTINGS = Component.translatable("gui." + MMM.MODID + ".controls.settings");

    public static final Lazy<KeyMapping> MEASURE_MAPPING = Lazy.of(() -> new KeyMapping(
            MEASURE.getString(),
            KeyConflictContext.UNIVERSAL,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_M,
            CATEGORY.getString()
    ));

    public static final Lazy<KeyMapping> SETTINGS_MAPPING = Lazy.of(() -> new KeyMapping(
            SETTINGS.getString(),
            KeyConflictContext.UNIVERSAL,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_M,
            CATEGORY.getString()
    ));

}
